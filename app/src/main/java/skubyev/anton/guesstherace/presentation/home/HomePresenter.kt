package skubyev.anton.guesstherace.presentation.home

import android.os.Handler
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import skubyev.anton.guesstherace.Screens
import skubyev.anton.guesstherace.extension.addTo
import skubyev.anton.guesstherace.extension.random
import skubyev.anton.guesstherace.extension.subscribeIgnoreResult
import skubyev.anton.guesstherace.model.data.storage.Image
import skubyev.anton.guesstherace.model.interactor.image.ImageInteractor
import skubyev.anton.guesstherace.model.interactor.image.ImagesOverError
import skubyev.anton.guesstherace.model.interactor.notifications.NotificationsInteractor
import skubyev.anton.guesstherace.model.interactor.profile.ProfileInteractor
import skubyev.anton.guesstherace.model.interactor.settings.SettingsInteractor
import skubyev.anton.guesstherace.presentation.global.ErrorHandler
import skubyev.anton.guesstherace.presentation.global.GlobalMenuController
import javax.inject.Inject

@InjectViewState
class HomePresenter @Inject constructor(
        private val router: Router,
        private val menuController: GlobalMenuController,
        private val imageInteractor: ImageInteractor,
        private val notificationsInteractor: NotificationsInteractor,
        private val profileInteractor: ProfileInteractor,
        private val settingsInteractor: SettingsInteractor,
        private val errorHandler: ErrorHandler
) : MvpPresenter<HomeView>() {

    private val compositeDisposable = CompositeDisposable()

    private lateinit var currentImage: Image

    override fun onFirstViewAttach() {
        loadImage()

        if (settingsInteractor.isShowTraining()) {
            viewState.showTraining()
            settingsInteractor.setShowTraining(false)
        }
    }

    fun loadNotifications() = notificationsInteractor.getNotifications()
            .map { it.filter { it.show } }
            .doOnSuccess { viewState.showNotifications(it.size) }
            .subscribeIgnoreResult()

    override fun onDestroy() {
        compositeDisposable.dispose()
    }

    private fun loadImage() = imageInteractor.getImage()
            .doOnError { viewState.showImagesOverInfo() }
            .doOnSubscribe { viewState.showProgress(true) }
            .doAfterTerminate { viewState.showProgress(false) }
            .subscribe(
                    {
                        currentImage = it
                        viewState.showImage(it)
                        showRateDialog()
                    },
                    {
                        if (it is ImagesOverError) viewState.showImagesOverInfo()
                        else errorHandler.proceed(it, { viewState.showMessage(it) })
                    }
            )
            .addTo(compositeDisposable)

    fun clickedButton(answer: String) {
        viewState.showAnswer(
                currentImage.urlAnswer,
                currentImage.race == answer
        )
        imageInteractor.saveWatchedImage(currentImage).subscribe({}, {}).addTo(compositeDisposable)
        loadNextImage()
    }

    private fun loadNextImage() =
            Handler().postDelayed({
                loadImage()
            }, 3000)

    fun appendRating(state: Boolean) = profileInteractor.appendRating(state).subscribe({}, {})
            .addTo(compositeDisposable)

    fun isShowAdv() = false // (0..100).random() > 100

    private fun showRateDialog() {
        if ((0..100).random() > 95 && settingsInteractor.isShowRate()) {
            viewState.showRateDialog()
        }
    }

    fun changeStateShowRate(state: Boolean) = settingsInteractor.setShowRate(state)

    fun clickedDiscuss() = router.navigateTo(Screens.COMMENTS_SCREEN, currentImage.idImage)

    fun clickedNotifications() = router.navigateTo(Screens.NOTIFICATIONS_SCREEN)

    fun clickedSettings() = router.navigateTo(Screens.SETTINGS_SCREEN)

    fun onBackPressed() = router.finishChain()

    fun onMenuPressed() = menuController.open()
}