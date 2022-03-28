package com.tamer84.tango.product.searchpersister.repo

import com.nhaarman.mockitokotlin2.mock
import com.tamer84.tango.model.Market
import org.junit.Assert.assertEquals
import java.net.http.HttpClient
import kotlin.test.Test

class SearchRepoTest {

    val httpClient = mock<HttpClient>()

    val searchRepo = SearchRepo(httpClient, "http://localhost:9200", "")

    @Test
    fun testIndex() {
        val index = searchRepo.index(Market.DE)
        assertEquals("de", index)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testSaveWhenIdIsNull() {
        searchRepo.save(Market.DE, IceCreamStockReadModel())
    }
}
