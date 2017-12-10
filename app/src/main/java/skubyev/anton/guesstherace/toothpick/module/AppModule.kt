package skubyev.anton.guesstherace.toothpick.module

import android.content.Context
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import skubyev.anton.guesstherace.BuildConfig
import skubyev.anton.guesstherace.model.data.auth.AuthHolder
import skubyev.anton.guesstherace.model.data.storage.Prefs
import skubyev.anton.guesstherace.model.system.AppSchedulers
import skubyev.anton.guesstherace.model.system.ResourceManager
import skubyev.anton.guesstherace.model.system.SchedulersProvider
import skubyev.anton.guesstherace.toothpick.PrimitiveWrapper
import skubyev.anton.guesstherace.toothpick.qualifier.DefaultPageSize
import skubyev.anton.guesstherace.toothpick.qualifier.DefaultServerPath
import toothpick.config.Module

class AppModule(context: Context) : Module() {
    init {
        //Global
        bind(Context::class.java).toInstance(context)
        bind(String::class.java).withName(DefaultServerPath::class.java).toInstance(BuildConfig.ORIGIN_API_ENDPOINT)
        bind(PrimitiveWrapper::class.java).withName(DefaultPageSize::class.java).toInstance(PrimitiveWrapper(10))
        bind(SchedulersProvider::class.java).toInstance(AppSchedulers())
        bind(ResourceManager::class.java).singletonInScope()

        //Navigation
        val cicerone = Cicerone.create()
        bind(Router::class.java).toInstance(cicerone.router)
        bind(NavigatorHolder::class.java).toInstance(cicerone.navigatorHolder)

        //Auth
        bind(AuthHolder::class.java).to(Prefs::class.java).singletonInScope()
    }
}