package com.wilinz.globalization.translator.ui.dialog

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposePanel
import androidx.compose.ui.unit.dp
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.wilinz.globalization.translator.i18n.languageMessage
import com.wilinz.globalization.translator.i18n.message
import com.wilinz.globalization.translator.ui.theme.WidgetTheme
import com.wilinz.globalization.translator.ui.util.javaPainterResource
import com.wilinz.globalization.translator.ui.widgets.CheckboxWithLabel
import com.wilinz.globalization.translator.language.LanguageUtil
import javax.swing.JComponent

data class LanguageItem(
    val language: String,
    var checked: Boolean
)

open class TranslateDialog(
    project: Project?,
    title: String,
    defaultSourceLanguage: String = "en",
    private var onOKClick: ((sourceLanguage: String, targetLanguages: List<String>) -> Unit)? = null,
    private val translatedLanguages: List<String> = listOf(),
) :
    DialogWrapper(project) {

    protected var sourceLanguage by mutableStateOf(defaultSourceLanguage)
    protected val targetLanguages = mutableStateListOf<LanguageItem>().apply {
        addAll(
            LanguageUtil.languages.map {
                LanguageItem(
                    it,
                    false
                )
            })
    }

    fun setOnOKClickListener(onOKClick: ((sourceLanguage: String, targetLanguages: List<String>) -> Unit)) {
        this.onOKClick = onOKClick
    }

    init {
        super.init()
        this.title = title
        setOKButtonText(message("ok"))
        setCancelButtonText(message("cancel"))
    }

    override fun doOKAction() {
        super.doOKAction()
        onOKClick?.invoke(sourceLanguage, targetLanguages.filter { it.checked }.map { it.language })
    }

    override fun createCenterPanel(): JComponent {
        return ComposePanel().apply {
            setBounds(0, 0, 1000, 600)
            setContent {
                WidgetTheme(darkTheme = true) {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        Box(modifier = Modifier.padding(8.dp)) {
                            DialogContent(
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
    protected fun DialogContent(
        sourceLanguage: String,
        onSourceLanguageChange: (language: String) -> Unit,
        targetLanguages: SnapshotStateList<LanguageItem>,
        isTranslated: (language: String) -> Boolean
    ) {
        Column {
            SourceLanguage(sourceLanguage, onSourceLanguageChange)
            TargetLanguage(targetLanguages, isTranslated)
            LanguageList(targetLanguages)
        }
    }

    @Composable
    protected fun TargetLanguage(
        targetLanguages: SnapshotStateList<LanguageItem>,
        isTranslated: (language: String) -> Boolean
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            var selectMode by remember { mutableStateOf(0) }
            Text(message("target_language"))
            CheckboxWithLabel(
                checked = selectMode == 1,
                onCheckedChange = {
                    targetLanguages.replaceAll { language -> language.copy(checked = it) }
                    selectMode = if (it) 1 else 0
                },
                label = {
                    Text(text = message("select_all"))
                }
            )
            CheckboxWithLabel(
                checked = selectMode == 2,
                onCheckedChange = {
                    if (it) {
                        targetLanguages.replaceAll { item ->
                            item.copy(checked = !isTranslated(item.language))
                        }
                    } else {
                        targetLanguages.replaceAll { language -> language.copy(checked = it) }
                    }
                    selectMode = if (it) 2 else 0
                },
                label = {
                    Text(text = message("select_untranslated_language"))
                }
            )
        }
    }

    @Composable
    @OptIn(ExperimentalFoundationApi::class)
    protected fun LanguageList(targetLanguages: SnapshotStateList<LanguageItem>) {
        Box(modifier = Modifier.fillMaxSize()
            .padding(10.dp)) {
            val state = rememberLazyListState()
            LazyVerticalGrid(GridCells.Adaptive(150.dp), state = state) {
                itemsIndexed(targetLanguages) { index, item ->
                    CheckboxWithLabel(
                        checked = item.checked,
                        onCheckedChange = { targetLanguages[index] = item.copy(checked = it) },
                        label = {
                            Text(text = languageMessage(item.language))
                        }
                    )
                }
            }
            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                adapter = rememberScrollbarAdapter(
                    scrollState = state
                )
            )
        }

    }

    @Composable
    protected fun SourceLanguage(
        sourceLanguage: String,
        onSourceLanguageChange: (language: String) -> Unit
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val menuExpanded = remember { mutableStateOf(false) }

            Text(message("source_language"))
            Box {
                TextButton(onClick = {
                    menuExpanded.value = !menuExpanded.value
                }) {
                    Text(languageMessage(sourceLanguage))
                    Icon(
                        painter = javaPainterResource("icons/more.svg"),
                        contentDescription = "more",
                        modifier = Modifier.size(24.dp)
                    )
                }
                DropdownMenu(
                    expanded = menuExpanded.value,
                    onDismissRequest = { menuExpanded.value = false },
                    modifier = Modifier.background(MaterialTheme.colors.background)
                ) {
                    LanguageUtil.languages
                        .forEachIndexed { index, language ->
                            DropdownMenuItem(onClick = {
                                menuExpanded.value = false
                                onSourceLanguageChange(language)
                            }) {
                                Text(languageMessage(language))
                            }
                        }
                }
            }

        }
    }

}





