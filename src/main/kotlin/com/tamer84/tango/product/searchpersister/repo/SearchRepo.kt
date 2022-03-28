package com.tamer84.tango.product.searchpersister.repo

import com.tamer84.tango.model.Market
import com.tamer84.tango.product.searchpersister.util.JsonUtil
import com.tamer84.tango.product.searchpersister.util.sendInstrumented
import net.logstash.logback.argument.StructuredArguments
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest

/**
 * Repository class responsible for querying ElasticSearch for stock data.

 * This class intentionally uses HttpClient to keep dependencies and the shaded JAR size to a minimum
 */
class SearchRepo(private val client: HttpClient,
                 private val elasticsearchUrl: String,
                 private val indexPrefix: String) {

    companion object {
        private val log = LoggerFactory.getLogger(SearchRepo::class.java)
        private const val APPLICATION_JSON = "application/json; charset=UTF-8"
    }


    /**
     * Creates the index name for the given market
     *
     * note: the index name used here is really an 'alias'
     *
     * @param market
     * @return the index name (i.e collect_de)
     */
    fun index(market: Market) = "${indexPrefix}${market.name.lowercase()}"

    /**
     * Find StockMetadata by identifier
     *
     * If the stock is not found, a StockMetadata object is returned wtih default values
     *
     * @param market the market (i.e. FR)
     * @param id the productId
     *
     * @return StockMetadata
     */
    fun getStockMetadata(market: Market, id: String) : StockMetadata {

        val uri = URI.create("$elasticsearchUrl/${index(market)}/_doc/$id?_source_includes=id,createdDate,updateCount")

        val req = HttpRequest.newBuilder(uri)
                .GET()
                .header("Accept", APPLICATION_JSON)
                .build()

        val res = client.sendInstrumented(req)

        return when(res.statusCode()) {
            200  -> JsonUtil.fromJson<StockMetadataResponse>(res.body())._source
            404  -> StockMetadata().also { log.warn("Collect not found", StructuredArguments.raw("esResponse", res.body()))}
            else -> throw IOException("Save failed [status=${res.statusCode()}, esResponse=${res.body()}]")
        }
    }

    /**
     * Save the stock to the index
     *
     * @param market the market
     * @param document the stockItem to save
     */
    fun save(market: Market, document: IceCreamStockReadModel) {

        require(document.id != null) { "collect.id is required" }

        val uri = URI.create("$elasticsearchUrl/${index(market)}/_doc/${document.id}")

        val req = HttpRequest.newBuilder(uri)
                .POST(HttpRequest.BodyPublishers.ofString(JsonUtil.toJson(document)))
                .header("Accept", APPLICATION_JSON)
                .header("Content-Type", APPLICATION_JSON)
                .build()

        val res = client.sendInstrumented(req)

        when(res.statusCode()) {
            200, 201 -> JsonUtil.fromJson<EsPutResponse>(res.body()).also {
                log.info("Document ${it.result}")
            }
            else -> throw IOException("Save failed [status=${res.statusCode()}, esResponse=${res.body()}]")
        }
    }
}
