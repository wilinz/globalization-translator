package com.wilinz.i18ntranslator.translator.engine

interface Translator {
    fun translate(
        queryList: List<String>,
        from: String,
        to: String
    ): List<String>
    fun cancel()
}