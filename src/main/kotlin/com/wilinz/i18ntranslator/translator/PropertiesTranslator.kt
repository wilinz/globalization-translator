package com.wilinz.i18ntranslator.translator

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.vfs.VirtualFile
import com.wilinz.i18ntranslator.translator.engine.Translator
import com.wilinz.i18ntranslator.util.PropertiesUtil
import com.wilinz.i18ntranslator.util.escapeHTML
import com.wilinz.i18ntranslator.util.unescapeHTML
import org.codejive.properties.Properties

object PropertiesTranslator {

    fun translateForIntellij(
        translator: Translator,
        baseFilename: String,
        isEncodeUnicode: Boolean,
        resourceDir: VirtualFile,
        properties: Properties,
        form: String,
        to: List<String>,
        onEachStart: ((index: Int, language: String) -> Unit)? = null,
        onEachSuccess: ((index: Int, language: String) -> Unit)? = null,
        onEachError: ((index: Int, language: String, error: Throwable) -> Unit)? = null,
    ) {
        translate(
            translator = translator,
            oldProperties = properties,
            newPropertiesFetcher = { _, language ->
                val childName = PropertiesUtil.getFilenameByBaseName(baseFilename, language)
                return@translate resourceDir.findChild(childName)?.let {
                    it.inputStream.reader().use { input ->
                        Properties.loadProperties(input)
                    }
                }
            },
            form = form,
            to = to,
            onEachStart = onEachStart,
            onEachSuccess = { index, language, properties ->
                val filename = PropertiesUtil.getFilenameByBaseName(baseFilename, language)
                val applicationManager = ApplicationManager.getApplication()
                applicationManager.invokeLater {
                    applicationManager.runWriteAction {
                        val file = resourceDir.findChild(filename) ?: resourceDir.createChildData(null, filename)
                        file.getOutputStream(null).use { out ->
                            properties.store(out, isEncodeUnicode)
                        }
                    }
                }
                onEachSuccess?.invoke(index, language)
            },
            onEachError = onEachError
        )
    }

    fun translate(
        translator: Translator,
        oldProperties: Properties,
        newPropertiesFetcher: ((index: Int, language: String) -> Properties?)? = null,
        form: String,
        to: List<String>,
        onEachStart: ((index: Int, language: String) -> Unit)? = null,
        onEachSuccess: ((index: Int, language: String, Properties) -> Unit)? = null,
        onEachError: ((index: Int, language: String, error: Throwable) -> Unit)? = null,
        onEachFinish: (() -> Unit)? = null
    ) {
        for (languageIndex in to.indices) {
            val toLanguage = to[languageIndex]
            onEachStart?.invoke(languageIndex, toLanguage)
            try {
                val keys = oldProperties.keys.toList()
                val translatableList = oldProperties.map { with(PropertiesUtil) { it.value.escapeHTML().addCodeTag() } }
                val translateResult = translator.translate(
                    translatableList, form, toLanguage
                ).map { with(PropertiesUtil) { it.unescapeHTML().removeCodeTag() } }

                if (translateResult.size != translatableList.size) {
                    onEachError?.invoke(languageIndex, toLanguage, Exception("The translation result is abnormal"))
                    continue
                }

                val newProperties = newPropertiesFetcher?.invoke(languageIndex, toLanguage) ?: Properties()
                translateResult.forEachIndexed { index, value ->
                    newProperties.setProperty(keys[index], value)
                }
                onEachSuccess?.invoke(languageIndex, toLanguage, newProperties)
            } catch (e: Exception) {
                e.printStackTrace()
                onEachError?.invoke(languageIndex, toLanguage, e)
            } finally {
                onEachFinish?.invoke()
            }
        }

    }

}