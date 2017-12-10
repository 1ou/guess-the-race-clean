package skubyev.anton.guesstherace.model.repository.profile

import skubyev.anton.guesstherace.model.data.server.GeneralApi
import skubyev.anton.guesstherace.model.system.SchedulersProvider
import javax.inject.Inject

class ProfileRepository @Inject constructor(
        private val api: GeneralApi,
        private val schedulers: SchedulersProvider
) {
    fun getProfile(token: String) = api.getProfile(token)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun appendRating(
            token: String,
            state: Boolean
    ) = api.appendRating(token, state)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
}