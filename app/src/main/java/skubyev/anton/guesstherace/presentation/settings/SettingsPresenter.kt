package skubyev.anton.guesstherace.presentation.settings

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import skubyev.anton.guesstherace.R
import skubyev.anton.guesstherace.extension.subscribeIgnoreResult
import skubyev.anton.guesstherace.model.interactor.settings.SettingsInteractor
import skubyev.anton.guesstherace.model.system.ResourceManager
import skubyev.anton.guesstherace.presentation.global.ErrorHandler
import javax.inject.Inject

@InjectViewState
class SettingsPresenter @Inject constructor(
        private val router: Router,
        private val settingsInteractor: SettingsInteractor,
        private val errorHandler: ErrorHandler,
        private val resourceManager: ResourceManager
) : MvpPresenter<SettingsView>() {

    private val compositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        compositeDisposable.dispose()
    }

    fun clearFavorite() = settingsInteractor.clearWatchedImages()
            .doOnComplete {
                viewState.showMessage(resourceManager.getString(R.string.remove_success))
            }
            .doOnError {
                viewState.showMessage(resourceManager.getString(R.string.remove_error))
            }
            .subscribeIgnoreResult()

    fun onBackPressed() = router.exit()
}