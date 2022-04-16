package com.wilinz.androidi18n

import com.intellij.openapi.vfs.VirtualFile
import com.wilinz.androidi18n.translator.GoogleTranslate
import com.wilinz.androidi18n.util.Language
import org.dom4j.Document
import org.dom4j.DocumentHelper
import org.dom4j.Element
import org.dom4j.io.OutputFormat
import org.dom4j.io.SAXReader
import org.dom4j.io.XMLWriter
import java.io.File
import java.io.Reader


class TranslateHandler(
    private val resourceDir: VirtualFile,
    private val documentReader: Reader,
    private val form: Language,
    private val to: List<Language>,
    onTranslating: (index: Int, language: Language) -> Unit,
    onSuccess: (index: Int, language: Language) -> Unit,
    onError: (index: Int, language: Language, error: Exception) -> Unit,
) {

    init {
        val reader = SAXReader()
        val document = reader.read(documentReader)
        val list = document.filterToTranslatableList()
        to.forEachIndexed { index, toLanguage ->
            try {
                onTranslating(index, toLanguage)
                val translateResult = GoogleTranslate().translateMultipleText(
                    list.map { it.text },
                    form.code,
                    toLanguage.code
                )

                val languageDir = File(resourceDir.path, toLanguage.directoryName)
                languageDir.mkdirs()

                val stringsFile = File(languageDir, "strings.xml")

                val newDocument = if (stringsFile.exists()) reader.read(stringsFile.inputStream())
                else DocumentHelper.createDocument().apply { addElement("resources") }

                val root = newDocument.rootElement

                println(document.asXML())
                println(newDocument.asXML())
                translateResult.forEachIndexed { index, translateResult ->
                    val translateElement = list[index]
                    if (stringsFile.exists()) {
//                        val oldNode = newDocument.selectSingleNode("/resources/string[@name='${name}']")
                        val oldNode = root.elements().firstOrNull {
                            (it as Element).getNameAttr() == translateElement.getNameAttr()
                        } as Element?

                        if (oldNode == null) {
                            root.add(translateElement.createCopy().apply { text = translateResult.result })
                        } else {
                            oldNode.text = translateResult.result
                        }
                    } else {
                        root.add(translateElement.createCopy().apply { text = translateResult.result })
                    }
                }

                val writer = XMLWriter(stringsFile.outputStream(), OutputFormat.createPrettyPrint())
                writer.write(newDocument)
                onSuccess(index, toLanguage)
            } catch (e: Exception) {
                onError(index, toLanguage, e)
                e.printStackTrace()
            }
        }

    }

    private fun Document.filterToTranslatableList(): List<Element> {
        return document.rootElement.elements().map {
            (it as Element).createCopy()
        }.filter {
            return@filter it.attributeValue("translatable", "").toBooleanStrictOrNull() ?: true
        }
    }

}

fun Element.getNameAttr(): String? {
    return this.attributeValue("name")
}

public fun String.toBooleanStrictOrNull(): Boolean? = when (this) {
    "true" -> true
    "false" -> false
    else -> null
}