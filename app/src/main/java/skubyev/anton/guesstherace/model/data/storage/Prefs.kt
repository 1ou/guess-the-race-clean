package skubyev.anton.guesstherace.model.data.storage

import android.content.Context
import skubyev.anton.guesstherace.model.data.auth.AuthHolder
import javax.inject.Inject

class Prefs @Inject constructor(
        private val context: Context
) : AuthHolder {
    private val AUTH_DATA = "auth_data"
    private val KEY_TOKEN = "ad_token"
    private val KEY_IS_EXISTS = "ad_is_exists"
    private val ID_USER = "id_user"

    private fun getSharedPreferences(prefsName: String)
            = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)

    override var token: String
        get() = getSharedPreferences(AUTH_DATA).getString(KEY_TOKEN, "")
        set(value) {
            getSharedPreferences(AUTH_DATA).edit().putString(KEY_TOKEN, value).apply()
        }

    override var isToken: Boolean
        get() = getSharedPreferences(AUTH_DATA).getBoolean(KEY_IS_EXISTS, false)
        set(value) {
            getSharedPreferences(AUTH_DATA).edit().putBoolean(KEY_IS_EXISTS, value).apply()
        }

    override var idUser: Int
        get() =  getSharedPreferences(AUTH_DATA).getInt(ID_USER, -1)
        set(value) {
            getSharedPreferences(AUTH_DATA).edit().putInt(ID_USER, value).apply()
        }
}