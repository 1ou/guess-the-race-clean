package skubyev.anton.guesstherace.presentation.notifications

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import skubyev.anton.guesstherace.entity.Notification
import skubyev.anton.guesstherace.extension.addTo
import skubyev.anton.guesstherace.extension.subscribeIgnoreResult
import skubyev.anton.guesstherace.model.interactor.notifications.NotificationsInteractor
import skubyev.anton.guesstherace.model.system.ResourceManager
import skubyev.anton.guesstherace.presentation.global.ErrorHandler
import javax.inject.Inject

@InjectViewState
class NotificationsPresenter @Inject constructor(
        private val router: Router,
        private val notificationsInteractor: NotificationsInteractor,
        private val errorHandler: ErrorHandler,
        private val resourceManager: ResourceManager
) : MvpPresenter<NotificationsView>() {

    private val compositeDisposable = CompositeDisposable()

    override fun onFirstViewAttach() {
        loadNotifications()
    }

    fun loadNotifications() = notificationsInteractor.getNotifications()
            .doOnSuccess { notifications ->
                if (notifications.isEmpty()) viewState.showEmptyView()
                else viewState.showNotifications(notifications)
            }
            .doOnSubscribe { viewState.showProgress(true) }
            .doAfterTerminate { viewState.showProgress(false) }
            .subscribe(
                    { },
                    { errorHandler.proceed(it, { viewState.showMessage(it) }) }
            )
            .addTo(compositeDisposable)

    override fun onDestroy() {
        compositeDisposable.dispose()
    }

    fun notificationWasShowed(notification: Notification) = notificationsInteractor.updateNotification(notification)
            .subscribeIgnoreResult()

    fun onBackPressed() = router.exit()
}