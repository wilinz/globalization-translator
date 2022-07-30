package com.wilinz.globalization.translator.i18n

import org.jetbrains.annotations.PropertyKey
import java.text.MessageFormat
import java.util.*

private const val BUNDLE = "messages.strings"

private val res: ResourceBundle = ResourceBundle.getBundle(BUNDLE, Locale.getDefault())

fun message(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any): String {
    return MessageFormat.format(res.getString(key), *params)
}