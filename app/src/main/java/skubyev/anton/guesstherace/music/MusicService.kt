package skubyev.anton.guesstherace.music

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import skubyev.anton.guesstherace.R

@SuppressLint("Registered")
class MusicService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    private val binder = MusicBinder()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this.applicationContext, R.raw.jazz)
        }
        mediaPlayer?.isLooping = true
        playMusic()
        return START_STICKY
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
        mediaPlayer?.start()
    }

    fun pauseMusic() {
        mediaPlayer?.pause()
    }
}