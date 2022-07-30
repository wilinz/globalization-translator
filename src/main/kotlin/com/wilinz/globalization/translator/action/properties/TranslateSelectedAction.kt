package com.wilinz.globalization.translator.action.properties

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.wilinz.globalization.translator.i18n.message
import org.codejive.properties.Properties
import java.io.StringReader

class TranslateSelectedAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val file = CommonDataKeys.VIRTUAL_FILE.getData(e.dataContext) ?: return
        val editor = e.getData(PlatformDataKeys.EDITOR) ?: return
        val selectedText = editor.selectionModel.selectedText ?: return

        propertiesActionPerformed(
            e = e,
            file = file,
            getProperties = {
                StringReader(selectedText).use { Properties.loadProperties(it) }
            },
        )

    }

    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.text = message("translate_to_other_languages")
        val file = CommonDataKeys.VIRTUAL_FILE.getData(e.dataContext) ?: return
        val isSelectValueFile = file.extension == "properties"
        val editor = e.getData(PlatformDataKeys.EDITOR) ?: return
        val selectedText = editor.selectionModel.selectedText ?: ""
        e.presentation.isEnabledAndVisible = isSelectValueFile && selectedText.trim().isNotEmpty()
    }

}