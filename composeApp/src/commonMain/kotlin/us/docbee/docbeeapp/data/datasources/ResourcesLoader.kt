package us.docbee.docbeeapp.data.datasources

import docbee.composeapp.generated.resources.Res
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import us.docbee.docbeeapp.data.datasources.interfaces.ResourcesLoader

class JsonResourceLoader: ResourcesLoader {
    override suspend fun <T> loadResource(
        path: String,
        deserializer: KSerializer<List<T>>
    ): List<T> {
        val json = Res.readBytes(path).decodeToString()
        return Json.decodeFromString(deserializer, json)
    }
}
