package com.wilinz.androidi18n.translator

import com.wilinz.androidi18n.network.OkHttp
import com.wilinz.androidi18n.util.*
import okhttp3.Call
import okhttp3.FormBody
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Request
import org.json.JSONArray

class GoogleTranslate : Translator {

    private val url = "https://translate.googleapis.com/translate_a/t?anno=3&client=te&v=1.0&format=html"

    private val callList = mutableListOf<Call>()

    override fun translate(
        queryList: List<String>,
        from: String,
        to: String
    ): List<String> {
        val queryList1 = queryList.map { it.escapeAndroidXml().addCodeTag() }
        val tk = token(queryList1.joinToString(""))
        val httpUrl = url.toHttpUrl().newBuilder()
            .addQueryParameter("sl", from)
            .addQueryParameter("tl", to)
            .addQueryParameter("tk", tk)
            .build()
        val formBody = FormBody.Builder().apply {
            queryList1.forEach {
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
                return JSONArray(it).map { result ->
                    (result as String).converseResult().removeCodeTag().unescapeAndroidXml()
                }
            }
        } catch (e: Exception) {
            throw e
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