package com.wilinz.globalization.translator.util

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.wilinz.globalization.translator.language.LanguageUtil
import org.codejive.properties.Properties

object PropertiesUtil {

    private val addCodeTagRegex = Regex("""\{\d}""")
    private val removeCodeTagRegexPlaceholder = Regex("""<code>(\{\d})</code>""")

    internal fun String.addCodeTag(): String {
        return addCodeTag(addCodeTagRegex)
    }

    internal fun String.removeCodeTag(): String {
        return this.removeCodeTag(removeCodeTagRegexPlaceholder)
    }

    fun getLanguageByName(name: String): String? {
        val start = name.indexOf("_")
        val end = name.lastIndexOf(".").takeIf { it >= 0 } ?: name.length
        if (start >= 0) {
            val suffix = name.substring(start + 1, end)
            return LanguageUtil.propertiesLanguageMap.inverse()[suffix]
        }
        return null
    }

    fun getBaseName(name: String): String? {
        val end = name.indexOf("_").takeIf { it != -1 } ?: name.indexOf(".")
        if (end >= 0) {
            return name.substring(0, end)
        }
        return null
    }

    fun getFilenameByBaseName(baseName: String,language:String): String {
        return "${baseName}_${LanguageUtil.propertiesLanguageMap[language]}.properties"
    }

}

fun loadResourceProperties(name: String) =
    PropertiesUtil.javaClass.classLoader.getResourceAsStream(name)?.reader()?.use {
        Properties.loadProperties(it)
    }

fun Properties.toBiMap(): BiMap<String, String> {
    val biMap = HashBiMap.create<String, String>()
    this.forEach { k, v ->
        biMap[k] = v
    }
    return biMap
}