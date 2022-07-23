package com.wilinz.androidi18n.i18n

import org.jetbrains.annotations.PropertyKey

const val BUNDLE = "messages.strings"

object TranslationBundle : MyDynamicBundle(BUNDLE)

fun message(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any): String {
    return TranslationBundle.getMessage(key, *params)
}

fun adaptedMessage(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any): String {
    return TranslationBundle.getAdaptedMessage(key, *params)
}