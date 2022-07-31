package com.wilinz.globalization.translator.task

import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsContexts
import com.intellij.openapi.vfs.VirtualFile
import com.wilinz.globalization.translator.translator.AndroidXmlTranslator
import com.wilinz.globalization.translator.translator.engine.GoogleTranslator
import org.dom4j.Document

class AndroidTranslateTask(
    project: Project?,
    private val filename: String,
    private val resourceDir: VirtualFile,
    private val document: Document,
    private val form: String,
    private val to: List<String>,
    private val isOverwriteTargetFile: Boolean,
    title: @NlsContexts.ProgressTitle String
) : TranslateTask(project, title) {

    override fun run(indicator: ProgressIndicator) {
        translator = GoogleTranslator()
        AndroidXmlTranslator.translateForIntellij(
            translator = translator!!,
            filename = filename,
            resourceDir = resourceDir,
            document = document,
            form = form,
            to = to,
            isOverwriteTargetFile = isOverwriteTargetFile,
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
        onComplete()
    }

}