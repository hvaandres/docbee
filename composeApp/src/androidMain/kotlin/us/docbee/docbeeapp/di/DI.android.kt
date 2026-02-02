package us.docbee.docbeeapp.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import us.docbee.docbeeapp.data.database.createDataStore
import us.docbee.docbeeapp.utils.ui.network.NetworkUtils

actual val nativeModules = module {
    single { createDataStore(androidContext()) }
    single { NetworkUtils(androidContext()).also { it.start() } }
}