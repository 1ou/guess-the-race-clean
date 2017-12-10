package skubyev.anton.guesstherace.presentation.launch

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.terrakok.cicerone.Router
import skubyev.anton.guesstherace.Screens
import skubyev.anton.guesstherace.model.interactor.auth.AuthInteractor
import javax.inject.Inject

@InjectViewState
class LaunchPresenter @Inject constructor(
        private val router: Router,
        private val authInteractor: AuthInteractor
) : MvpPresenter<LaunchView>() {

    override fun onFirstViewAttach() {
        if (authInteractor.isSignedIn()) router.newRootScreen(Screens.MAIN_SCREEN)
        else router.newRootScreen(Screens.AUTH_SCREEN)
    }

    fun onBackPressed() = router.finishChain()
}