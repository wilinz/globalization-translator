package com.wilinz.globalization.translator

import com.wilinz.globalization.translator.i18n.message
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
            saxReader.read(File(""))//Enter your strings.xml file path
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

    @Test
    fun test1() {
        val result = message("translate_file",'a')
        println(result)
    }

}