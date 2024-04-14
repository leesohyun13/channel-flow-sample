package com.sohyun.channelsample

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

fun findAddress(timeoutMills: Long) = channelFlow<String> {
    val addressCollector = launch {
        getAddress().collect {
            delay(200)
            send(it)
        }
        close()
    }

    delay(timeoutMills)
    addressCollector.cancel()

    val collector = launch {
        getCurrentAddress().collect {
            delay(100)
            send(it)
        }
        close()
    }

    delay(timeoutMills)
    collector.cancel()
    throw Exception("time out")
}

fun getCurrentAddress() : Flow<String> = flow {
    delay(1000)
    emit("current location")
}

fun getAddress() : Flow<String> = flow {
    delay(100)
    emit("current location")
}