package com.tamer84.tango.product.searchpersister

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestStreamHandler
import com.tamer84.tango.product.searchpersister.event.AwsTangoEvent
import com.tamer84.tango.product.searchpersister.util.IocContainer
import com.tamer84.tango.product.searchpersister.util.JsonUtil
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.slf4j.MDCContext
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import java.io.InputStream
import java.io.OutputStream

class EventHandler(private val persistService: Persister = IocContainer.persisterService) : RequestStreamHandler {

    companion object {
        private val log = LoggerFactory.getLogger(EventHandler::class.java)
    }

    override fun handleRequest(inputStream: InputStream, outStream: OutputStream, ctx: Context) = runBlocking(MDCContext()) {

        // Receive Event
        val awsEvent = JsonUtil.fromJson<AwsTangoEvent>(inputStream)
        val event = awsEvent.detail

        MDC.put("awsId", ctx.awsRequestId)
        MDC.put("sagaId", event.sagaId)
        MDC.put("productId", event.productId())
        MDC.put("market", event.market())

        try {
            log.info("EVENT received")

            persistService.save(event)

            log.info("EVENT complete")

            outStream.use {
                it.write(("Product saved [productId=${event.productId}, sagaId=${event.sagaId}, " +
                        "market=${event.market}}]").toByteArray())
            }
        }
        finally {
            MDC.clear()
        }
    }
}

