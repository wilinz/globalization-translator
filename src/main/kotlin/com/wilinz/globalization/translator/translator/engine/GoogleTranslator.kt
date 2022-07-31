package com.wilinz.globalization.translator.translator.engine

import com.google.gson.JsonParser
import com.wilinz.globalization.translator.network.OkHttp
import com.wilinz.globalization.translator.printlnDebug
import com.wilinz.globalization.translator.util.converseResult
import com.wilinz.globalization.translator.util.firstUppercase
import okhttp3.Call
import okhttp3.FormBody
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Request

class GoogleTranslator : Translator {

    private val url = "https://translate.googleapis.com/translate_a/t?anno=3&client=te&v=1.0&format=html"

    private val callList = mutableListOf<Call>()

    override fun translate(
        queryList: List<String>,
        from: String,
        to: String
    ): List<String> {
        val tk = getGoogleToken(queryList.joinToString(""))
        val httpUrl = url.toHttpUrl().newBuilder()
            .addQueryParameter("sl", from)
            .addQueryParameter("tl", to)
            .addQueryParameter("tk", tk)
            .build()
        val formBody = FormBody.Builder().apply {
            queryList.forEach {
                add("q", it)
            }
        }.build()

        val request = Request.Builder()
            .url(httpUrl)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .post(formBody)
            .build()
        val call = OkHttp.client.newCall(request)
        callList.add(call)
        try {
            val response = call.execute()
            response.body?.string()?.let {
                val result = JsonParser.parseString(it).asJsonArray.map { element ->
                    element.asString.converseResult().firstUppercase().trim()
                }
                printlnDebug(result)
                return result
            }
        } finally {
            callList.remove(call)
        }
        return emptyList()
    }

    override fun cancel() {
        callList.forEach {
            if (!it.isCanceled()) it.cancel()
        }
        callList.clear()
    }
}