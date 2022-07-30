package com.wilinz.globalization.translator.translator.engine

interface Translator {
    fun translate(
        queryList: List<String>,
        from: String,
        to: String
    ): List<String>
    fun cancel()
}