package us.docbee.docbeeapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform