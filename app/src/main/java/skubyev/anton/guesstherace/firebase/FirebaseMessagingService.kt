package skubyev.anton.guesstherace.firebase

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.content.LocalBroadcastManager
import com.google.firebase.messaging.RemoteMessage
import skubyev.anton.guesstherace.R
import skubyev.anton.guesstherace.extension.subscribeIgnoreResult
import skubyev.anton.guesstherace.model.data.storage.NotificationEntity
import skubyev.anton.guesstherace.model.repository.notifications.NotificationsRepository
import skubyev.anton.guesstherace.toothpick.DI
import skubyev.anton.guesstherace.ui.launch.MainActivity
import toothpick.Toothpick
import javax.inject.Inject

class FirebaseMessagingService : com.google.firebase.messaging.FirebaseMessagingService() {
    @Inject lateinit var notificationRepository: NotificationsRepository

    init {
        Toothpick.openScopes(DI.DATA_SCOPE, DI.FIREBASE_SCOPE).apply {
            Toothpick.inject(this@FirebaseMessagingService, this)
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        val title = remoteMessage?.notification?.title
        val message = remoteMessage?.notification?.body

        if (message != null && title != null) {
            val notification = NotificationEntity()
            notification.title = title
            notification.message = message
            notification.show = true

            notificationRepository.addNotification(notification)
                    .doOnComplete {
                        showNotification(title, message)
                        sendMessage()
                    }
                    .subscribeIgnoreResult()
        }
    }

    private fun showNotification(title: String, message: String) {
        val i = Intent(this, MainActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(title).setContentText(message)
                .setSmallIcon(R.drawable.ic_chevron_down)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, builder.build())
    }

    private fun sendMessage() {
        val intent = Intent("notification")
        intent.putExtra("message", true)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }
}