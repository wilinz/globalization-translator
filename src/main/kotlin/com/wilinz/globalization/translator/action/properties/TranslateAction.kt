package com.wilinz.globalization.translator.action.properties

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.wilinz.globalization.translator.i18n.message
import org.codejive.properties.Properties

class TranslateAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val file = CommonDataKeys.VIRTUAL_FILE.getData(e.dataContext) ?: return
        propertiesActionPerformed(
            e = e,
            file = file,
            getProperties = { charset ->
                file.inputStream.use { Properties.loadProperties(it.reader(charset)) }
            },
        )
    }

    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.text = message("translate_this_file")
        val file = CommonDataKeys.VIRTUAL_FILE.getData(e.dataContext) ?: return
        val isSelectValueFile = file.extension == "properties"
        e.presentation.isEnabledAndVisible = isSelectValueFile
    }

}