package com.tamer84.tango.product.searchpersister

import com.nhaarman.mockitokotlin2.*
import com.tamer84.tango.model.Market
import com.tamer84.tango.model.ProductType
import com.tamer84.tango.model.event.IndexableEvent
import com.tamer84.tango.product.searchpersister.aggregator.AggregatorClient
import com.tamer84.tango.product.searchpersister.repo.IceCreamStockReadModel
import com.tamer84.tango.product.searchpersister.repo.SearchRepo
import com.tamer84.tango.product.searchpersister.repo.StockMetadata

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import java.util.*
import kotlin.test.Test

class CollectServiceTest {

    val testEvent = IndexableEvent(UUID.fromString("ad3a30cc-684c-48fd-9c7e-c50317273415"), "sagaId", "unit_test",
        ProductType.ICE_CREAM, Market.DE, System.currentTimeMillis())


    private val aggregatorClient = mock<AggregatorClient>()

    private val searchRepo = mock<SearchRepo>()

    private val persistService = Persister(aggregatorClient, searchRepo)

    @Test(expected = IllegalStateException::class)
    fun testAggregateNotFound() {

        whenever(aggregatorClient.fetchStockItem(testEvent.productId())).thenReturn(null)
        try {
            runBlocking {
                persistService.save(testEvent)
            }
        }
        finally {
            verify(aggregatorClient).fetchStockItem(testEvent.productId())
            verify(searchRepo, never()).getStockMetadata(testEvent.market, testEvent.productId())
            verify(searchRepo, never()).save(any(), any())
        }
    }

    @Test
    fun testNewDocument() {

        val aggregateModel = IceCreamStockReadModel().apply {
            id = UUID.randomUUID().toString()
        }

        whenever(aggregatorClient.fetchStockItem(testEvent.productId())).thenReturn(aggregateModel)
        whenever(searchRepo.getStockMetadata(testEvent.market, testEvent.productId())).thenReturn(
            StockMetadata()
        )

        runBlocking {
            persistService.save(testEvent)
        }

        verify(aggregatorClient).fetchStockItem(testEvent.productId())
        verify(searchRepo).getStockMetadata(testEvent.market, testEvent.productId())
        verify(searchRepo).save(testEvent.market, aggregateModel)

        assertEquals(1, aggregateModel.updateCount)
    }

}
