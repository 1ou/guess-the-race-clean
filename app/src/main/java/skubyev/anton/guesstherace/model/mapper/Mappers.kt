package skubyev.anton.guesstherace.model.mapper

import skubyev.anton.guesstherace.entity.ImagesResponse
import skubyev.anton.guesstherace.entity.Notification
import skubyev.anton.guesstherace.model.data.storage.Image
import skubyev.anton.guesstherace.model.data.storage.NotificationEntity
import skubyev.anton.guesstherace.model.data.storage.WatchedImage
import skubyev.anton.guesstherace.model.data.storage.WatchedImageEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageMapper @Inject constructor() : (ImagesResponse) -> Image {
    override fun invoke(response: ImagesResponse) = Image(
            response.id,
            response.url,
            response.urlAnswer,
            response.urlLandscape,
            response.urlAnswerLandscape,
            response.race
    )
}

@Singleton
class NotificationMapper @Inject constructor() : (NotificationEntity) -> Notification {
    override fun invoke(response: NotificationEntity) = Notification(
            response.id,
            response.title,
            response.message,
            response.show
    )
}

@Singleton
class NotificationEntityMapper @Inject constructor() : (Notification) -> NotificationEntity {
    override fun invoke(response: Notification): NotificationEntity {
        val notification = NotificationEntity()
        notification.id = response.id
        notification.title = response.title
        notification.message = response.message
        notification.show = response.show
        return notification
    }
}

@Singleton
class WatchedImagesEntityMapper @Inject constructor() : (Image) -> WatchedImageEntity {
    override fun invoke(response: Image): WatchedImageEntity {
        val watchedImage = WatchedImageEntity()
        watchedImage.idImage = response.idImage
        return watchedImage
    }
}

fun <From, To> convertCollection(fromCollection: Collection<From>?, itemMapper: (From) -> To?): List<To> {
    val result = mutableListOf<To>()

    if (fromCollection != null) {
        for (fromItem in fromCollection) {
            itemMapper.invoke(fromItem)?.let {
                result.add(it)
            }
        }
    }

    return result
}

fun <From, To, Dependency> convertCollection(fromCollection: Collection<From>?,
                                             dependency: Dependency,
                                             itemMapper: (From, Dependency) -> To?): List<To> {
    val result = mutableListOf<To>()

    if (fromCollection != null) {
        for (fromItem in fromCollection) {
            itemMapper.invoke(fromItem, dependency)?.let {
                result.add(it)
            }
        }
    }

    return result
}

