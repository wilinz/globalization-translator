package com.wilinz.androidi18n.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.wilinz.androidi18n.i18n.message
import com.wilinz.androidi18n.ui.TranslateDialog
import com.wilinz.androidi18n.util.isStringsFile
import java.io.StringReader

class TranslateSelectedAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val editor = e.getData(PlatformDataKeys.EDITOR) ?: return
        val selectedText = editor.selectionModel.selectedText
        val xml = "<resources>$selectedText</resources>"
        TranslateDialog(message("translate_to_other_languages"), e, StringReader(xml)).show()
    }

    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.text = message("translate_to_other_languages")
        val file = CommonDataKeys.VIRTUAL_FILE.getData(e.dataContext)
        val isStringsFile = file?.isStringsFile() ?: false
        val editor = e.getData(PlatformDataKeys.EDITOR) ?: return
        val selectedText = editor.selectionModel.selectedText ?: ""
        e.presentation.isEnabledAndVisible =
            isStringsFile && selectedText.trim().matches(Regex("^<.+[\\s\\S]*</.+>$"))
    }
}