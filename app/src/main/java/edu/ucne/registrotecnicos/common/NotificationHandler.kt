package edu.ucne.registrotecnicos.common

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import edu.ucne.registrotecnicos.R
import kotlin.random.Random

class NotificationHandler(private val context: Context) {

    private val notificationManager =
        context.getSystemService(NotificationManager::class.java)
    private val notificationChannelID = "notification_channel_id"

    fun showSimpleNotification() {
        val notification = NotificationCompat.Builder(context, notificationChannelID)
            .setContentTitle("Notificación Simple")
            .setContentText("Este es el mensaje de la notificación.")
            .setSmallIcon(R.drawable.outline_notifications_24) // ¡Asegúrate de tener este ícono!
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setAutoCancel(true) // La notificación se cierra al tocarla
            .build()

        // Usamos un ID aleatorio para que cada notificación sea única
        notificationManager.notify(Random.nextInt(), notification)
    }
}