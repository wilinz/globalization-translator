package com.wilinz.i18ntranslator.i18n

import org.jetbrains.annotations.PropertyKey

const val LANGUAGE_BUNDLE = "messages.languages"

object LanguageBundle : MyDynamicBundle(LANGUAGE_BUNDLE)

fun languageMessage(@PropertyKey(resourceBundle = LANGUAGE_BUNDLE) key: String, vararg params: Any): String {
    return LanguageBundle.getMessage(key, *params)
}

fun adaptedLanguageMessage(@PropertyKey(resourceBundle = LANGUAGE_BUNDLE) key: String, vararg params: Any): String {
    return LanguageBundle.getAdaptedMessage(key, *params)
}