package com.wilinz.i18ntranslator.action.android

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.wilinz.i18ntranslator.i18n.message
import com.wilinz.i18ntranslator.util.isStringsXmlFile
import org.dom4j.io.SAXReader

class TranslateSelectedAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val editor = e.getData(PlatformDataKeys.EDITOR) ?: return
        val selectedText = editor.selectionModel.selectedText
        val xml = "<resources>$selectedText</resources>"
        val file = CommonDataKeys.VIRTUAL_FILE.getData(e.dataContext) ?: return
        androidActionPerformed(
            e = e,
            file = file,
            getDocument = {
                SAXReader().read(xml)
            },
        )
    }

    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.text = message("translate_to_other_languages")
        val file = CommonDataKeys.VIRTUAL_FILE.getData(e.dataContext)
        val isStringsFile = file?.isStringsXmlFile() ?: false
        val editor = e.getData(PlatformDataKeys.EDITOR) ?: return
        val selectedText = editor.selectionModel.selectedText ?: ""
        e.presentation.isEnabledAndVisible =
            isStringsFile && selectedText.trim().matches(Regex("^<.+[\\s\\S]*</.+>$"))
    }
}