package com.tamer84.tango.product.searchpersister

import com.tamer84.tango.model.event.IndexableEvent
import com.tamer84.tango.product.searchpersister.aggregator.AggregatorClient
import com.tamer84.tango.product.searchpersister.repo.SearchRepo
import com.tamer84.tango.product.searchpersister.util.DateUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class Persister(private val aggregatorClient: AggregatorClient,
                private val searchRepo: SearchRepo
) {

    /**
     * When an event is received, this method saves the new or updated product into the search repository.
     * It performs the following:
     * - Query for the latest representation of the product from the aggregator
     * - Query for the existing product's metadata (if it exists)
     * - Update the metadata
     * - Save the latest product representation in the search repo
     */
    suspend fun save(event: IndexableEvent) = coroutineScope {

        val pendingItem = async(Dispatchers.IO) { aggregatorClient.fetchStockItem(event.productId()) }

        val stockItem = pendingItem.await()
            ?: throw IllegalStateException("Product not found in aggregator [id=${event.productId()}")


        searchRepo.getStockMetadata(event.market, event.productId()).let {

            stockItem.createdDate = it.createdDate
            stockItem.updateCount = ++it.updateCount
            stockItem.lastModifiedDate = DateUtil.nowIso8601Utc()

        }

        searchRepo.save(event.market, stockItem)
    }
}
