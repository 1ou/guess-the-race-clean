package skubyev.anton.guesstherace.presentation.auth

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import skubyev.anton.guesstherace.R
import skubyev.anton.guesstherace.Screens
import skubyev.anton.guesstherace.extension.addTo
import skubyev.anton.guesstherace.extension.subscribeIgnoreResult
import skubyev.anton.guesstherace.model.interactor.auth.AuthInteractor
import skubyev.anton.guesstherace.model.system.ResourceManager
import skubyev.anton.guesstherace.presentation.global.ErrorHandler
import javax.inject.Inject

@InjectViewState
class AuthPresenter @Inject constructor(
        private val router: Router,
        private val authInteractor: AuthInteractor,
        private val errorHandler: ErrorHandler,
        private val resourceManager: ResourceManager
) : MvpPresenter<AuthView>() {

    private var compositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        compositeDisposable.dispose()
    }

    fun registration(login: String) = authInteractor
            .registration(login)
            .doOnSubscribe { viewState.showProgress(true) }
            .doAfterTerminate { viewState.showProgress(false) }
            .subscribe(
                    { router.newRootScreen(Screens.MAIN_SCREEN) },
                    { errorHandler.proceed(it, { viewState.showMessage(it) }) }
            ).addTo(compositeDisposable)

    fun testLogin(login: String) = authInteractor
            .testLogin(login)
            .doOnSuccess {
                if (it.success) registration(login)
                else viewState.showErrorDialog(resourceManager.getString(R.string.error_login))
            }
            .subscribe(
                    { },
                    { errorHandler.proceed(it, { viewState.showMessage(it) }) }
            ).addTo(compositeDisposable)

    fun onBackPressed() = router.exit()
}