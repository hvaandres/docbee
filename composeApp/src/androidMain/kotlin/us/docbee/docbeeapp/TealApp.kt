package us.docbee.docbeeapp

import android.app.Application
import com.google.firebase.FirebaseApp
import us.docbee.docbeeapp.di.initKoin

class TealApp: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
        FirebaseApp.initializeApp(this)
    }
}