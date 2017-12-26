package skubyev.anton.guesstherace.presentation.browser

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import skubyev.anton.guesstherace.R
import skubyev.anton.guesstherace.extension.subscribeIgnoreResult
import skubyev.anton.guesstherace.model.interactor.settings.SettingsInteractor
import skubyev.anton.guesstherace.model.system.ResourceManager
import skubyev.anton.guesstherace.toothpick.qualifier.Link
import javax.inject.Inject

@InjectViewState
class BrowserPresenter @Inject constructor(
        private val router: Router,
        @Link private val link: String
) : MvpPresenter<BrowserView>() {

    override fun onFirstViewAttach() {
        viewState.showPage(link)
    }

    fun onBackPressed() = router.exit()
}