package com.wilinz.i18ntranslator.ui.dialog

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
import com.wilinz.i18ntranslator.i18n.message
import com.wilinz.i18ntranslator.ui.theme.WidgetTheme
import com.wilinz.i18ntranslator.ui.util.javaPainterResource
import com.wilinz.i18ntranslator.ui.widgets.CheckboxWithLabel
import com.wilinz.i18ntranslator.language.LanguageUtil
import com.wilinz.i18ntranslator.util.PropertiesUtil
import java.nio.charset.Charset
import javax.swing.JComponent

class PropertiesTranslateDialog(
    project: Project?,
    title: String,
    defaultSourceLanguage: String = "en",
    private val onOKClick: (sourceLanguage: String, targetLanguages: List<String>, isEncodeUnicode: Boolean, sourceFileFormat: Charset) -> Unit,
    private val translatedLanguages: List<String> = listOf()
) : TranslateDialog(project, title, defaultSourceLanguage, null, translatedLanguages) {

    private var isEncodeUnicode by mutableStateOf(false)
    private var fileFormat by mutableStateOf(Charsets.UTF_8.name())

    init {
        super.setOnOKClickListener { sourceLanguage, targetLanguages ->
            onOKClick(sourceLanguage, targetLanguages, isEncodeUnicode, charset(fileFormat))
        }
    }

    override fun createCenterPanel(): JComponent {
        return ComposePanel().apply {
            setBounds(0, 0, 1000, 600)
            setContent {
                WidgetTheme(darkTheme = true) {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        Box(modifier = Modifier.padding(8.dp)) {
                            PropertiesDialogContent(
                                fileFormat = fileFormat,
                                onFileFormatChange = { fileFormat = it },
                                isEncodeUnicode = isEncodeUnicode,
                                onIsEncodeUnicodeChange = { isEncodeUnicode = it },
                                sourceLanguage = sourceLanguage,
                                isTranslated = { language ->
                                    translatedLanguages.indexOf(language) != -1
                                },
                                onSourceLanguageChange = {
                                    sourceLanguage = it
                                },
                                targetLanguages = targetLanguages
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun PropertiesDialogContent(
        fileFormat: String,
        onFileFormatChange: (language: String) -> Unit,
        isEncodeUnicode: Boolean,
        onIsEncodeUnicodeChange: (Boolean) -> Unit,
        sourceLanguage: String,
        onSourceLanguageChange: (language: String) -> Unit,
        targetLanguages: SnapshotStateList<LanguageItem>,
        isTranslated: (language: String) -> Boolean
    ) {
        Column {
            SourceFileFormat(fileFormat, onFileFormatChange)
            SourceLanguage(sourceLanguage, onSourceLanguageChange)
            Row {
                TargetLanguage(targetLanguages, isTranslated)
                EncodeUnicodeCkeckBox(isEncodeUnicode, onIsEncodeUnicodeChange)
            }
            LanguageList(targetLanguages)
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
    onOKClick: (sourceLanguage: String, targetLanguages: List<String>, isEncodeUnicode: Boolean, sourceFileFormat: Charset) -> Unit,
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
        project,
        title,
        defaultSourceLanguage,
        onOKClick,
        translatedLanguages
    )
}