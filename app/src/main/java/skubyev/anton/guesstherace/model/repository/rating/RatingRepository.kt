package skubyev.anton.guesstherace.model.repository.rating

import skubyev.anton.guesstherace.model.data.server.GeneralApi
import skubyev.anton.guesstherace.model.system.SchedulersProvider
import javax.inject.Inject

class RatingRepository @Inject constructor(
        private val api: GeneralApi,
        private val schedulers: SchedulersProvider
) {
    fun getUsersRating(token: String) = api.getUsersRating(token)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
}