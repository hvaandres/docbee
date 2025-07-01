package us.docbee.docbeeapp

import android.app.Application
import com.google.firebase.FirebaseApp

class TealApp: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}