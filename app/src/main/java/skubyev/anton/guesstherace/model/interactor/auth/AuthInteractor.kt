package skubyev.anton.guesstherace.model.interactor.auth

import io.reactivex.Completable
import skubyev.anton.guesstherace.model.repository.auth.AuthRepository
import javax.inject.Inject

class AuthInteractor @Inject constructor(
        private val authRepository: AuthRepository
) {
    fun isSignedIn() = authRepository.isSignedIn

    fun login() =
            Completable.defer {
                authRepository
                        .requestToken(
                                ""
                        )
                        .doOnSuccess {
                            authRepository.saveAuthData(it.token, true, it.id)
                        }
                        .toCompletable()
            }

    fun registration(login: String) = authRepository
            .registration(
                    login
            )
            .doOnSuccess {
                authRepository.saveAuthData(it.token, true, it.id)
            }

    fun testLogin(login: String) = authRepository
            .testLogin(
                    login
            )

    fun token() = authRepository.token()

    fun idUser() = authRepository.idUser()

    fun logout() = authRepository.clearAuthData()
}