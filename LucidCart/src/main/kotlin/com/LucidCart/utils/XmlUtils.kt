package com.LucidCart.utils


import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper

object XmlUtils {

    private val mapper = XmlMapper().apply {
        configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    fun <T> fromXml(xml: String, clazz: Class<T>): T {
        return mapper.readValue(xml, clazz)
    }

    fun toXml(obj: Any): String {
        return mapper.writeValueAsString(obj)
    }
}