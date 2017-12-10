package skubyev.anton.guesstherace.presentation.global

import ru.terrakok.cicerone.Router
import skubyev.anton.guesstherace.Screens
import skubyev.anton.guesstherace.extension.userMessage
import skubyev.anton.guesstherace.model.data.server.ServerError
import skubyev.anton.guesstherace.model.interactor.auth.AuthInteractor
import skubyev.anton.guesstherace.model.system.ResourceManager
import timber.log.Timber
import javax.inject.Inject

class ErrorHandler @Inject constructor(
        private val router: Router,
        private val authInteractor: AuthInteractor,
        private val resourceManager: ResourceManager
) {

    fun proceed(error: Throwable, messageListener: (String) -> Unit = {}) {
        Timber.e("Error: $error")
        if (error is ServerError) {
            when (error.errorCode) {
                401 -> {
                    authInteractor.logout()
                    router.newRootScreen(Screens.AUTH_SCREEN)
                }
            }
        } else {
            messageListener.invoke(error.userMessage(resourceManager))
        }
    }
}
