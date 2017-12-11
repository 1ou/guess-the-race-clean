package skubyev.anton.guesstherace.model.data.server.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import skubyev.anton.guesstherace.model.data.auth.AuthHolder

class AuthHeaderInterceptor(private val authData: AuthHolder) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        authData.token.let {
            if (authData.isToken) {
                request = request.newBuilder().addHeader("Authorization", "Bearer " + it).build()
            } else {
                request = request.newBuilder().addHeader("PRIVATE-TOKEN", it).build()
            }
        }
        return chain.proceed(request)
    }
}