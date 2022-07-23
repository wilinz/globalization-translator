package com.wilinz.androidi18n.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.wilinz.androidi18n.i18n.message
import com.wilinz.androidi18n.ui.TranslateDialog
import com.wilinz.androidi18n.util.isStringsFile

class TranslateAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val file = CommonDataKeys.VIRTUAL_FILE.getData(e.dataContext)
        val reader = file?.inputStream?.reader() ?: return
        TranslateDialog(message("translate_this_file"), e, reader).show()
    }

    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.text = message("translate_this_file")
        //翻译选项仅在选择 strings.xmlplurals.xmlarrays.xml 时显示
        val file = CommonDataKeys.VIRTUAL_FILE.getData(e.dataContext)
        val isSelectValueFile = file?.isStringsFile() ?: false
        e.presentation.isEnabledAndVisible = isSelectValueFile
    }

}