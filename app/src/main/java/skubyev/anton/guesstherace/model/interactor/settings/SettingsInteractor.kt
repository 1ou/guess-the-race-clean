package skubyev.anton.guesstherace.model.interactor.settings

import skubyev.anton.guesstherace.model.repository.settings.SettingsRepository
import skubyev.anton.guesstherace.model.repository.watchedimages.WatchedImagesRepository
import javax.inject.Inject

class SettingsInteractor @Inject constructor(
        private val watchedImagesRepository: WatchedImagesRepository,
        private val settingsRepository: SettingsRepository
) {
    fun clearWatchedImages() = watchedImagesRepository.clearWatchedImages()

    fun isShowTraining() = settingsRepository.isShowTraining()

    fun setIsShowTraining(isShowTraining: Boolean) = settingsRepository.setShowTraining(isShowTraining)
}