package skubyev.anton.guesstherace.model.interactor.rating

import skubyev.anton.guesstherace.model.repository.rating.RatingRepository
import java.text.DecimalFormat
import javax.inject.Inject

class RatingInteractor @Inject constructor(
        private val ratingRepository: RatingRepository
) {
    fun getRating() = ratingRepository.getUsersRating()
}