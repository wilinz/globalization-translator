package com.wilinz.i18ntranslator.util

object AndroidUtil {
    private val addCodeTagRegex = Regex("""%(\d\$)?[sdf]|\\n""")
    private val removeCodeTagRegexPlaceholder = Regex("""<code>(%(\d\$)?[sdf])</code>""")
    private val removeCodeTagRegexLineBreak = Regex("""<code>(\\n)</code>""")

    internal fun String.addCodeTag(): String {
        return addCodeTag(addCodeTagRegex)
    }

    internal fun String.escapeAndroidXml(): String {
        return this.replace("\\\"", "&quot;")
            .replace("\\'", "&#39;")
            .replace(">", "&gt;")
    }

    internal fun String.unescapeAndroidXml(): String {
        return this.replace("&quot;", "\\\"")
            .replace("&#39;", "\\'")
            .replace("&gt;", ">")
    }


    internal fun String.removeCodeTag(): String {
        return this.removeCodeTag(removeCodeTagRegexPlaceholder).removeCodeTag(removeCodeTagRegexLineBreak)
    }

}