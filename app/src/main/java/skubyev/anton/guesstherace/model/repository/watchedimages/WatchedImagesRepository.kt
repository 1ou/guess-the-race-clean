package skubyev.anton.guesstherace.model.repository.watchedimages

import skubyev.anton.guesstherace.model.data.database.PersistentDatabase
import skubyev.anton.guesstherace.model.data.storage.Image
import skubyev.anton.guesstherace.model.data.storage.WatchedImageEntity
import skubyev.anton.guesstherace.model.mapper.WatchedImagesEntityMapper
import skubyev.anton.guesstherace.toothpick.qualifier.database.WatchedImageQualifier
import javax.inject.Inject

class WatchedImagesRepository @Inject constructor(
        @WatchedImageQualifier private var database: PersistentDatabase<WatchedImageEntity>
) {
    private val mapper = WatchedImagesEntityMapper()

    fun addWatchedImage(image: Image) = database.addResult(mapper.invoke(image))

    fun getWatchedImages() = database.getAllResults()

    fun clearWatchedImages() = database.removeAllResults()
}