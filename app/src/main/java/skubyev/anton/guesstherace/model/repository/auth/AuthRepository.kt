package skubyev.anton.guesstherace.model.repository.auth

import skubyev.anton.guesstherace.model.data.interfaces.AuthHolder
import skubyev.anton.guesstherace.model.data.server.GeneralApi
import skubyev.anton.guesstherace.model.system.SchedulersProvider
import javax.inject.Inject

class AuthRepository @Inject constructor(
        private val authData: AuthHolder,
        private val api: GeneralApi,
        private val schedulers: SchedulersProvider
) {
    val isSignedIn get() = authData.token != ""

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
            idUser: Int,
            userName: String
    ) {
        authData.token = token
        authData.isToken = isAuthToken
        authData.idUser = idUser
        authData.userName = userName
    }

    fun token() = authData.token

    fun idUser() = authData.idUser

    fun userName() = authData.userName

    fun clearAuthData() {
        authData.token = ""
        authData.userName = ""
        authData.idUser = 0
    }
}