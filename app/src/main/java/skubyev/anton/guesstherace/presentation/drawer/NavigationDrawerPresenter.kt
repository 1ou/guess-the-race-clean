package skubyev.anton.guesstherace.presentation.drawer

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import skubyev.anton.guesstherace.Screens
import skubyev.anton.guesstherace.model.interactor.auth.AuthInteractor
import skubyev.anton.guesstherace.model.interactor.settings.SettingsInteractor
import skubyev.anton.guesstherace.presentation.drawer.NavigationDrawerView.MenuItem
import skubyev.anton.guesstherace.presentation.drawer.NavigationDrawerView.MenuItem.*
import skubyev.anton.guesstherace.presentation.global.ErrorHandler
import skubyev.anton.guesstherace.presentation.global.GlobalMenuController
import javax.inject.Inject

@InjectViewState
class NavigationDrawerPresenter @Inject constructor(
        private val router: Router,
        private val menuController: GlobalMenuController,
        private val authInteractor: AuthInteractor,
        private val settingsInteractor: SettingsInteractor
) : MvpPresenter<NavigationDrawerView>() {

    private var currentSelectedItem: MenuItem? = null
    private val compositeDisposable = CompositeDisposable()

    override fun onFirstViewAttach() {
        viewState.showGreeting(authInteractor.userName())
    }

    fun onScreenChanged(item: MenuItem) {
        menuController.close()
        currentSelectedItem = item
        viewState.selectMenuItem(item)
    }

    fun onMenuItemClick(item: MenuItem) {
        menuController.close()
        if (item != currentSelectedItem) {
            when (item) {
                HOME -> router.newRootScreen(Screens.MAIN_SCREEN)
                SETTINGS -> router.navigateTo(Screens.SETTINGS_SCREEN)
                FEEDBACK -> router.navigateTo(Screens.FEEDBACK_SCREEN)
                RATING -> router.navigateTo(Screens.RATING_SCREEN)
                ABOUT -> router.navigateTo(Screens.ABOUT_SCREEN)
                BROWSER -> router.navigateTo(Screens.BROWSER_SCREEN)
                else -> TODO()
            }
        }
    }

    fun onLogoutClick() {
        menuController.close()
        authInteractor.logout()
        settingsInteractor.clear()
        router.newRootScreen(Screens.AUTH_SCREEN)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
    }
}