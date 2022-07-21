package com.wilinz.androidi18n

import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsContexts
import com.intellij.openapi.vfs.VirtualFile
import com.wilinz.androidi18n.util.Language
import java.io.Reader

class

TranslateTask(
    project: Project?,
    private val resourceDir: VirtualFile,
    private val documentReader: Reader,
    private val form: Language,
    private val to: List<Language>,
    title: @NlsContexts.ProgressTitle String
) :
    Task.Backgroundable(project, title) {

    private var translateHandler: TranslateHandler? = null

    override fun run(indicator: ProgressIndicator) {
        translateHandler = TranslateHandler(
            resourceDir = resourceDir,
            documentReader = documentReader,
            form = form,
            to = to,
            onTranslating = { index, language ->
                this.title = "正在翻译到第${index + 1}个语言${language.name}"
            },
            onSuccess = { index, language ->
                val notificationGroup = NotificationGroup("翻译成功", NotificationDisplayType.BALLOON, true);
                val notification = notificationGroup.createNotification(
                    "翻译第${index + 1}个语言${language.name}成功",
                    NotificationType.INFORMATION
                );
                Notifications.Bus.notify(notification);
            },
            onError = { index, language, error ->
                val notificationGroup = NotificationGroup("翻译失败", NotificationDisplayType.BALLOON, true);
                val notification = notificationGroup.createNotification(
                    "翻译第${index + 1}个语言${language.name}失败：${error.message}",
                    NotificationType.ERROR
                );
                Notifications.Bus.notify(notification);
            }
        ).apply {
            start()
        }
    }

    override fun onCancel() {
        translateHandler?.cancel()
        super.onCancel()
    }

}