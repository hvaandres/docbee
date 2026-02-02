package us.docbee.docbeeapp.di

import org.koin.dsl.module
import us.docbee.docbeeapp.data.database.createDataStore
import us.docbee.docbeeapp.utils.ui.network.NetworkUtils

actual val nativeModules = module {
    single { createDataStore() }
    single {
        NetworkUtils(null).also { it.start() }
    }
}