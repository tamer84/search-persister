package com.tamer84.tango.product.searchpersister.util

import com.tamer84.tango.product.searchpersister.Persister
import com.tamer84.tango.product.searchpersister.aggregator.AggregatorClient
import com.tamer84.tango.product.searchpersister.repo.SearchRepo
import java.net.http.HttpClient
import java.time.Duration

// This Object serves the role that is typically reserved for IOC or DI Frameworks
// For complicated projects, it may make sense to use a lightweight DI such as Koin
object IocContainer {

    private val httpClient = createHttpClient()

    private val aggregatorClient = AggregatorClient(httpClient)

    private val searchRepo = SearchRepo(httpClient, EnvVar.ELASTICSEARCH_URL, EnvVar.INDEX_PREFIX)

    val persisterService = Persister(aggregatorClient, searchRepo)

    private fun createHttpClient(connectTimeoutSec: Long = 10): HttpClient {
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(connectTimeoutSec))
                .build()
    }
}
