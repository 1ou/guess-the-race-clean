package skubyev.anton.guesstherace.model.interactor.rating

import skubyev.anton.guesstherace.R
import skubyev.anton.guesstherace.entity.Rank
import skubyev.anton.guesstherace.model.repository.rating.RatingRepository
import skubyev.anton.guesstherace.model.system.ResourceManager
import javax.inject.Inject

class RatingInteractor @Inject constructor(
        private val ratingRepository: RatingRepository,
        private val resourceManager: ResourceManager
) {
    fun getRating() = ratingRepository.getUsersRating()

    fun getRank(value: Int): Rank = when(value) {
        in 0..20 -> Rank(resourceManager.getString(R.string.rang_1), "http://wooa.ru/android/determinace/backend/rank/rank1.jpg")
        in 21..40 -> Rank(resourceManager.getString(R.string.rang_2), "http://wooa.ru/android/determinace/backend/rank/rank2.jpg")
        in 41..60 -> Rank(resourceManager.getString(R.string.rang_3), "http://wooa.ru/android/determinace/backend/rank/rank3.jpg")
        in 61..80 -> Rank(resourceManager.getString(R.string.rang_4), "http://wooa.ru/android/determinace/backend/rank/rank4.jpg")
        in 81..100 -> Rank(resourceManager.getString(R.string.rang_5), "http://wooa.ru/android/determinace/backend/rank/rank5.jpg")
        else -> Rank(resourceManager.getString(R.string.rang_1), "http://wooa.ru/android/determinace/backend/rank/rank1.jpg")
    }
}