package skubyev.anton.guesstherace.model.interactor.image

import skubyev.anton.guesstherace.model.data.storage.Image
import skubyev.anton.guesstherace.model.repository.images.ImagesRepository
import skubyev.anton.guesstherace.model.repository.watchedimages.WatchedImagesRepository
import javax.inject.Inject

class ImageInteractor @Inject constructor(
        private val imagesRepository: ImagesRepository,
        private val watchedImagesRepository: WatchedImagesRepository
) {

    fun getImage() = watchedImagesRepository.getWatchedImages()
            .map { it.map { it.idImage } }
            .flatMap {
                val imagesSize = imagesRepository.getImagesSize()
                if (imagesSize != it.size || imagesSize == 0) {
                    return@flatMap imagesRepository.getImage(it)
                } else {
                    throw ImagesOverError()
                }
            }

    fun saveWatchedImage(
            image: Image
    ) = watchedImagesRepository.addWatchedImage(
            image
    )
}