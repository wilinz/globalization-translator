package com.wilinz.globalization.translator.task

import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsContexts
import com.wilinz.globalization.translator.i18n.languageMessage
import com.wilinz.globalization.translator.i18n.message
import com.wilinz.globalization.translator.translator.engine.Translator

abstract class TranslateTask(
    project: Project?,
    private val form: String,
    private val to: List<String>,
    title: @NlsContexts.ProgressTitle String
) :
    Task.Backgroundable(project, title) {

    protected var translator: Translator? = null

    protected fun onEachError(index: Int, language: String, error: Throwable) {
        val notificationGroup = NotificationGroup(message("translation_failed"), NotificationDisplayType.BALLOON, true);
        val notification = notificationGroup.createNotification(
            "${index + 1}. ${message("translation_failed2", languageMessage(language), error.localizedMessage)}",
            NotificationType.ERROR
        );
        Notifications.Bus.notify(notification)
    }

    protected fun onEachSuccess(index: Int, language: String) {
        this.title = "${index + 1}. ${message("translate_success", languageMessage(language))}"
    }

    protected fun onEachStart(
        indicator: ProgressIndicator,
        index: Int,
        language: String
    ) {
        indicator.checkCanceled()
        this.title = "${index + 1}. ${message("translating", languageMessage(language))}"
    }

    override fun onCancel() {
        translator?.cancel()
        super.onCancel()
        val notificationGroup = NotificationGroup(message("cancel_translate"), NotificationDisplayType.BALLOON, true);
        val notification = notificationGroup.createNotification(
            message("translation_cancelled"),
            NotificationType.INFORMATION
        );
        Notifications.Bus.notify(notification);
    }

}