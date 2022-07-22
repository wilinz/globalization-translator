package com.wilinz.androidi18n.color

import com.wilinz.androidi18n.translator.AndroidXmlTranslator
import com.wilinz.androidi18n.translator.GoogleTranslator
import com.wilinz.androidi18n.util.Language
import org.dom4j.io.OutputFormat
import org.dom4j.io.SAXReader
import org.dom4j.io.XMLWriter
import org.junit.Test
import java.io.File

class Test {

    @Test
    fun testGoogleTranslator() {
        val result = GoogleTranslator().translate(listOf("Welcome to &lt;b&gt;Android&lt;/b&gt;!"), "en", "zh-CN")
        println(result)
    }

    @Test
    fun testAndroidXmlTranslator() {
        val saxReader = SAXReader()
        val document =
            saxReader.read(File("C:\\Users\\wilinz\\StudioProjects\\AutoX\\app\\src\\main\\res\\values\\strings.xml"))//Enter your strings.xml file path
        AndroidXmlTranslator.translateXml(
            translator = GoogleTranslator(),
            oldDocument = document,
            newDocumentFetcher = null,
            form = Language(code = "zh-CN"),
            to = listOf(Language(code = "en")),
            onEachSuccess = { _, document, _ ->
                val xmlWriter = XMLWriter(System.out, OutputFormat.createPrettyPrint()).apply { isEscapeText = false }
                xmlWriter.write(document)
            }
        )
    }

    @Test
    fun test6(){
        val doc=SAXReader().read("C:\\Users\\wilinz\\StudioProjects\\AutoX\\app\\src\\main\\res\\values\\strings.xml")
        doc.rootElement.elements().forEach {
            println(it.name)
        }
    }
}