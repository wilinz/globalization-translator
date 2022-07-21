package com.wilinz.androidi18n.color

import com.google.gson.Gson
import com.wilinz.androidi18n.translateXml
import com.wilinz.androidi18n.translator.GoogleTranslate
import com.wilinz.androidi18n.util.Language
import com.wilinz.androidi18n.util.LanguageUtil
import org.dom4j.Element
import org.dom4j.Node
import org.dom4j.io.OutputFormat
import org.dom4j.io.SAXReader
import org.dom4j.io.XMLWriter
import org.junit.Test
import java.io.File
import java.io.StringReader
import java.util.*

data class Data(val node: Node, val isSkip: Boolean)
class Test {
    @Test
    fun testDom4j() {
        val file =
            File("C:\\Users\\weilizan71\\Desktop\\gitother\\AndroidLocalizePlugin\\src\\main\\resources\\strings.xml")
        val reader = SAXReader()
        val document = reader.read(file)
        val nodeIterator = document.rootElement.nodeIterator()
        val translateList = mutableListOf<Data>()
        handlerNode(translateList, nodeIterator)
        translateList.forEachIndexed { index, data ->
            println("${index + 1},${data.isSkip}: ${data.node.asXML()}")
        }
    }

    @Test
    fun testDom4j2() {
        val text = """<string name="nav_group_library">Libraries</string>
    <string name="nav_destination_audio">Audio</string>
    <string name="nav_destination_images">Images</string>
    <string name="nav_destination_documents">Documents</string>
    <string name="nav_destination_videos">Videos</string>"""
        val xml = "<resources>$text</resources>"
        val reader = SAXReader()
        val document = reader.read(StringReader(xml))
        println(document.rootElement.selectSingleNode("string[@name=\"nav_destination_videos\"]"))
//        val nodeIterator = document.rootElement.nodeIterator()
//        val translateList = mutableListOf<Data>()
//        handlerNode(translateList, nodeIterator)
//        translateList.forEachIndexed { index, data ->
//            println("${index + 1},${data.isSkip}: ${data.node.asXML()}")
//        }
    }

    @Test
    fun test3() {
        val data = LanguageUtil.languages.toMutableList().apply {
            replaceAll {
                val suffix = if (it.code.contains("-")) {
                    val codeSplit = it.code.split("-")
                    codeSplit[0] + "-r" + codeSplit[1]
                } else {
                    it.code
                }
                it.copy(directoryName = "values-${suffix}")
            }
        }
        val json = Gson().toJson(data)
        val file =
            File("C:\\Users\\weilizan71\\Desktop\\gitother\\compose-jb\\examples\\intellij-plugin-with-experimental-shared-base\\src\\main\\resources\\language\\language.json")
        file.writeText(json)
    }

    @Test
    fun test4() {
        val result = GoogleTranslate().translate(listOf("你好", "世界"), "zh-CN", "en")
        println(result)
    }

    @Test
    fun test5() {
        val saxReader = SAXReader()
        val document =
            saxReader.read(File("C:\\Users\\wilinz\\StudioProjects\\AutoX\\app\\src\\main\\res\\values\\strings.xml"))
        val result = translateXml(
            translator = GoogleTranslate(),
            oldDocument = document,
            newDocument = null,
            form = Language(code = "zh-CN"),
            to = listOf(Language(code = "en")),
            onSuccess = { index, document, language ->
                val xmlWriter = XMLWriter(System.out, OutputFormat.createPrettyPrint()).apply { isEscapeText = false }
                xmlWriter.write(document)
            }
        )
        println(result)
    }

    @Test
    fun test6(){
        val language = Locale.getDefault().language
        val region = Locale.getDefault().country
        val localLanguageCode = "$language-$region"
        println(localLanguageCode.contains("zh-CN"))
    }
}

fun handlerNode(translateList: MutableList<Data>, nodeIterator: Iterator<*>, isSkip: Boolean = false) {
    if (nodeIterator.hasNext()) {
        val node: Node = nodeIterator.next() as Node
        if (node.nodeType == Node.COMMENT_NODE) {
            translateList.add(Data(node, true))
            when (node.text) {
                "start-translated" -> {
                    handlerNode(translateList, nodeIterator, true)
                }
                "end-translated" -> {
                    handlerNode(translateList, nodeIterator, false)
                }
            }
            return
        } else if (node.nodeType == Node.ELEMENT_NODE) {
            val e = node as Element
            val translatable = e.attributeValue("translatable").toBooleanStrictOrNull() ?: true
            if (isSkip) translateList.add(Data(node, isSkip))
            else translateList.add(Data(node, translatable))
        }
        handlerNode(translateList, nodeIterator, isSkip)
    }
}