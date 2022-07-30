package com.wilinz.globalization.translator.task

import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsContexts
import com.intellij.openapi.vfs.VirtualFile
import com.wilinz.globalization.translator.translator.PropertiesTranslator
import com.wilinz.globalization.translator.translator.engine.GoogleTranslator
import org.codejive.properties.Properties

class PropertiesTranslateTask(
    project: Project?,
    private val baseFilename: String,
    private val isEncodeUnicode: Boolean,
    private val resourceDir: VirtualFile,
    private val properties: Properties,
    private val form: String,
    private val to: List<String>,
    title: @NlsContexts.ProgressTitle String
) : TranslateTask(project, form, to, title) {
    override fun run(indicator: ProgressIndicator) {
        translator = GoogleTranslator()
        PropertiesTranslator.translateForIntellij(
            translator = translator!!,
            baseFilename = baseFilename,
            isEncodeUnicode = isEncodeUnicode,
            resourceDir = resourceDir,
            properties = properties,
            form = form,
            to = to,
            onEachStart = { index, language ->
                onEachStart(indicator, index, language)
            },
            onEachSuccess = { index, language ->
                onEachSuccess(index, language)
            },
            onEachError = { index, language, error ->
                onEachError(index, language, error)
            }
        )
    }
}