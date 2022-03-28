package com.tamer84.tango.product.searchpersister.util

object EnvVar {

    val APPLICATION_NAME: String    = System.getenv("APPLICATION_NAME")
    val AGGREGATOR_URL: String      = System.getenv("AGGREGATOR_URL")
    val INDEX_PREFIX        = System.getenv("COLLECT_INDEX_PREFIX") ?: ""
    val ELASTICSEARCH_URL: String   = System.getenv("ELASTICSEARCH_URL")
}
