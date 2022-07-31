package com.wilinz.globalization.translator.ui.dialog

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.wilinz.globalization.translator.language.LanguageUtil

fun androidTranslateDialog(
    project: Project?,
    title: String,
    file: VirtualFile,
    onOKClick: (config: TranslationConfig) -> Unit,
    isShowOverwriteCheckBox: Boolean = false
): TranslateDialog {

    val defaultSourceLanguage =
        file.parent?.name?.let { LanguageUtil.androidLanguageDirMap.inverse()[it] }
            ?: LanguageUtil.getLocalLanguage()
            ?: "en"

    val resourceDir = file.parent?.parent
    val translatedLanguages =
        resourceDir?.children?.mapNotNull { LanguageUtil.androidLanguageDirMap.inverse()[it.name] } ?: emptyList()

    return TranslateDialog(
        project,
        title,
        defaultSourceLanguage,
        onOKClick,
        translatedLanguages,
        isShowOverwriteCheckBox
    )
}