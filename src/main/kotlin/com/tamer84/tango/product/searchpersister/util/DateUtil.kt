package com.tamer84.tango.product.searchpersister.util

import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object DateUtil {

    fun nowIso8601Utc() = ZonedDateTime.now( ZoneOffset.UTC ).format( DateTimeFormatter.ISO_INSTANT )
}
