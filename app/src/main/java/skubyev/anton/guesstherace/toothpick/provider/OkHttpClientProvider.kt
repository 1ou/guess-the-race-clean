package skubyev.anton.guesstherace.toothpick.provider

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import skubyev.anton.guesstherace.BuildConfig
import skubyev.anton.guesstherace.model.data.auth.AuthHolder
import skubyev.anton.guesstherace.model.data.server.interceptor.AuthHeaderInterceptor
import skubyev.anton.guesstherace.model.data.server.interceptor.CurlLoggingInterceptor
import skubyev.anton.guesstherace.model.data.server.interceptor.ErrorResponseInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider

class OkHttpClientProvider @Inject constructor(
        authData: AuthHolder
) : Provider<OkHttpClient> {
    private val httpClient: OkHttpClient

    init {
        val httpClientBuilder = OkHttpClient.Builder()

//        httpClientBuilder.addNetworkInterceptor(AuthHeaderInterceptor(authData))
        httpClientBuilder.addNetworkInterceptor(ErrorResponseInterceptor())
        httpClientBuilder.readTimeout(30, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClientBuilder.addNetworkInterceptor(httpLoggingInterceptor)
            httpClientBuilder.addNetworkInterceptor(CurlLoggingInterceptor())
        }
        httpClient = httpClientBuilder.build()
    }

    override fun get() = httpClient
}