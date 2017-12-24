package skubyev.anton.guesstherace.model.interactor.profile

import skubyev.anton.guesstherace.model.repository.profile.ProfileRepository
import javax.inject.Inject

class ProfileInteractor @Inject constructor(
        private val profileRepository: ProfileRepository
) {
    fun getProfile() = profileRepository.getProfile()

    fun appendRating(
            state: Boolean
    ) = profileRepository.appendRating(
            state
    )
}