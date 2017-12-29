package skubyev.anton.guesstherace.model.repository.settings

import skubyev.anton.guesstherace.model.data.interfaces.SettingsHolder
import javax.inject.Inject

class SettingsRepository @Inject constructor(
        private val settingsData: SettingsHolder
) {
    fun isShowTraining() = settingsData.isShowTraining

    fun setShowTraining(isShowTraining: Boolean) {
        settingsData.isShowTraining = isShowTraining
    }

    fun isPlayMusic() = settingsData.isPlayMusic

    fun setPlayMusic(isPlayMusic: Boolean) {
        settingsData.isPlayMusic = isPlayMusic
    }

    fun isShowRate() = settingsData.isShowRate

    fun setShowRate(isShowRate: Boolean) {
        settingsData.isShowRate = isShowRate
    }

    fun clear() {
        settingsData.isShowRate = true
        settingsData.isPlayMusic = false
        settingsData.isShowTraining = true
    }
}