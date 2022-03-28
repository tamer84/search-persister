package com.tamer84.tango.product.searchpersister.aggregator

import com.tamer84.tango.icecream.domain.icsi.model.IceCreamStockItem
import com.tamer84.tango.product.searchpersister.repo.IceCreamStockReadModel
import com.tamer84.tango.product.searchpersister.util.JsonUtil

import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class AggregatorClientTest {

    /**
     * This test is important because the Aggregator really returns JSON that represents ConnectStockItem.
     *
     * The CollectStockReadModel extends CollectStockItem
     */
    @Test
    fun testCollectStockReadModelDeserialization() {

        val stockItem = IceCreamStockItem.builder()
            .id(UUID.randomUUID().toString())
            .build()

        val json = JsonUtil.toJson(stockItem)

        val readModel = JsonUtil.fromJson<IceCreamStockReadModel>(json)

        assertEquals(stockItem.id, readModel.id)
        assertEquals(1, readModel.updateCount)

        println(readModel)
    }
}
