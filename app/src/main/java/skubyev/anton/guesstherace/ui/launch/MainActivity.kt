package skubyev.anton.guesstherace.ui.launch

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.firebase.analytics.FirebaseAnalytics
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.SupportAppNavigator
import ru.terrakok.cicerone.commands.Back
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward
import skubyev.anton.guesstherace.R
import skubyev.anton.guesstherace.Screens
import skubyev.anton.guesstherace.music.MusicService
import skubyev.anton.guesstherace.presentation.drawer.NavigationDrawerView
import skubyev.anton.guesstherace.presentation.global.GlobalMenuController
import skubyev.anton.guesstherace.presentation.launch.LaunchPresenter
import skubyev.anton.guesstherace.presentation.launch.LaunchView
import skubyev.anton.guesstherace.toothpick.DI
import skubyev.anton.guesstherace.toothpick.module.MainActivityModule
import skubyev.anton.guesstherace.ui.about.AboutFragment
import skubyev.anton.guesstherace.ui.auth.AuthActivity
import skubyev.anton.guesstherace.ui.browser.BrowserFragment
import skubyev.anton.guesstherace.ui.comments.CommentsFragment
import skubyev.anton.guesstherace.ui.drawer.NavigationDrawerFragment
import skubyev.anton.guesstherace.ui.feedback.FeedbackFragment
import skubyev.anton.guesstherace.ui.global.BaseActivity
import skubyev.anton.guesstherace.ui.global.BaseFragment
import skubyev.anton.guesstherace.ui.home.HomeFragment
import skubyev.anton.guesstherace.ui.notifications.NotificationsFragment
import skubyev.anton.guesstherace.ui.rating.RatingFragment
import skubyev.anton.guesstherace.ui.settings.SettingsFragment
import toothpick.Toothpick
import javax.inject.Inject

class MainActivity : BaseActivity(), LaunchView {

    @Inject lateinit var navigationHolder: NavigatorHolder
    @Inject lateinit var menuController: GlobalMenuController

    private var menuStateDisposable: Disposable? = null

    override val layoutRes = R.layout.activity_main

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private var bound = false
    private var musicService: MusicService? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, binder: IBinder) {
            val binder = binder as MusicService.MusicBinder
            musicService = binder.service
            bound = true
        }
        override fun onServiceDisconnected(name: ComponentName) {
            bound = false
        }
    }

    @InjectPresenter lateinit var presenter: LaunchPresenter

    @ProvidePresenter
    fun providePresenter(): LaunchPresenter {
        return Toothpick
                .openScopes(DI.DATA_SCOPE)
                .getInstance(LaunchPresenter::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)

        Toothpick.openScopes(DI.DATA_SCOPE, DI.MAIN_ACTIVITY_SCOPE).apply {
            installModules(MainActivityModule())
            Toothpick.inject(this@MainActivity, this)
        }

        initFirebaseAnalytics()

        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        presenter.playMusic()
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        menuStateDisposable = menuController.state.subscribe { openNavDrawer(it) }
        navigationHolder.setNavigator(navigator)
    }

    override fun onPause() {
        menuStateDisposable?.dispose()
        navigationHolder.removeNavigator()
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        if (bound) {
            musicService?.pauseMusic()
            unbindService(connection)
            bound = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (isFinishing) {
            Toothpick.closeScope(DI.MAIN_ACTIVITY_SCOPE)
            stopService(intent)
        }
    }

    private fun initFirebaseAnalytics() {
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
    }

    override fun startMusic() {
        val intent = Intent(this, MusicService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
            bindService(intent, connection, BIND_NOT_FOREGROUND)
        } else {
            startService(intent)
            bindService(intent, connection, BIND_NOT_FOREGROUND)
        }
        musicService?.playMusic()
    }

    private val navigator = object : SupportAppNavigator(this, R.id.mainContainer) {

        override fun createActivityIntent(screenKey: String?, data: Any?): Intent? = when (screenKey) {
            Screens.AUTH_SCREEN -> Intent(this@MainActivity, AuthActivity::class.java)
            else -> null
        }

        override fun applyCommand(command: Command?) {
            super.applyCommand(command)
            updateNavDrawer()
        }

        override fun createFragment(screenKey: String?, data: Any?): Fragment? = when (screenKey) {
            Screens.MAIN_SCREEN -> HomeFragment()
            Screens.SETTINGS_SCREEN -> SettingsFragment()
            Screens.FEEDBACK_SCREEN -> FeedbackFragment()
            Screens.COMMENTS_SCREEN -> CommentsFragment.createNewInstance(data as Int)
            Screens.RATING_SCREEN -> RatingFragment()
            Screens.NOTIFICATIONS_SCREEN -> NotificationsFragment()
            Screens.ABOUT_SCREEN -> AboutFragment()
            Screens.BROWSER_SCREEN -> BrowserFragment.createNewInstance(data as String)
            else -> null
        }

        override fun setupFragmentTransactionAnimation(command: Command?, currentFragment: Fragment?, nextFragment: Fragment?, fragmentTransaction: FragmentTransaction?) {
            if (command is Forward) {
                if (nextFragment !is HomeFragment) {
                    fragmentTransaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                }
            } else if (command is Back) {
                if (currentFragment !is HomeFragment) {
                    fragmentTransaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                }
            }
        }
    }

    //region nav drawer
    private fun openNavDrawer(open: Boolean) {
        drawerLayout.postDelayed({
            if (open) drawerLayout.openDrawer(GravityCompat.START)
            else drawerLayout.closeDrawer(GravityCompat.START)
        }, 150)
    }

    private fun enableNavDrawer(enable: Boolean) {
        drawerLayout.setDrawerLockMode(
                if (enable) DrawerLayout.LOCK_MODE_UNLOCKED
                else DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                GravityCompat.START
        )
    }

    private fun updateNavDrawer() {
        supportFragmentManager.executePendingTransactions()

        val drawerFragment = supportFragmentManager.findFragmentById(R.id.navigationDrawer) as NavigationDrawerFragment
        supportFragmentManager.findFragmentById(R.id.mainContainer)?.let {
            when (it) {
                    is HomeFragment -> drawerFragment.onScreenChanged(NavigationDrawerView.MenuItem.HOME)
                    is SettingsFragment -> drawerFragment.onScreenChanged(NavigationDrawerView.MenuItem.SETTINGS)
                    is FeedbackFragment -> drawerFragment.onScreenChanged(NavigationDrawerView.MenuItem.FEEDBACK)
                    is RatingFragment -> drawerFragment.onScreenChanged(NavigationDrawerView.MenuItem.RATING)
                    is AboutFragment -> drawerFragment.onScreenChanged(NavigationDrawerView.MenuItem.ABOUT)
                }
                enableNavDrawer(isNavDrawerAvailableForFragment(it))
            }
    }

    private fun isNavDrawerAvailableForFragment(currentFragment: Fragment) = when (currentFragment) {
        is HomeFragment -> true
        is RatingFragment -> true
        is SettingsFragment -> false
        is FeedbackFragment -> false
        is AboutFragment -> false
        is BrowserFragment -> false
        else -> false
    }
    //end region

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            openNavDrawer(false)
        } else {
            val fragment = supportFragmentManager.findFragmentById(R.id.mainContainer)
            if (fragment is BaseFragment) {
                fragment.onBackPressed()
            } else {
                presenter.onBackPressed()
            }
        }
    }
}