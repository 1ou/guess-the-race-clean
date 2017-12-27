package skubyev.anton.guesstherace.ui.settings

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_settings.*
import skubyev.anton.guesstherace.R
import skubyev.anton.guesstherace.music.MusicService
import skubyev.anton.guesstherace.presentation.settings.SettingsPresenter
import skubyev.anton.guesstherace.presentation.settings.SettingsView
import skubyev.anton.guesstherace.toothpick.DI
import skubyev.anton.guesstherace.ui.global.BaseFragment
import skubyev.anton.guesstherace.ui.global.ConfirmDialog
import toothpick.Toothpick

class SettingsFragment : BaseFragment(), SettingsView, ConfirmDialog.OnClickListener {

    private companion object {
        private val CONFIRM_WATCHED_IMAGES_TAG = "confirm_watched_images_tag"
    }

    private var musicService: MusicService? = null

    override val layoutRes = R.layout.fragment_settings

    @InjectPresenter lateinit var presenter: SettingsPresenter

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, binder: IBinder) {
            val binder = binder as MusicService.MusicBinder
            musicService = binder.service
            if (!presenter.isMusicTurnOn()) {
                musicService?.pauseMusic()
            }
        }
        override fun onServiceDisconnected(name: ComponentName) {}
    }

    override val dialogConfirm: (tag: String) -> Unit = { tag ->
        when (tag) {
            CONFIRM_WATCHED_IMAGES_TAG -> presenter.clearWatchedImages()
        }
    }

    @ProvidePresenter
    fun providePresenter(): SettingsPresenter {
        return Toothpick.openScopes(DI.MAIN_ACTIVITY_SCOPE)
                .getInstance(SettingsPresenter::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.setNavigationOnClickListener { presenter.onBackPressed() }

        clearFavorites.setOnClickListener {
            ConfirmDialog
                    .newInstants(
                            msg = getString(R.string.remove_from_watched_images),
                            positive = getString(R.string.remove),
                            tag = CONFIRM_WATCHED_IMAGES_TAG
                    )
                    .show(childFragmentManager, CONFIRM_WATCHED_IMAGES_TAG)
        }

        rateApp.setOnClickListener { launchMarket() }

        recommendApp.setOnClickListener {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
            shareIntent.type = "*/*"
            shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Get the Guess The Race App for you Smartphone or Tablet: " + "market://details?id=" + context?.packageName)
            startActivity(Intent.createChooser(shareIntent, "Select App to Share Text and Image"))
        }

        privacy.setOnClickListener {
            presenter.clickedPrivacy()
        }

        version_sub.text = context?.packageManager?.getPackageInfo(context?.packageName, 0)?.versionName

        musicSwitch.isChecked = presenter.isMusicTurnOn()

        val intent = Intent(context, MusicService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity?.startForegroundService(intent)
            activity?.bindService(intent, connection, BIND_AUTO_CREATE)
        } else {
            activity?.startService(intent)
            activity?.bindService(intent, connection, BIND_AUTO_CREATE)
        }

        musicSwitch.setOnCheckedChangeListener { _, b ->
            presenter.changeStateMusicPlayer(b)
            if (b) {
                musicService?.playMusic()
            } else {
                musicService?.pauseMusic()
            }
        }
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    private fun launchMarket() {
        val uri = Uri.parse("market://details?id=" + context?.packageName)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            showSnackMessage("Error")
        }
    }

    override fun onBackPressed() = presenter.onBackPressed()
}