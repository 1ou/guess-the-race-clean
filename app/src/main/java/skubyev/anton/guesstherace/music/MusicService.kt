package skubyev.anton.guesstherace.music

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import skubyev.anton.guesstherace.R

@SuppressLint("Registered")
class MusicService : Service() {

    private lateinit var mediaPlayer: MediaPlayer
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        mediaPlayer = MediaPlayer.create(this.applicationContext, R.raw.jazz)
        mediaPlayer.isLooping = true
        mediaPlayer.start()

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    override fun onBind(p0: Intent?): IBinder {
        return onBind(p0)
    }
}