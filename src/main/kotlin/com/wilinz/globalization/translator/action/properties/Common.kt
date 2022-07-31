package com.wilinz.globalization.translator.action.properties

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.vfs.VirtualFile
import com.wilinz.globalization.translator.i18n.message
import com.wilinz.globalization.translator.task.PropertiesTranslateTask
import com.wilinz.globalization.translator.ui.dialog.propertiesTranslateDialog
import com.wilinz.globalization.translator.util.PropertiesUtil
import org.codejive.properties.Properties
import java.nio.charset.Charset

internal fun propertiesActionPerformed(
    e: AnActionEvent,
    file: VirtualFile,
    getProperties: (charset: Charset) -> Properties,
    isShowOverwriteCheckBox: Boolean = false
) {
    val basename = PropertiesUtil.getBaseName(file.name) ?: return
    val resourceDir = file.parent
    if (!resourceDir.exists()) return

    propertiesTranslateDialog(
        project = e.project,
        title = message("translate_this_file"),
        onOKClick = { config ->
            PropertiesTranslateTask(
                project = e.project,
                baseFilename = basename,
                isEncodeUnicode = config.isEncodeUnicode,
                resourceDir = resourceDir,
                properties = getProperties(config.charset),
                form = config.sourceLanguage,
                to = config.targetLanguages,
                isOverwriteTargetFile = config.isOverwriteTargetFile,
                title = message("translate_file", file.name)
            ).queue()
        },
        file = file,
        isShowOverwriteCheckBox = isShowOverwriteCheckBox
    ).show()
}