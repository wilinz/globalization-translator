package com.wilinz.globalization.translator.i18n

import org.jetbrains.annotations.PropertyKey
import java.text.MessageFormat
import java.util.*

private const val LANGUAGE_BUNDLE = "messages.languages"

private val res: ResourceBundle = ResourceBundle.getBundle(LANGUAGE_BUNDLE, Locale.getDefault())

fun languageMessage(@PropertyKey(resourceBundle = LANGUAGE_BUNDLE) key: String, vararg params: Any): String {
    return MessageFormat.format(res.getString(key), *params)
}


