package com.wilinz.androidi18n.translator

interface Translator {
    fun translate(
        queryList: List<String>,
        from: String,
        to: String
    ): List<String>
    fun cancel()
}