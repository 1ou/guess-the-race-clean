package skubyev.anton.guesstherace.model.data.storage

import android.content.Context
import skubyev.anton.guesstherace.model.data.interfaces.SettingsHolder
import javax.inject.Inject

class SettingsPrefs @Inject constructor(
        private val context: Context
) : SettingsHolder {
    private val SETTINGS_DATA = "auth_data"
    private val IS_SHOW_TRAINING = "is_show_training"
    private val IS_PLAY_MUSIC = "is_play_music"

    private fun getSharedPreferences(prefsName: String)
            = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)

    override var isShowTraining: Boolean
        get() = getSharedPreferences(SETTINGS_DATA).getBoolean(IS_SHOW_TRAINING, false)
        set(value) {
            getSharedPreferences(SETTINGS_DATA).edit().putBoolean(IS_SHOW_TRAINING, value).apply()
        }

    override var isPlayMusic: Boolean
        get() = getSharedPreferences(SETTINGS_DATA).getBoolean(IS_PLAY_MUSIC, true)
        set(value) {
            getSharedPreferences(SETTINGS_DATA).edit().putBoolean(IS_PLAY_MUSIC, value).apply()
        }
}