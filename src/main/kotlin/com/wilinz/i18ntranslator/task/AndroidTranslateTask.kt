package com.wilinz.i18ntranslator.task

import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsContexts
import com.intellij.openapi.vfs.VirtualFile
import com.wilinz.i18ntranslator.translator.AndroidXmlTranslator
import com.wilinz.i18ntranslator.translator.engine.GoogleTranslator
import org.dom4j.Document

class AndroidTranslateTask(
    project: Project?,
    private val filename: String,
    private val resourceDir: VirtualFile,
    private val document: Document,
    private val form: String,
    private val to: List<String>,
    title: @NlsContexts.ProgressTitle String
) : TranslateTask(project, form, to, title) {

    override fun run(indicator: ProgressIndicator) {
        translator = GoogleTranslator()
        AndroidXmlTranslator.translateForIntellij(
            translator = translator!!,
            filename = filename,
            resourceDir = resourceDir,
            document = document,
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