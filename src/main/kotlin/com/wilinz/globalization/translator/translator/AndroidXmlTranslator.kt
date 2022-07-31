package com.wilinz.globalization.translator.translator

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.vfs.VirtualFile
import com.wilinz.globalization.translator.language.LanguageUtil
import com.wilinz.globalization.translator.translator.engine.Translator
import com.wilinz.globalization.translator.util.AndroidUtil
import org.dom4j.Document
import org.dom4j.DocumentHelper
import org.dom4j.Element
import org.dom4j.io.OutputFormat
import org.dom4j.io.SAXReader
import org.dom4j.io.XMLWriter

object AndroidXmlTranslator {

    fun translateForIntellij(
        translator: Translator,
        filename: String,
        resourceDir: VirtualFile,
        document: Document,
        form: String,
        to: List<String>,
        isOverwriteTargetFile: Boolean,
        onEachStart: ((index: Int, language: String) -> Unit)? = null,
        onEachSuccess: ((index: Int, language: String) -> Unit)? = null,
        onEachError: ((index: Int, language: String, error: Throwable) -> Unit)? = null,
    ) {
        val saxReader = SAXReader()
        translate(
            translator = translator,
            oldDocument = document,
            newDocumentFetcher = { index, language ->
                if (isOverwriteTargetFile) return@translate null
                val stringsFile =
                    resourceDir.findChild(LanguageUtil.androidLanguageDirMap[language]!!)?.findChild(filename)
                        ?: return@translate null
                if (stringsFile.exists()) {
                    stringsFile.inputStream.use { input ->
                        try {
                            saxReader.read(input)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            null
                        }
                    }
                } else null
            },
            form = form,
            to = to,
            onEachSuccess = { index, newDocument, language ->
                val dirname = LanguageUtil.androidLanguageDirMap[language]!!
                val applicationManager = ApplicationManager.getApplication()
                applicationManager.invokeLater {
                    applicationManager.runWriteAction {
                        val languageDir =
                            resourceDir.findChild(dirname) ?: resourceDir.createChildDirectory(null, dirname)
                        val stringsFile = languageDir.findChild(filename) ?: languageDir.createChildData(null, filename)
                        stringsFile.getOutputStream(null).use { out ->
                            val xmlWriter = XMLWriter(out, OutputFormat.createPrettyPrint()).apply {
                                isEscapeText = false
                            }
                            xmlWriter.write(newDocument.addCommentToTop(Commentary))
                        }
                    }
                }
                onEachSuccess?.invoke(index, language)
            },
            onEachStart = onEachStart,
            onEachError = onEachError
        )
    }

    fun translate(
        translator: Translator,
        oldDocument: Document,
        newDocumentFetcher: ((index: Int, String) -> Document?)? = null,
        form: String,
        to: List<String>,
        onEachStart: ((index: Int, language: String) -> Unit)? = null,
        onEachSuccess: ((index: Int, Document, language: String) -> Unit)? = null,
        onEachError: ((index: Int, language: String, error: Throwable) -> Unit)? = null,
        onEachFinish: (() -> Unit)? = null
    ) {
        val translatableNodeList = oldDocument.rootElement.createCopy().getTranslatableNodes()
        for (languageIndex in to.indices) {
            val toLanguage = to[languageIndex]
            onEachStart?.invoke(languageIndex, toLanguage)
            try {
                val translatableList =
                    translatableNodeList.mapToStringList().map {
                        with(AndroidUtil) {
                            it.escapeAndroidXml().addCodeTag()
                        }
                    }
                val translateResult = translator.translate(
                    translatableList, form, toLanguage
                ).map { with(AndroidUtil) { it.removeCodeTag().unescapeAndroidXml() } }

                if (translateResult.size != translatableList.size) {
                    onEachError?.invoke(languageIndex, toLanguage, Exception("The translation result is abnormal"))
                    continue
                }

                val newDocument = newDocumentFetcher?.invoke(languageIndex, toLanguage)
                    ?: DocumentHelper.createDocument().apply { addElement("resources") }
                val newRoot = newDocument.rootElement
                val newElementsMap = newRoot.elements().toNameMap()
                var i = 0
                translatableNodeList.forEach { translatableElement ->
                    val newNode = newElementsMap[translatableElement.getNameAttrValue()]
                        ?: translatableElement.createCopy().also { newRoot.add(it) }
                    when (newNode.name) {
                        STRING_TAG -> {
                            newNode.text = translateResult[i++]
                        }
                        PLURALS_TAG, STRING_ARRAY_TAG -> {
                            newNode.elements().forEach { item ->
                                item.text = translateResult[i++]
                            }
                        }
                    }
                }
                onEachSuccess?.invoke(languageIndex, newDocument, toLanguage)
            } catch (e: Exception) {
                e.printStackTrace()
                onEachError?.invoke(languageIndex, toLanguage, e)
            } finally {
                onEachFinish?.invoke()
            }
        }
    }

    private fun Document.addCommentToTop(vararg comment: String): Document {
        return DocumentHelper.createDocument().apply {
            comment.forEach { addComment(it) }
            add(this@addCommentToTop.rootElement.createCopy())
        }
    }

    private fun List<Element>.toNameMap(): Map<String, Element> {
        val map = mutableMapOf<String, Element>()
        this.forEach {
            map[it.getNameAttrValue()] = it
        }
        return map
    }

    private fun List<Element>.mapToStringList(): List<String> {
        val stringList = mutableListOf<String>()
        this.forEach { element ->
            when (element.name) {
                STRING_TAG -> stringList.add(element.text)
                PLURALS_TAG, STRING_ARRAY_TAG -> {
                    element.elements().forEach { item ->
                        stringList.add(item.text)
                    }
                }
            }
        }
        return stringList
    }

    private fun Element.getTranslatableNodes(): List<Element> {
        return elements().filter {
            return@filter it.attributeValue("translatable", "").toBooleanStrictOrNull() ?: true
        }
    }

    private fun Element.getNameAttrValue(): String {
        return this.attributeValue("name")
    }

    private fun String.toBooleanStrictOrNull(): Boolean? = when (this) {
        "true" -> true
        "false" -> false
        else -> null
    }

    private const val PLURALS_TAG = "plurals"
    private const val STRING_ARRAY_TAG = "string-array"
    private const val STRING_TAG = "string"
}

