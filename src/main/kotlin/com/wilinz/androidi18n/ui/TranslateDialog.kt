package com.wilinz.androidi18n.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposePanel
import androidx.compose.ui.unit.dp
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.vfs.VirtualFile
import com.wilinz.androidi18n.TranslateTask
import com.wilinz.androidi18n.myPainterResource
import com.wilinz.androidi18n.ui.theme.WidgetTheme
import com.wilinz.androidi18n.ui.widgets.CheckboxWithLabel
import com.wilinz.androidi18n.util.Language
import com.wilinz.androidi18n.util.LanguageUtil
import java.io.FileNotFoundException
import java.io.Reader
import java.util.*
import javax.swing.JComponent

data class LanguageItem(
    val language: Language,
    var checked: Boolean
)

class TranslateDialog(private val actionEvent: AnActionEvent, private val documentReader: Reader) :
    DialogWrapper(actionEvent.project) {

    private val existingDirList: List<String>
    private val sourceLanguage: MutableState<Language>

    private val resourceDir: VirtualFile
    private val language = mutableStateListOf<LanguageItem>().apply {
        addAll(
            LanguageUtil.languages.map {
                LanguageItem(
                    it,
                    false
                )
            })
    }


    init {
        val regex = Regex("^values.*$")
        val file = CommonDataKeys.VIRTUAL_FILE.getData(actionEvent.dataContext)

        val language = Locale.getDefault().language
        val region = Locale.getDefault().country
        val localLanguageCode = "$language-$region"
        println(localLanguageCode)
        sourceLanguage = mutableStateOf(
            LanguageUtil.languages.firstOrNull {
                it.directoryName == file?.parent?.name
            } ?: LanguageUtil.languages.firstOrNull {
                localLanguageCode.contains(it.code)
            }
            ?: LanguageUtil.languages.first { it.code == "en" }
        )

        resourceDir = file?.parent?.parent ?: throw FileNotFoundException("resource dir is not found")

        existingDirList = resourceDir.children
            ?.map {
                it.name
            }?.filter {
                it.matches(regex)
            } ?: emptyList()

        title = "翻译成其他语言"
        init()
        setOKButtonText("确定")
        setCancelButtonText("取消")
    }

    override fun doOKAction() {
        super.doOKAction()
        TranslateTask(
            actionEvent.project,
            resourceDir,
            documentReader,
            sourceLanguage.value,
            language.filter { it.checked }.map { it.language },
            "正在翻译strings.xml"
        ).queue()
    }

    @OptIn(ExperimentalFoundationApi::class)
    override fun createCenterPanel(): JComponent {
        return ComposePanel().apply {
            setBounds(0, 0, 1000, 600)
            setContent {
                WidgetTheme(darkTheme = true) {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        Box(modifier = Modifier.padding(8.dp)) {
                            DialogContent()
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
    @Composable
    private fun DialogContent() {

        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val menuExpanded = remember { mutableStateOf(false) }

                Text("源语言：")
                Box {
                    TextButton(onClick = {
                        menuExpanded.value = !menuExpanded.value
                    }) {
                        Text(sourceLanguage.value.name)
                        Icon(
                            painter = myPainterResource("icons/more.svg"),
                            contentDescription = "more",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = menuExpanded.value,
                        onDismissRequest = { menuExpanded.value = false },
                        modifier = Modifier.background(MaterialTheme.colors.background)
                    ) {
                        LanguageUtil.languagesWithAuto
                            .forEachIndexed { index, language ->
                                DropdownMenuItem(onClick = {
                                    menuExpanded.value = false
                                    sourceLanguage.value = language
                                }) {
                                    Text(language.name)
                                }
                            }
                    }
                }

            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                val selectMode = remember { mutableStateOf(0) }
                Text("目标语言：")
                CheckboxWithLabel(
                    checked = selectMode.value == 1,
                    onCheckedChange = {
                        language.replaceAll { language -> language.copy(checked = it) }
                        selectMode.value = if (it) 1 else 0
                    },
                    label = {
                        Text(text = "全选")
                    }
                )
                CheckboxWithLabel(
                    checked = selectMode.value == 2,
                    onCheckedChange = {
                        if (it) {
                            language.replaceAll {
                                val exist = existingDirList.any { existingDir ->
                                    it.language.directoryName == existingDir
                                }
                                it.copy(checked = !exist)
                            }
                        } else {
                            language.replaceAll { language -> language.copy(checked = it) }
                        }
                        selectMode.value = if (it) 2 else 0
                    },
                    label = {
                        Text(text = "选择未翻译的语言")
                    }
                )
            }

            LazyVerticalGrid(GridCells.Adaptive(150.dp)) {
                itemsIndexed(language) { index, item ->
                    CheckboxWithLabel(
                        checked = item.checked,
                        onCheckedChange = { language[index] = item.copy(checked = it) },
                        label = {
                            Text(text = item.language.name)
                        }
                    )
                }
            }

        }
    }

}

