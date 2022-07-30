package com.wilinz.globalization.translator.task

import com.intellij.notification.*
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsContexts
import com.wilinz.globalization.translator.i18n.languageMessage
import com.wilinz.globalization.translator.i18n.message
import com.wilinz.globalization.translator.translator.engine.Translator

abstract class TranslateTask(
    project: Project?,
    title: @NlsContexts.ProgressTitle String
) :
    Task.Backgroundable(project, title) {

    protected var translator: Translator? = null

    protected fun onEachError(index: Int, language: String, error: Throwable) {
        val msg = "${index + 1}. ${message("translation_failed2", languageMessage(language), error.localizedMessage)}"
        NotificationGroupManager.getInstance()
            .getNotificationGroup("Globalization Translator Notification")
            .createNotification(message("translation_failed"), msg, NotificationType.ERROR)
            .notify(project);
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

    protected fun onComplete(){
        NotificationGroupManager.getInstance()
            .getNotificationGroup("Globalization Translator Notification Sticky")
            .createNotification(message("translation_complete"), NotificationType.INFORMATION)
            .notify(project);
    }

    override fun onCancel() {
        translator?.cancel()
        super.onCancel()
        NotificationGroupManager.getInstance()
            .getNotificationGroup("Globalization Translator Notification")
            .createNotification(message("translation_cancelled"), NotificationType.INFORMATION)
            .notify(project);
    }

}