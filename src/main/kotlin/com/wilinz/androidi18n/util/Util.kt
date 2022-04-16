package com.wilinz.androidi18n.util

import com.google.gson.Gson
import com.intellij.openapi.vfs.VirtualFile

fun VirtualFile.isStringsFile(): Boolean {
//        val parent = file.parent ?: return false
//        if (parent.name != "values") return false
    val regex = Regex("^strings.xml$|^plurals.xml$|^arrays.xml$")
    return regex.matches(name)
}
