package com.wilinz.androidi18n.task

import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsContexts
import com.intellij.openapi.vfs.VirtualFile
import com.wilinz.androidi18n.translator.AndroidXmlTranslator
import com.wilinz.androidi18n.translator.Translator
import com.wilinz.androidi18n.util.Language
import java.io.Reader

class TranslateTask(
    project: Project?,
    private val resourceDir: VirtualFile,
    private val documentReader: Reader,
    private val form: Language,
    private val to: List<Language>,
    title: @NlsContexts.ProgressTitle String
) :
    Task.Backgroundable(project, title) {

    private var translator: Translator? = null

    override fun run(indicator: ProgressIndicator) {
        translator = AndroidXmlTranslator.translateForIntellij(
            resourceDir = resourceDir,
            documentReader = documentReader,
            form = form,
            to = to,
            onEachStart = { index, language ->
                indicator.checkCanceled()
                this.title = "正在翻译到第${index + 1}个语言${language.name}"
            },
            onEachSuccess = { index, language ->
                val notificationGroup = NotificationGroup("翻译成功", NotificationDisplayType.BALLOON, true);
                val notification = notificationGroup.createNotification(
                    "翻译第${index + 1}个语言${language.name}成功",
                    NotificationType.INFORMATION
                );
                Notifications.Bus.notify(notification);
            },
            onEachError = { index, language, error ->
                val notificationGroup = NotificationGroup("翻译失败", NotificationDisplayType.BALLOON, true);
                val notification = notificationGroup.createNotification(
                    "翻译第${index + 1}个语言${language.name}失败：${error.message}",
                    NotificationType.ERROR
                );
                Notifications.Bus.notify(notification);
            }
        )
    }

    override fun onCancel() {
        translator?.cancel()
        super.onCancel()
        val notificationGroup = NotificationGroup("取消翻译", NotificationDisplayType.BALLOON, true);
        val notification = notificationGroup.createNotification(
            "您已取消翻译",
            NotificationType.INFORMATION
        );
        Notifications.Bus.notify(notification);
    }

}