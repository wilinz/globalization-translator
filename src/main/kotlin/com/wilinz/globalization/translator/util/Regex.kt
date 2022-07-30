package com.wilinz.globalization.translator.util

import com.intellij.openapi.vfs.VirtualFile

private val isStringsFileRegex = Regex("^strings.xml$|^plurals.xml$|^arrays.xml$")
private val converseResultRegex = Regex("<b>(.*?)</b>")
private val converseResultRegex1 = Regex("<i>.*?</b>")
internal fun VirtualFile.isStringsXmlFile(): Boolean {
    val parent = this.parent ?: return false
    if (!parent.name.matches(Regex("^values.*$"))) return false
    return isStringsFileRegex.matches(name)
}

internal fun String.removeCodeTag(regex: Regex): String {
    return regex.replace(this) {
        it.groups.getOrNull(1)?.value?.let { rawText ->
            return@replace rawText
        }
        it.value
    }
}

fun String.addCodeTag(regex: Regex): String {
    return regex.replace(this) {
        "<code>${it.value}</code>"
    }
}

fun String.escapeHTML(): String {
    return this
        .replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;")
        .replace("'", "&#39;");
}

fun String.unescapeHTML(): String {
    return this
        .replace("&amp;", "&")
        .replace("&lt;", "<")
        .replace("&gt;", ">")
        .replace("&quot;", "\"")
        .replace("&#39;", "'");
}

internal fun String.converseResult(): String {
    if (converseResultRegex1.matches(this)) {
        return converseResultRegex.findAll(this).joinToString(". ")
    }
    return this
}

fun MatchGroupCollection.getOrNull(index: Int): MatchGroup? {
    if (this.size > index) {
        return this[index]
    }
    return null
}
