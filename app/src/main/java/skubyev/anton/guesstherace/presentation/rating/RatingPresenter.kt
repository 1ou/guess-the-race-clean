package skubyev.anton.guesstherace.presentation.rating

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import skubyev.anton.guesstherace.extension.addTo
import skubyev.anton.guesstherace.model.interactor.rating.RatingInteractor
import skubyev.anton.guesstherace.model.system.ResourceManager
import skubyev.anton.guesstherace.presentation.global.ErrorHandler
import skubyev.anton.guesstherace.presentation.global.GlobalMenuController
import javax.inject.Inject

@InjectViewState
class RatingPresenter @Inject constructor(
        private val router: Router,
        private val ratingInteractor: RatingInteractor,
        private val errorHandler: ErrorHandler,
        private val resourceManager: ResourceManager,
        private val menuController: GlobalMenuController
) : MvpPresenter<RatingView>() {

    private val compositeDisposable = CompositeDisposable()

    override fun onFirstViewAttach() {
        ratingInteractor.getRating()
                .doOnSuccess { rating ->
                    if (rating.isEmpty()) viewState.showEmptyView()
                    else viewState.showRating(rating)
                }
                .doOnSubscribe { viewState.showProgress(true) }
                .doAfterTerminate { viewState.showProgress(false) }
                .subscribe(
                        { },
                        { errorHandler.proceed(it, { viewState.showMessage(it) }) }
                )
                .addTo(compositeDisposable)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
    }

    fun onMenuPressed() = menuController.open()
}