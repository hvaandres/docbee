package us.docbee.docbeeapp.data.datasources.interfaces

import kotlinx.serialization.KSerializer

interface ResourcesLoader {
    suspend fun <T> loadResource(path: String, deserializer: KSerializer<List<T>>): List<T>
}