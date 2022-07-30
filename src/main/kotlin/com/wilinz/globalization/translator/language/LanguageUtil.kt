package com.wilinz.globalization.translator.language

import com.google.common.collect.BiMap
import com.wilinz.globalization.translator.util.loadResourceProperties
import com.wilinz.globalization.translator.util.toBiMap
import org.codejive.properties.Properties
import java.util.*

object LanguageUtil {

    val languagesEn: Properties by lazy { loadResourceProperties("language/languages.properties")!! }
    val androidLanguageDirMap: BiMap<String, String> by lazy {
        loadResourceProperties("language/android.properties")!!.toBiMap()
    }
    val propertiesLanguageMap: BiMap<String, String> by lazy {
        loadResourceProperties("language/properties.properties")!!.toBiMap()
    }
    val languages: List<String> by lazy { languagesEn.keys.toList() }

    fun getLocalLanguage(): String? {
        return languages.firstOrNull { it == Locale.getDefault().language }
            ?: languages.firstOrNull { it == Locale.getDefault().language + "-" + Locale.getDefault().country }
    }

}
