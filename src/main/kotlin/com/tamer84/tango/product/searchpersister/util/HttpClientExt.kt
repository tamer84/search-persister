package com.tamer84.tango.product.searchpersister.util

import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.ConnectException
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

private val log = LoggerFactory.getLogger(HttpClient::class.java)

fun HttpClient.sendInstrumented(req: HttpRequest) : HttpResponse<String> {

    try {
        val start = System.currentTimeMillis()

        log.info("Request [method={}, uri={}]", req.method(), req.uri())

        return this.send(req, HttpResponse.BodyHandlers.ofString()).also {
            log.info("Response [method={}, uri={}, code={}, durationMs={}]",
                    it.request().method(), it.request().uri(), it.statusCode(), (System.currentTimeMillis()-start))
        }
    }
    catch(e: ConnectException) {
        throw IOException("CONNECTION FAILURE [method=${req.method()}, uri=${req.uri()}]", e)
    }
}
