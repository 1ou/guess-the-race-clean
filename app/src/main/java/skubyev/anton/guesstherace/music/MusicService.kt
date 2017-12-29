package skubyev.anton.guesstherace.music

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import skubyev.anton.guesstherace.R
import skubyev.anton.guesstherace.model.interactor.settings.SettingsInteractor
import skubyev.anton.guesstherace.toothpick.DI
import toothpick.Toothpick
import javax.inject.Inject

@SuppressLint("Registered")
class MusicService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    private val binder = MusicBinder()
    @Inject lateinit var settingsInteractor: SettingsInteractor

    init {
        Toothpick.openScopes(DI.DATA_SCOPE, DI.MUSIC_SCOPE).apply {
            Toothpick.inject(this@MusicService, this)
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

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this.applicationContext, R.raw.jazz)
        }
        mediaPlayer?.isLooping = true
        playMusic()
        return START_NOT_STICKY
    }

    inner class MusicBinder : Binder() {
        internal
        val service: MusicService
            get() = this@MusicService
    }

    override fun onDestroy() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
    }

    override fun onBind(p0: Intent?): IBinder {
        playMusic()
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        pauseMusic()
        return super.onUnbind(intent)
    }

    fun playMusic() {
        if (settingsInteractor.isPlayMusic()) {
            mediaPlayer?.start()
        }
    }

    fun pauseMusic() {
        mediaPlayer?.pause()
    }
}