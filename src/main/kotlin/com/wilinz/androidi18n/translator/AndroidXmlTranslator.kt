package com.wilinz.androidi18n.translator

import com.intellij.openapi.vfs.VirtualFile
import com.wilinz.androidi18n.util.Language
import org.dom4j.Document
import org.dom4j.DocumentHelper
import org.dom4j.Element
import org.dom4j.io.OutputFormat
import org.dom4j.io.SAXReader
import org.dom4j.io.XMLWriter
import java.io.File
import java.io.Reader

object AndroidXmlTranslator {

    fun translateForIntellij(
        resourceDir: VirtualFile,
        documentReader: Reader,
        form: Language,
        to: List<Language>,
        onEachStart: ((index: Int, language: Language) -> Unit)? = null,
        onEachSuccess: ((index: Int, language: Language) -> Unit)? = null,
        onEachError: ((index: Int, language: Language, error: Throwable) -> Unit)? = null,
    ): Translator {
        val translator = GoogleTranslator()
        val saxReader = SAXReader()
        translateXml(
            translator = translator,
            oldDocument = saxReader.read(documentReader),
            newDocumentFetcher = { index, language ->
                val languageDir = File(resourceDir.path, language.directoryName)
                val stringsFile = File(languageDir, "strings.xml")
                if (stringsFile.exists()) saxReader.read(stringsFile.inputStream()) else null
            },
            form = form,
            to = to,
            onEachSuccess = { index, document, language ->
                val languageDir = File(resourceDir.path, language.directoryName)
                languageDir.mkdirs()
                val stringsFile = File(languageDir, "strings.xml")
                if (!stringsFile.exists()) stringsFile.createNewFile()
                val xmlWriter = XMLWriter(stringsFile.outputStream(), OutputFormat.createPrettyPrint()).apply {
                    isEscapeText = false
                }
                xmlWriter.write(document)
                onEachSuccess?.invoke(index, language)
            },
            onEachStart = onEachStart,
            onEachError = onEachError
        )
        return translator
    }

    fun translateXml(
        translator: Translator,
        oldDocument: Document,
        newDocumentFetcher: ((index: Int, Language) -> Document?)? = null,
        form: Language,
        to: List<Language>,
        onEachStart: ((index: Int, language: Language) -> Unit)? = null,
        onEachSuccess: ((index: Int, Document, language: Language) -> Unit)? = null,
        onEachError: ((index: Int, language: Language, error: Throwable) -> Unit)? = null,
        onEachFinish: (() -> Unit)? = null
    ) {
        val translatableList = oldDocument.rootElement.createCopy().getTranslatableTags()
        for (languageIndex in to.indices) {
            val toLanguage = to[languageIndex]
            onEachStart?.invoke(languageIndex, toLanguage)
            try {
                val translatableStringList = translatableList.mapToStringList()
                val translateResult = translator.translate(
                    translatableStringList, form.code, toLanguage.code
                )
                if (translateResult.size != translatableStringList.size) continue
                val newDocument = newDocumentFetcher?.invoke(languageIndex, toLanguage)
                val newDocument1 = newDocument ?: DocumentHelper.createDocument()
                    .apply { addElement("resources") }
                val root = newDocument1.rootElement
                var i = 0
                translatableList.forEach { translateElement ->
                    val node = if (newDocument != null) {
                        root.elements().firstOrNull {
                            it.getNameAttrValue() == translateElement.getNameAttrValue()
                        } ?: translateElement.createCopy().also { root.add(it) }
                    } else translateElement.createCopy().also { root.add(it) }
                    when (node.name) {
                        StringTag -> {
                            node.text = translateResult[i++]
                        }
                        PluralsTag, StringArrayTag -> {
                            node.elements().forEach { item ->
                                item.text = translateResult[i++]
                            }
                        }
                    }
                }
                onEachSuccess?.invoke(languageIndex, newDocument1, toLanguage)
            } catch (e: Exception) {
                e.printStackTrace()
                onEachError?.invoke(languageIndex, toLanguage, e)
            } finally {
                onEachFinish?.invoke()
            }
        }
    }

    private fun List<Element>.mapToStringList(): List<String> {
        val stringList = mutableListOf<String>()
        this.forEach { element ->
            when (element.name) {
                StringTag -> stringList.add(element.text)
                PluralsTag, StringArrayTag -> {
                    element.elements().forEach { item ->
                        stringList.add(item.text)
                    }
                }
            }
        }
        return stringList
    }

    private fun Element.getTranslatableTags(): List<Element> {
        return elements().filter {
            return@filter it.attributeValue("translatable", "").toBooleanStrictOrNull() ?: true
        }
    }

    private fun Element.getNameAttrValue(): String? {
        return this.attributeValue("name")
    }

    private fun String.toBooleanStrictOrNull(): Boolean? = when (this) {
        "true" -> true
        "false" -> false
        else -> null
    }

    const val PluralsTag = "plurals"
    const val StringArrayTag = "string-array"
    const val StringTag = "string"
}

