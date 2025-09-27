package us.docbee.docbeeapp

import android.app.Application
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import us.docbee.docbeeapp.di.initKoin

class TealApp: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@TealApp)
        }
        FirebaseApp.initializeApp(this)
    }
}