package com.tamer84.tango.product.searchpersister.util

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.InputStream

val mapper: ObjectMapper = jacksonObjectMapper()
        .registerModules(Jdk8Module())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

object JsonUtil {

    inline fun <reified T> fromJson(inputStream: InputStream) : T = mapper.readValue(inputStream)

    inline fun <reified T> fromJson(json : String) : T = mapper.readValue(json)

    fun <T> fromMap(map: Map<String, Any>, clazz: Class<T>) : T {
        return mapper.convertValue(map, clazz)
    }

    fun toJson(value: Any) : String {
        return mapper.writeValueAsString(value)
    }
}
