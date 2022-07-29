package com.wilinz.i18ntranslator

import com.wilinz.i18ntranslator.i18n.languageMessage
import com.wilinz.i18ntranslator.translator.AndroidXmlTranslator
import com.wilinz.i18ntranslator.translator.engine.GoogleTranslator
import com.wilinz.i18ntranslator.translator.PropertiesTranslator
import com.wilinz.i18ntranslator.language.LanguageUtil
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
    fun test9(){
        println(languageMessage("sq"))
    }
}