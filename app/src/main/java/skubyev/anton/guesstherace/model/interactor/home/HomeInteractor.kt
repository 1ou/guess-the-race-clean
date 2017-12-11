package skubyev.anton.guesstherace.model.interactor.home

import skubyev.anton.guesstherace.extension.random
import skubyev.anton.guesstherace.model.data.storage.Image
import skubyev.anton.guesstherace.model.repository.images.ImagesRepository
import skubyev.anton.guesstherace.model.repository.watchedimages.WatchedImagesRepository
import javax.inject.Inject

class HomeInteractor @Inject constructor(
        private val imagesRepository: ImagesRepository,
        private val watchedImagesRepository: WatchedImagesRepository
) {

    fun getImage(token: String) = watchedImagesRepository.getWatchedImages()
            .map { it.map { it.idImage } }
            .flatMap { return@flatMap imagesRepository.getImage(token, it) }

    fun saveWatchedImage(
            image: Image
    ) = watchedImagesRepository.addWatchedImage(
            image
    )

    fun isShowAdv() = (0..100).random() > 93
}