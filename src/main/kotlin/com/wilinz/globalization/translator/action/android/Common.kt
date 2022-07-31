package com.wilinz.globalization.translator.action.android

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.vfs.VirtualFile
import com.wilinz.globalization.translator.i18n.message
import com.wilinz.globalization.translator.task.AndroidTranslateTask
import com.wilinz.globalization.translator.ui.dialog.androidTranslateDialog
import org.dom4j.Document

internal fun androidActionPerformed(
    e: AnActionEvent,
    file: VirtualFile,
    getDocument: () -> Document,
    isShowOverwriteCheckBox: Boolean = false
) {
    val resourceDir = file.parent.parent
    if (!resourceDir.exists()) return

    androidTranslateDialog(
        project = e.project,
        title = message("translate_this_file"),
        file = file,
        onOKClick = { config ->
            AndroidTranslateTask(
                project = e.project,
                filename = file.name,
                resourceDir = resourceDir,
                document = getDocument(),
                form = config.sourceLanguage,
                to = config.targetLanguages,
                isOverwriteTargetFile = config.isOverwriteTargetFile,
                title = message("translate_file", file.name)
            ).queue()
        },
        isShowOverwriteCheckBox = isShowOverwriteCheckBox
    ).show()
}