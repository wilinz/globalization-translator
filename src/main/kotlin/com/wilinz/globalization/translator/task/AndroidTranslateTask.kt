package com.wilinz.globalization.translator.task

import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsContexts
import com.intellij.openapi.vfs.VirtualFile
import com.wilinz.globalization.translator.translator.AndroidXmlTranslator
import com.wilinz.globalization.translator.translator.engine.GoogleTranslator
import com.wilinz.globalization.translator.ui.dialog.TranslationConfig
import org.dom4j.Document

class AndroidTranslateTask(
    project: Project?,
    private val filename: String,
    private val resourceDir: VirtualFile,
    private val document: Document,
    private val config: TranslationConfig,
    title: @NlsContexts.ProgressTitle String
) : TranslateTask(project, title) {

    override fun run(indicator: ProgressIndicator) {
        translator = GoogleTranslator(config.isFirstUppercase)
        AndroidXmlTranslator.translateForIntellij(
            translator = translator!!,
            filename = filename,
            resourceDir = resourceDir,
            document = document,
            form = config.sourceLanguage,
            to = config.targetLanguages,
            isOverwriteTargetFile = config.isOverwriteTargetFile,
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