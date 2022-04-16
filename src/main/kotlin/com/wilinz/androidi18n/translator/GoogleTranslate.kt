package com.wilinz.androidi18n.translator

import com.wilinz.androidi18n.model.TranslateResult
import com.wilinz.androidi18n.network.OkHttpConfig
import com.wilinz.androidi18n.util.GoogleHttp
import com.wilinz.androidi18n.util.token
import okhttp3.FormBody
import okhttp3.Request
import org.json.JSONArray

class GoogleTranslate {

    companion object {
        private const val url = "http://translate.google.com/translate_a/single"
    }

    fun translateMultipleText(multipleText: List<String>, from: String, to: String): List<TranslateResult> {
        val translateResult = translate(multipleText.joinToString("\n"), from, to)
        return translateResult.map { TranslateResult(it.src.trimEnd('\n'), it.result.trimEnd('\n')) }
    }

    fun translate(text: String, from: String, to: String): List<TranslateResult> {
        val tk = token(text)

        val requestBody = FormBody.Builder()
            .add("client", "webapp")
            .add("sl", from)
            .add("tl", to)
            .add("hl", "zh-CN")
            .add("dt", "t")
            .add("ie", "UTF-8")//输入编码
            .add("oe", "UTF-8")//输出编码
            .add("source", "bh")
            .add("tk", tk)
//            .add("q", "text")
            .add("q", text)
            .build()

        val request = Request.Builder()
            .url(url)
            .addHeader("User-Agent", GoogleHttp.userAgent)
            .post(requestBody)
            .build()

        val response = OkHttpConfig.okHttpClient.newCall(request).execute()
        val respString = response.body?.string() ?: throw NullPointerException("response data is null")
        val data = JSONArray(respString)

        val translateResultList = mutableListOf<TranslateResult>()

        for (result in data[0] as JSONArray) {
            if (result is JSONArray) {
                val translateResult = TranslateResult(
                    result.getString(1),
                    result.getString(0)
                )
                translateResultList.add(translateResult)
            }
        }

        return translateResultList
    }
}