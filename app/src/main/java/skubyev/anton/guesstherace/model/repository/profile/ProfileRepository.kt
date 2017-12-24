package skubyev.anton.guesstherace.model.repository.profile

import skubyev.anton.guesstherace.model.data.server.GeneralApi
import skubyev.anton.guesstherace.model.system.SchedulersProvider
import javax.inject.Inject

class ProfileRepository @Inject constructor(
        private val api: GeneralApi,
        private val schedulers: SchedulersProvider
) {
    fun getProfile() = api.getProfile()
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun appendRating(
            state: Boolean
    ) = api.appendRating(state)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
}