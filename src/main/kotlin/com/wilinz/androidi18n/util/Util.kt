package com.wilinz.androidi18n.util

import com.intellij.openapi.vfs.VirtualFile

private val isStringsFileRegex = Regex("^strings.xml$|^plurals.xml$|^arrays.xml$")
private val addCodeTagRegex = Regex("""%([0-9]\$)?[sd]|\\n""")
private val removeCodeTagRegexPlaceholder = Regex("""<code>(%([0-9]\$)?[sd])</code>""")
private val removeCodeTagRegexLineBreak = Regex("""<code>(\\n)</code>""")
private val converseResultRegex = Regex("<b>(.*?)</b>")
private val converseResultRegex1 = Regex("<i>.*?</b>")
internal fun VirtualFile.isStringsFile(): Boolean {
//        val parent = file.parent ?: return false
//        if (parent.name != "values") return false
    return isStringsFileRegex.matches(name)
}

internal fun String.addCodeTag(): String {
    return addCodeTagRegex.replace(this) {
        "<code>${it.value}</code>"
    }
}

internal fun String.escapeAndroidXml(): String {
    return this.replace("\\\"", "&quot;")
        .replace("\\'", "&#39;")
        .replace(">", "&gt;")
}

internal fun String.unescapeAndroidXml(): String {
    return this.replace( "&quot;","\\\"",)
        .replace("&#39;","\\'", )
        .replace("&gt;",">", )
}

internal fun String.converseResult(): String {
    if (converseResultRegex1.matches(this)) {
        return converseResultRegex.findAll(this).joinToString(". ")
    }
    return this
}

internal fun String.removeCodeTag(): String {
    val result1 = removeCodeTagRegexPlaceholder.removeCodeTag(this)
    return removeCodeTagRegexLineBreak.removeCodeTag(result1)
}

internal fun Regex.removeCodeTag(text:String):String{
    return this.replace(text) {
        it.groups.getOrNull(1)?.value?.let { rawText ->
            return@replace rawText
        }
        it.value
    }
}

fun MatchGroupCollection.getOrNull(index: Int): MatchGroup? {
    if (this.size > index) {
        return this[index]
    }
    return null
}
