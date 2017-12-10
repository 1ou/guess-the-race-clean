package skubyev.anton.guesstherace.model.repository.watchedimages

import skubyev.anton.guesstherace.model.data.database.PersistentDatabase
import skubyev.anton.guesstherace.model.data.storage.Image
import skubyev.anton.guesstherace.model.data.storage.WatchedImage
import skubyev.anton.guesstherace.toothpick.qualifier.database.WatchedImageQualifier
import javax.inject.Inject

class WatchedImagesRepository @Inject constructor(
        @WatchedImageQualifier private var database: PersistentDatabase<WatchedImage>
) {
    fun addWatchedImage(image: Image) = database.addResult(WatchedImage(image.idImage))

    fun getWatchedImages() = database.getAllResults()

    fun clearWatchedImages() = database.removeAllResults()
}