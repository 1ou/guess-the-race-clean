package skubyev.anton.guesstherace.model.interactor.comments

import skubyev.anton.guesstherace.model.repository.comments.CommentsRepository
import javax.inject.Inject

class CommentsInteractor @Inject constructor(
        private val commentsRepository: CommentsRepository
) {
    fun getComments(idImage: Int) = commentsRepository.getComments(idImage)

    fun addComment(
            message: String,
            idImage: Int,
            idAuthor: Int
    ) = commentsRepository.addComment(
            message,
            idImage,
            idAuthor
    )
}