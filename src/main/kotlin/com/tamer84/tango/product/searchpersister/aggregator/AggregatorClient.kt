package com.tamer84.tango.product.searchpersister.aggregator

import com.tamer84.tango.product.searchpersister.repo.IceCreamStockReadModel
import com.tamer84.tango.product.searchpersister.util.EnvVar
import com.tamer84.tango.product.searchpersister.util.EnvVar.APPLICATION_NAME
import com.tamer84.tango.product.searchpersister.util.JsonUtil
import com.tamer84.tango.product.searchpersister.util.sendInstrumented
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest

class AggregatorClient(private val httpClient: HttpClient = HttpClient.newHttpClient(),
                       private val url: String = EnvVar.AGGREGATOR_URL) {

    companion object {
        private val log = LoggerFactory.getLogger(AggregatorClient::class.java)
        private const val APPLICATION_JSON = "application/json; charset=UTF-8"
    }

    /**
     * Fetch collect product by id.
     *
     * IMPORTANT: This is intentionally returning CollectStockReadModel instead of ConnectStockItem so that we
     * can avoid complex merging when persisting collect products.
     */
    fun fetchStockItem(id: String) : IceCreamStockReadModel? {

        val reqId = kotlin.runCatching { MDC.get("awsId").orEmpty() }.getOrDefault(APPLICATION_NAME)

        val req = HttpRequest.newBuilder(URI("$url/aggregator/$id"))
                .header("Accept", APPLICATION_JSON)
                .header("reqId", reqId)
                .GET()
                .build()

        val res = httpClient.sendInstrumented(req)

        return when(res.statusCode()) {
            200 -> JsonUtil.fromJson<IceCreamStockReadModel>(res.body())
            404 -> null.also { log.warn("StockItem not found [id=$id]")}
            else -> throw IllegalStateException("Request failed [status=${res.statusCode()}, response=${res.body()}]")
        }
    }
}
