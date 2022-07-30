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
    getProperties: (charset: Charset) -> Properties
) {
    val basename = PropertiesUtil.getBaseName(file.name) ?: return
    val resourceDir = file.parent
    if (!resourceDir.exists()) return

    propertiesTranslateDialog(
        project = e.project,
        title = message("translate_this_file"),
        onOKClick = { sourceLanguage, targetLanguages, isEncodeUnicode, charset ->
            PropertiesTranslateTask(
                project = e.project,
                baseFilename = basename,
                isEncodeUnicode = isEncodeUnicode,
                resourceDir = resourceDir,
                properties = getProperties(charset),
                form = sourceLanguage,
                to = targetLanguages,
                title = message("translate_file", file.name)
            ).queue()
        },
        file = file
    ).show()
}