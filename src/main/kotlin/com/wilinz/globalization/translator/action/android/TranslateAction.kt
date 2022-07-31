package com.wilinz.globalization.translator.action.android

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.wilinz.globalization.translator.i18n.message
import com.wilinz.globalization.translator.util.isStringsXmlFile
import org.dom4j.io.SAXReader

class TranslateAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val file = CommonDataKeys.VIRTUAL_FILE.getData(e.dataContext) ?: return
        androidActionPerformed(
            e = e,
            file = file,
            getDocument = {
                file.inputStream.use { SAXReader().read(it) }
            },
            isShowOverwriteCheckBox = true
        )
    }

    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.text = message("translate_this_file")
        val file = CommonDataKeys.VIRTUAL_FILE.getData(e.dataContext)
        val isSelectValueFile = file?.isStringsXmlFile() ?: false
        e.presentation.isEnabledAndVisible = isSelectValueFile
    }

}