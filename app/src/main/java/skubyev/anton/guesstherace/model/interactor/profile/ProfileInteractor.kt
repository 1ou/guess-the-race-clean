package skubyev.anton.guesstherace.model.interactor.profile

import skubyev.anton.guesstherace.model.repository.profile.ProfileRepository
import javax.inject.Inject

class ProfileInteractor @Inject constructor(
        private val profileRepository: ProfileRepository
) {
    fun getProfile(token: String) = profileRepository.getProfile(token)

    fun appendRating(
            token: String,
            state: Boolean
    ) = profileRepository.appendRating(
            token,
            state
    )
}