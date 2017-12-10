package skubyev.anton.guesstherace.model.interactor.settings

import skubyev.anton.guesstherace.model.repository.watchedimages.WatchedImagesRepository
import javax.inject.Inject

class SettingsInteractor @Inject constructor(
        private val watchedImagesRepository: WatchedImagesRepository
) {
    fun clearWatchedImages() = watchedImagesRepository.clearWatchedImages()
}