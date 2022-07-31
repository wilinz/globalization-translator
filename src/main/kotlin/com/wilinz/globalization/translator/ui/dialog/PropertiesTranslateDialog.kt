package com.wilinz.globalization.translator.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposePanel
import androidx.compose.ui.unit.dp
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.wilinz.globalization.translator.i18n.message
import com.wilinz.globalization.translator.language.LanguageUtil
import com.wilinz.globalization.translator.ui.theme.WidgetTheme
import com.wilinz.globalization.translator.ui.util.javaPainterResource
import com.wilinz.globalization.translator.ui.widgets.CheckboxWithLabel
import com.wilinz.globalization.translator.util.PropertiesUtil
import java.nio.charset.Charset
import javax.swing.JComponent

class PropertiesTranslateConfig(
    sourceLanguage: String,
    targetLanguages: List<String>,
    val isEncodeUnicode: Boolean,
    val charset: Charset,
    isOverwriteTargetFile: Boolean
) : TranslationConfig(sourceLanguage, targetLanguages, isOverwriteTargetFile)

class PropertiesTranslateDialog(
    project: Project?,
    title: String,
    defaultSourceLanguage: String = "en",
    isShowOverwriteCheckBox: Boolean,
    private val onOKClick: (config: PropertiesTranslateConfig) -> Unit,
    private val translatedLanguages: List<String> = listOf()
) : TranslateDialog(project, title, defaultSourceLanguage, null, translatedLanguages, isShowOverwriteCheckBox) {

    private var isEncodeUnicode by mutableStateOf(false)
    private var fileFormat by mutableStateOf(Charsets.UTF_8.name())

    init {
        super.setOnOKClickListener { config ->
            onOKClick(
                PropertiesTranslateConfig(
                    sourceLanguage = config.sourceLanguage,
                    targetLanguages = config.targetLanguages,
                    isOverwriteTargetFile = config.isOverwriteTargetFile,
                    isEncodeUnicode = isEncodeUnicode,
                    charset = charset(fileFormat)
                )
            )
        }
    }

    override fun createCenterPanel(): JComponent {
        return setCenterPanel {
            SourceFileFormat(format = fileFormat, onFormatChange = { fileFormat = it })
            EncodeUnicodeCkeckBox(isEncodeUnicode = isEncodeUnicode, onIsEncodeUnicodeChange = { isEncodeUnicode = it })
        }
    }

    @Composable
    private fun EncodeUnicodeCkeckBox(
        isEncodeUnicode: Boolean,
        onIsEncodeUnicodeChange: (Boolean) -> Unit
    ) {
        CheckboxWithLabel(
            checked = isEncodeUnicode,
            onCheckedChange = {
                onIsEncodeUnicodeChange(it)
            },
            label = {
                Text(text = message("is_encode_unicode"))
            }
        )
    }

    @Composable
    private fun SourceFileFormat(
        format: String,
        onFormatChange: (language: String) -> Unit
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            var menuExpanded by remember { mutableStateOf(false) }
            val option = remember { listOf(Charsets.UTF_8.name(), Charsets.ISO_8859_1.name()) }
            Text(message("source_file_format"))
            Box {
                TextButton(onClick = {
                    menuExpanded = !menuExpanded
                }) {
                    Text(format)
                    Icon(
                        painter = javaPainterResource("icons/more.svg"),
                        contentDescription = "more",
                        modifier = Modifier.size(24.dp)
                    )
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    modifier = Modifier.background(MaterialTheme.colors.background)
                ) {
                    option.forEachIndexed { index, charset ->
                        DropdownMenuItem(onClick = {
                            menuExpanded = false
                            onFormatChange(charset)
                        }) {
                            Text(charset)
                        }
                    }
                }
            }

        }
    }

}

fun propertiesTranslateDialog(
    project: Project?,
    title: String,
    file: VirtualFile,
    onOKClick: (config: PropertiesTranslateConfig) -> Unit,
    isShowOverwriteCheckBox: Boolean = false,
): PropertiesTranslateDialog {
    val defaultSourceLanguage =
        file.name.let { PropertiesUtil.getLanguageByName(it) }
            ?: LanguageUtil.getLocalLanguage()
            ?: "en"

    val translatedLanguages = file.parent?.children
        ?.filter { PropertiesUtil.getBaseName(it.name) == PropertiesUtil.getBaseName(file.name) }
        ?.mapNotNull { PropertiesUtil.getLanguageByName(it.name) }
        ?: emptyList()

    return PropertiesTranslateDialog(
        project = project,
        title = title,
        defaultSourceLanguage = defaultSourceLanguage,
        isShowOverwriteCheckBox = isShowOverwriteCheckBox,
        onOKClick = onOKClick,
        translatedLanguages = translatedLanguages,
    )
}