package skubyev.anton.guesstherace.presentation.comments

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import skubyev.anton.guesstherace.R
import skubyev.anton.guesstherace.extension.addTo
import skubyev.anton.guesstherace.model.interactor.auth.AuthInteractor
import skubyev.anton.guesstherace.model.interactor.comments.CommentsInteractor
import skubyev.anton.guesstherace.model.system.ResourceManager
import skubyev.anton.guesstherace.presentation.global.ErrorHandler
import skubyev.anton.guesstherace.toothpick.PrimitiveWrapper
import skubyev.anton.guesstherace.toothpick.qualifier.ImageId
import javax.inject.Inject

@InjectViewState
class CommentsPresenter @Inject constructor(
        @ImageId private val idImageWrapper: PrimitiveWrapper<Int>,
        private val router: Router,
        private val commentsInteractor: CommentsInteractor,
        private val authInteractor: AuthInteractor,
        private val errorHandler: ErrorHandler,
        private val resourceManager: ResourceManager
) : MvpPresenter<CommentsView>() {

    private val idImage = idImageWrapper.value

    private val compositeDisposable = CompositeDisposable()

    override fun onFirstViewAttach() {
        updateComments()
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
    }

    fun updateComments() = commentsInteractor.getComments(idImage)
            .doOnSuccess { comments ->
                if (comments.isEmpty()) viewState.showEmptyView()
                else viewState.showComments(comments)
            }
            .doOnSubscribe { viewState.showProgress(true) }
            .doAfterTerminate { viewState.showProgress(false) }
            .subscribe(
                    { },
                    { errorHandler.proceed(it, { viewState.showMessage(it) }) }
            )
            .addTo(compositeDisposable)

    fun addComment(msg: String) = commentsInteractor.addComment(
            msg,
            idImage,
            authInteractor.idUser()
    )
            .doOnSuccess { state ->
                if (state.success) {
                    viewState.showMessage(resourceManager.getString(R.string.success_add_comment))
                    updateComments()
                }
                else viewState.showMessage(resourceManager.getString(R.string.fail_add_comment))
            }
            .doOnSubscribe { viewState.showProgress(true) }
            .doAfterTerminate { viewState.showProgress(false) }
            .subscribe(
                    { },
                    { errorHandler.proceed(it, { viewState.showMessage(it) }) }
            )
            .addTo(compositeDisposable)

    fun onBackPressed() = router.exit()
}