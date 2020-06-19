package com.artembotnev.weatherstationserver.dao

import java.io.File
import java.io.IOException
import java.lang.NumberFormatException
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException

class DeviceAuthXmlDao : DeviceAuthDao {

    private val path = "${System.getProperty("user.dir")}${File.separator}"
    private val map = mutableMapOf<Int, String>()

    override fun getPasswordMap(): Map<Int, String> {
        try {
            parse(File("$path$FILE_NAME$XML_SUFFIX"))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return map
    }

    @Throws(ParserConfigurationException::class, IOException::class, NumberFormatException::class)
    private fun parse(file: File) {
        val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().run { parse(file) }
        val nodeList = document.documentElement.getElementsByTagName("device")

        for (i in 0 until nodeList.length) {
            val node = nodeList.item(i)
            val id = node.attributes.getNamedItem("id").nodeValue.toInt()
            val pass = node.textContent

            map[id] = pass
        }
    }

    companion object {
        private const val FILE_NAME = "pass_data"
        private const val XML_SUFFIX= ".xml"
    }
}