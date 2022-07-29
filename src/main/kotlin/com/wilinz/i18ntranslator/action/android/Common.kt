package com.wilinz.i18ntranslator.action.android

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.vfs.VirtualFile
import com.wilinz.i18ntranslator.i18n.message
import com.wilinz.i18ntranslator.task.AndroidTranslateTask
import com.wilinz.i18ntranslator.ui.dialog.androidTranslateDialog
import org.dom4j.Document

internal fun androidActionPerformed(e: AnActionEvent, file: VirtualFile, getDocument: ()->Document) {
    val resourceDir = file.parent.parent
    if (!resourceDir.exists()) return

    androidTranslateDialog(
        project = e.project,
        title = message("translate_this_file"),
        file = file,
        onOKClick = { sourceLanguage, targetLanguages ->
            AndroidTranslateTask(
                project = e.project,
                filename = file.name,
                resourceDir = resourceDir,
                document = getDocument(),
                form = sourceLanguage,
                to = targetLanguages,
                title = message("translate_file", file.name)
            ).queue()
        }
    ).show()
}