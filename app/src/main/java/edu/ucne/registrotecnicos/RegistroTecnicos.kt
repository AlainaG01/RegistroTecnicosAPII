package edu.ucne.registrotecnicos

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
@HiltAndroidApp
class RegistroTecnicos: Application() {
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                "notification channel id",
                 "Notificaciones Generales",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Canal para notificaciones generales de la app"

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as
                    NotificationManager
                    notificationManager.createNotificationChannel(channel)

        }
    }
}