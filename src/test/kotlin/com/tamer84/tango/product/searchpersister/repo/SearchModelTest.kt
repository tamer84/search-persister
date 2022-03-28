package com.tamer84.tango.product.searchpersister.repo

import com.tamer84.tango.product.searchpersister.util.JsonUtil
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class SearchModelTest {

    @Test
    fun testMetaData_Defaults() {
        val m = StockMetadata()
        assertEquals(0, m.updateCount)
        assertNull(m.id)
        assertNotNull(m.createdDate)
    }

    @Test
    fun testStockMetadataResponsee_Deserialization_ProductWithoutSource() {

        val data = JsonUtil.fromJson<StockMetadataResponse>("""
        {
        	"_index": "de_20210101124510",
        	"_type": "_doc",
        	"_id": "ce3036be-ad53-40e9-ac1f-431441bd1f46",
        	"_version": 26,
        	"_seq_no": 6600,
        	"_primary_term": 1,
        	"found": true,
        	"_source": {}
        }
    """.trimIndent())

        assertEquals("ce3036be-ad53-40e9-ac1f-431441bd1f46", data._id)
        assertNotNull(data._source.createdDate)
        assertNull(data._source.id)
        assertEquals(0, data._source.updateCount)
    }
}
