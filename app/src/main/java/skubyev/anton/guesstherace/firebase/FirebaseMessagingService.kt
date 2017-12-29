package skubyev.anton.guesstherace.firebase

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
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
import java.util.*
import javax.inject.Inject

class FirebaseMessagingService : com.google.firebase.messaging.FirebaseMessagingService() {
    @Inject lateinit var notificationRepository: NotificationsRepository

    @RequiresApi(Build.VERSION_CODES.O)
    private var mNotificationUtils: NotificationUtils? = null

    init {
        Toothpick.openScopes(DI.DATA_SCOPE, DI.FIREBASE_SCOPE).apply {
            Toothpick.inject(this@FirebaseMessagingService, this)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= 26) {
            val CHANNEL_ID = "my_channel_01"
            val channel = NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT)

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)

            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build()

            startForeground(1, notification)
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
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) showNotificationNew(title, message)
                        else showNotification(title, message)
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
        notificationManager.notify(getId(), builder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotificationNew(title: String, message: String) {
        val i = Intent(this, MainActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT)
        val nb = mNotificationUtils?.getAndroidChannelNotification(title, message)

        nb?.setContentIntent(pendingIntent)
        mNotificationUtils?.manager?.notify(getId(), nb?.build())
    }

    private fun sendMessage() {
        val intent = Intent("notification")
        intent.putExtra("message", true)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    private fun getId(): Int {
        val time = Date().time.toString()
        return time.substring(time.length - 5).toInt()
    }
}