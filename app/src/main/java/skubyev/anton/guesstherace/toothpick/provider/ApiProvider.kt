package skubyev.anton.guesstherace.toothpick.provider

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import skubyev.anton.guesstherace.model.data.server.GeneralApi
import skubyev.anton.guesstherace.toothpick.qualifier.ServerPath
import javax.inject.Inject
import javax.inject.Provider

class ApiProvider @Inject constructor(
        private val okHttpClient: OkHttpClient,
        private val gson: Gson,
        @ServerPath private val serverPath: String
) : Provider<GeneralApi> {

    override fun get() =
            Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .baseUrl(serverPath)
                    .build()
                    .create(GeneralApi::class.java)
}