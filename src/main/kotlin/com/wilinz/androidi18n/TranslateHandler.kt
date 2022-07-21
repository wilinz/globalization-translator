package com.wilinz.androidi18n

import com.intellij.openapi.vfs.VirtualFile
import com.wilinz.androidi18n.translator.GoogleTranslate
import com.wilinz.androidi18n.translator.Translator
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
    private val onTranslating: ((index: Int, language: Language) -> Unit)? = null,
    private val onSuccess: ((index: Int, language: Language) -> Unit)? = null,
    private val onError: ((index: Int, language: Language, error: Throwable) -> Unit)? = null,
) {

    private val translator = GoogleTranslate()
    private var isCancel = false

    fun start() {
        isCancel = false
        val saxReader = SAXReader()

        val result = translateXml(
            translator,
            oldDocument = saxReader.read(documentReader),
            newDocument = { index, language ->
                val languageDir = File(resourceDir.path, language.directoryName)
                val stringsFile = File(languageDir, "strings.xml")
                if (stringsFile.exists()) saxReader.read(stringsFile.inputStream()) else null
            },
            form = form,
            to = to,
            isCancel = isCancel,
            onSuccess = { index, document, language ->
                val languageDir = File(resourceDir.path, language.directoryName)
                languageDir.mkdirs()
                val stringsFile = File(languageDir, "strings.xml")
                if (!stringsFile.exists()) stringsFile.createNewFile()
                val xmlWriter = XMLWriter(stringsFile.outputStream(), OutputFormat.createPrettyPrint()).apply {
                    isEscapeText = false
                }
                xmlWriter.write(document)
                onSuccess?.invoke(index, language)
            },
            onTranslating = onTranslating,
            onError = onError
        )
        println(result)
    }


    fun cancel() {
        isCancel = true
        translator.cancel()
    }

}

fun translateXml(
    translator: Translator,
    oldDocument: Document,
    newDocument: ((index: Int, Language) -> Document?)? = null,
    form: Language,
    to: List<Language>,
    isCancel: Boolean=false,
    onTranslating: ((index: Int, language: Language) -> Unit)? = null,
    onSuccess: ((index: Int, Document, language: Language) -> Unit)? = null,
    onError: ((index: Int, language: Language, error: Throwable) -> Unit)? = null,
    onFinish: (() -> Unit)? = null
) {
    val list = oldDocument.filterToTranslatableList()
    for (index in to.indices) {
        if (isCancel) break
        val toLanguage = to[index]
        onTranslating?.invoke(index, toLanguage)
        try {
            val translateResult = translator.translate(
                list.map { it.text },
                form.code,
                toLanguage.code
            )
            val newDocument1 = newDocument?.invoke(index, toLanguage) ?: DocumentHelper.createDocument()
                .apply { addElement("resources") }
            val root = newDocument1.rootElement
            translateResult.forEachIndexed { index, translateResult ->
                val translateElement = list[index]
                if (newDocument1 != null) {
                    val oldNode = root.elements().firstOrNull {
                        (it as Element).getNameAttr() == translateElement.getNameAttr()
                    } ?: translateElement.createCopy().also { root.add(it) }
                    oldNode.text = translateResult
                } else {
                    root.add(translateElement.createCopy().apply { text = translateResult })
                }
            }
            onSuccess?.invoke(index, newDocument1, toLanguage)
        } catch (e: Exception) {
            onError?.invoke(index, toLanguage, e)
        } finally {
            onFinish?.invoke()
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

fun Element.getNameAttr(): String? {
    return this.attributeValue("name")
}

public fun String.toBooleanStrictOrNull(): Boolean? = when (this) {
    "true" -> true
    "false" -> false
    else -> null
}