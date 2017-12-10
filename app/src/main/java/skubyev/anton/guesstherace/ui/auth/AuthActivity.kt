package skubyev.anton.guesstherace.ui.auth

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.SupportAppNavigator
import ru.terrakok.cicerone.commands.Replace
import skubyev.anton.guesstherace.R
import skubyev.anton.guesstherace.Screens
import skubyev.anton.guesstherace.toothpick.DI
import skubyev.anton.guesstherace.ui.global.BaseActivity
import skubyev.anton.guesstherace.ui.launch.MainActivity
import toothpick.Toothpick
import javax.inject.Inject

class AuthActivity : BaseActivity() {
    @Inject lateinit var navigationHolder: NavigatorHolder

    override val layoutRes = R.layout.activity_container

    override fun onCreate(savedInstanceState: Bundle?) {
        Toothpick.inject(this, Toothpick.openScope(DI.APP_SCOPE))
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            navigator.applyCommand(Replace(Screens.AUTH_SCREEN, null))
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigationHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigationHolder.removeNavigator()
        super.onPause()
    }

    private val navigator = object : SupportAppNavigator(this, R.id.container) {

        override fun createActivityIntent(screenKey: String?, data: Any?): Intent? = when (screenKey) {
            Screens.MAIN_SCREEN -> Intent(this@AuthActivity, MainActivity::class.java)
            else -> null
        }

        override fun createFragment(screenKey: String?, data: Any?): Fragment? = when (screenKey) {
            Screens.AUTH_SCREEN -> AuthFragment()
            else -> null
        }
    }
}