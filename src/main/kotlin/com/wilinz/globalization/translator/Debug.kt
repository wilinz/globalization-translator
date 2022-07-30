package com.wilinz.globalization.translator

const val isDebug = false

fun printlnDebug(message: Any?) {
    if (isDebug) println(message)
}