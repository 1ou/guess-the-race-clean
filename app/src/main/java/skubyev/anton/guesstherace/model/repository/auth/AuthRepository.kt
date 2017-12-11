package skubyev.anton.guesstherace.model.repository.auth

import skubyev.anton.guesstherace.model.data.auth.AuthHolder
import skubyev.anton.guesstherace.model.data.server.GeneralApi
import skubyev.anton.guesstherace.model.system.SchedulersProvider
import javax.inject.Inject

class AuthRepository @Inject constructor(
        private val authData: AuthHolder,
        private val api: GeneralApi,
        private val schedulers: SchedulersProvider
) {
    fun isSignedIn() = authData.token != ""

    fun requestToken(token: String) = api
            .auth(token)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun registration(login: String) = api
            .registration(login)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun testLogin(login: String) = api
            .testLogin(login)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun saveAuthData(
            token: String,
            isAuthToken: Boolean,
            idUser: Int
    ) {
        authData.token = token
        authData.isToken = isAuthToken
        authData.idUser = idUser
    }

    fun token() = authData.token

    fun idUser() = authData.idUser

    fun clearAuthData() {
        authData.token = ""
    }
}