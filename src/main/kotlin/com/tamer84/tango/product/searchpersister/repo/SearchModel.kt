package com.tamer84.tango.product.searchpersister.repo


import com.tamer84.tango.icecream.domain.icsi.model.IceCreamStockItem
import com.tamer84.tango.product.searchpersister.util.DateUtil


data class IceCreamStockReadModel(var createdDate: String = DateUtil.nowIso8601Utc(),
                                  var lastModifiedDate: String = DateUtil.nowIso8601Utc(),
                                  var updateCount: Long = 1) : IceCreamStockItem()

data class StockMetadata(val id: String? = null,
                         val createdDate: String = DateUtil.nowIso8601Utc(),
                         var updateCount: Long = 0)

data class StockMetadataResponse(val _index: String,
                                 val _type: String,
                                 val _id: String,
                                 val _version: Int,
                                 val _seq_no: Long,
                                 val _primary_term: Int,
                                 val found: Boolean,
                                 val _source: StockMetadata
)

data class EsPutResponse(val _index: String,
                         val _type: String,
                         val _id: String,
                         val _version: Int,
                         val _seq_no: Long,
                         val _primary_term: Int,
                         val result: String,
                         val _shards: Map<String,Int>)
