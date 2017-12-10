package skubyev.anton.guesstherace.presentation.home

import android.os.Handler
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import skubyev.anton.guesstherace.R
import skubyev.anton.guesstherace.Screens
import skubyev.anton.guesstherace.extension.addTo
import skubyev.anton.guesstherace.extension.subscribeIgnoreResult
import skubyev.anton.guesstherace.model.data.storage.Image
import skubyev.anton.guesstherace.model.interactor.auth.AuthInteractor
import skubyev.anton.guesstherace.model.interactor.home.HomeInteractor
import skubyev.anton.guesstherace.model.interactor.notifications.NotificationsInteractor
import skubyev.anton.guesstherace.model.interactor.profile.ProfileInteractor
import skubyev.anton.guesstherace.model.system.ResourceManager
import skubyev.anton.guesstherace.presentation.global.ErrorHandler
import skubyev.anton.guesstherace.presentation.global.GlobalMenuController
import javax.inject.Inject

@InjectViewState
class HomePresenter @Inject constructor(
        private val router: Router,
        private val menuController: GlobalMenuController,
        private val homeInteractor: HomeInteractor,
        private val authInteractor: AuthInteractor,
        private val notificationsInteractor: NotificationsInteractor,
        private val profileInteractor: ProfileInteractor,
        private val errorHandler: ErrorHandler,
        private val resourceManager: ResourceManager
) : MvpPresenter<HomeView>() {

    private val compositeDisposable = CompositeDisposable()

    private lateinit var currentImage: Image

    override fun onFirstViewAttach() {
        loadImage()
    }

    fun loadNotifications() = notificationsInteractor.getNotifications()
            .map { it.filter { it.show } }
            .doOnSuccess { viewState.showNotifications(it.size) }
            .subscribeIgnoreResult()

    override fun onDestroy() {
        compositeDisposable.dispose()
    }

    private fun loadImage() {
        homeInteractor.getImage()
                .doOnSuccess { image ->
                    currentImage = image
                    viewState.showImage(image)
                }
                .doOnSubscribe { viewState.showProgress(true) }
                .doAfterTerminate { viewState.showProgress(false) }
                .subscribe(
                        { },
                        { errorHandler.proceed(it, { viewState.showMessage(it) }) }
                )
                .addTo(compositeDisposable)
    }

    fun clickedButton(answer: String) {
        viewState.showAnswer(
                currentImage.urlAnswer,
                currentImage.race == answer
        )
        homeInteractor.saveWatchedImage(currentImage).subscribeIgnoreResult()
        loadNextImage()
    }

    private fun loadNextImage() {
        Handler().postDelayed({
            loadImage()
        }, 3000)
    }

    fun appendRating(state: Boolean) {
        val token = authInteractor.token()
        if (token != null) {
            profileInteractor.appendRating(
                    token,
                    state
            ).subscribe(
                    { },
                    { errorHandler.proceed(it, { viewState.showMessage(it) }) }
            ).addTo(compositeDisposable)
        } else {
            viewState.showMessage(resourceManager.getString(R.string.you_not_auth))
        }
    }

    fun clickedDiscuss() = router.navigateTo(Screens.COMMENTS_SCREEN, currentImage.idImage)

    fun clickedNotifications() = router.navigateTo(Screens.NOTIFICATIONS_SCREEN)

    fun onBackPressed() = router.finishChain()

    fun onMenuPressed() = menuController.open()
}