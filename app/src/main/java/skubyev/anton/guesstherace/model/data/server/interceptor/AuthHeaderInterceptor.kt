package skubyev.anton.guesstherace.model.data.server.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import skubyev.anton.guesstherace.model.data.interfaces.AuthHolder

class AuthHeaderInterceptor(private val authData: AuthHolder) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (authData.token.isNotEmpty()) {
            request = request.newBuilder().addHeader("token", authData.token).build()
        }
        return chain.proceed(request)
    }
}