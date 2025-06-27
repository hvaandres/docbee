package com.docbee.tealapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform