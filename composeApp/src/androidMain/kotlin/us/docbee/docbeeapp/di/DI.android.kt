package us.docbee.docbeeapp.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import us.docbee.docbeeapp.data.database.createDataStore

actual val nativeModules = module {
    single { createDataStore(androidContext()) }
}