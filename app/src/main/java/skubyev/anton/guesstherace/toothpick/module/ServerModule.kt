package skubyev.anton.guesstherace.toothpick.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import skubyev.anton.guesstherace.model.data.server.GeneralApi
import skubyev.anton.guesstherace.model.interactor.auth.AuthInteractor
import skubyev.anton.guesstherace.model.interactor.comments.CommentsInteractor
import skubyev.anton.guesstherace.model.interactor.image.ImageInteractor
import skubyev.anton.guesstherace.model.interactor.notifications.NotificationsInteractor
import skubyev.anton.guesstherace.model.interactor.profile.ProfileInteractor
import skubyev.anton.guesstherace.model.interactor.rating.RatingInteractor
import skubyev.anton.guesstherace.model.interactor.settings.SettingsInteractor
import skubyev.anton.guesstherace.model.repository.auth.AuthRepository
import skubyev.anton.guesstherace.model.repository.comments.CommentsRepository
import skubyev.anton.guesstherace.model.repository.images.ImagesRepository
import skubyev.anton.guesstherace.model.repository.notifications.NotificationsRepository
import skubyev.anton.guesstherace.model.repository.profile.ProfileRepository
import skubyev.anton.guesstherace.model.repository.rating.RatingRepository
import skubyev.anton.guesstherace.model.repository.watchedimages.WatchedImagesRepository
import skubyev.anton.guesstherace.presentation.global.ErrorHandler
import skubyev.anton.guesstherace.toothpick.provider.ApiProvider
import skubyev.anton.guesstherace.toothpick.provider.OkHttpClientProvider
import skubyev.anton.guesstherace.toothpick.qualifier.ServerPath
import toothpick.config.Module

class ServerModule(serverUrl: String) : Module() {
    init {
        //Network
        bind(String::class.java).withName(ServerPath::class.java).toInstance(serverUrl)
        bind(Gson::class.java).toInstance(GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create())
        bind(OkHttpClient::class.java).toProvider(OkHttpClientProvider::class.java).singletonInScope()
        bind(GeneralApi::class.java).toProvider(ApiProvider::class.java).singletonInScope()

        //Auth
        bind(AuthRepository::class.java).singletonInScope()
        bind(AuthInteractor::class.java).singletonInScope()

        //Error handler with logout logic
        bind(ErrorHandler::class.java)

        //Images
        //Watched images
        bind(ImagesRepository::class.java)
        bind(WatchedImagesRepository::class.java)
        bind(ImageInteractor::class.java)

        //Rating
        bind(RatingRepository::class.java)
        bind(RatingInteractor::class.java)

        //Comments
        bind(CommentsRepository::class.java)
        bind(CommentsInteractor::class.java)

        //Profile
        bind(ProfileRepository::class.java)
        bind(ProfileInteractor::class.java)

        //Settings
        bind(SettingsInteractor::class.java)

        //Notifications
        bind(NotificationsRepository::class.java)
        bind(NotificationsInteractor::class.java)
    }
}