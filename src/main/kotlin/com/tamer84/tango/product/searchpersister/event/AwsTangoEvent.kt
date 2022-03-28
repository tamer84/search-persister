package com.tamer84.tango.product.searchpersister.event

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.tamer84.tango.model.event.IndexableEvent
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class AwsTangoEvent(val eventBusName : String?,
                          val time: Date,
                          val source: String,
                          val resources: List<String>,
                          @JsonProperty("detail-type") val detailType: String,
                          val detail: IndexableEvent
)
