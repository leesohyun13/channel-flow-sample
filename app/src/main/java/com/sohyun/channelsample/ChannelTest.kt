package com.sohyun.channelsample

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * 기본적인 flow
 * */
fun defaultFlow() = runBlocking {
    flow<Int> {
        (0..10).forEach {
            println("emit $it")
            emit(it) // 1을 입력하면
        }
    }.collect {
        println("collect $it") // 1이 출력되고,
        delay(1) // 1ms를 대기한다. 그동안 blocking 한다.
    }
    delay(100)
}

/**
 * ChannelFlow could control each coroutine scope
 * */
suspend fun launchAdditionalProducer(scope: ProducerScope<String>) {
    scope.launch {
        repeat(5) { index ->
            delay(50)
            scope.send("Message $index from Main Additional Producer")
        }
    }
}

fun channelFlowWithLaunches() {
    val channel = channelFlow {
        launch {
            repeat(20) { index ->
                delay(100)
                send("Message $index from Main Producer")
                if (index == 2) {
                    launchAdditionalProducer(this@channelFlow)
                }
                if (index >= 5) {
                    cancel()
                }
            }
        }
        launch {
            repeat(20) { index ->
                delay(33)
                send("Message $index from Secondary Producer")
            }
        }
    }
    CoroutineScope(Dispatchers.Default).launch {
        channel.collect { value ->
            println(value)
        }
    }
}