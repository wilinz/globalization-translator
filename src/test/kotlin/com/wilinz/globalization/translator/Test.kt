package com.wilinz.globalization.translator

import com.wilinz.globalization.translator.language.LanguageUtil
import com.wilinz.globalization.translator.translator.AndroidXmlTranslator
import com.wilinz.globalization.translator.translator.PropertiesTranslator
import com.wilinz.globalization.translator.translator.engine.GoogleTranslator
import org.codejive.properties.Properties
import org.dom4j.io.OutputFormat
import org.dom4j.io.SAXReader
import org.dom4j.io.XMLWriter
import org.junit.Test
import java.io.File
import java.io.StringReader

class Test {

    @Test
    fun testGoogleTranslator() {
        val result = GoogleTranslator().translate(listOf("albanian"), "en", "kok")
        println(result)
    }

    @Test
    fun testAndroidXmlTranslator() {
        val saxReader = SAXReader()
        val document =
            saxReader.read(StringReader("<r><string name=\"crash_feedback\">将会自动提交错误信息。您也可以手动复制调试信息提交给开发者(*^▽^*)或者点击\\\"退出\\\"退出程序o(╥﹏╥)o</string></r>"))
        AndroidXmlTranslator.translate(
            translator = GoogleTranslator(),
            oldDocument = document,
            newDocumentFetcher = null,
            form = "zh-CN",
            to = listOf("en"),
            onEachSuccess = { _, document, _ ->
                val xmlWriter = XMLWriter(System.out, OutputFormat.createPrettyPrint()).apply { isEscapeText = false }
                xmlWriter.write(document)
            }
        )
    }

    @Test
    fun testTranslateProperties() {
        PropertiesTranslator.translate(
            translator = GoogleTranslator(),
            oldProperties = Properties.loadProperties(File("")),
            null,
            "zh-CN",
            LanguageUtil.languages,
            onEachSuccess = { index, language, properties ->
                properties.store(System.out,false)
            }
        )
    }

}