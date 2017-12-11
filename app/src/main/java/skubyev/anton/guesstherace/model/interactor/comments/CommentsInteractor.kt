package skubyev.anton.guesstherace.model.interactor.comments

import skubyev.anton.guesstherace.model.repository.comments.CommentsRepository
import javax.inject.Inject

class CommentsInteractor @Inject constructor(
        private val commentsRepository: CommentsRepository
) {
    fun getComments(token: String, idImage: Int) = commentsRepository.getComments(token, idImage)

    fun addComment(
            token: String,
            message: String,
            idImage: Int,
            idAuthor: Int
    ) = commentsRepository.addComment(
            token,
            message,
            idImage,
            idAuthor
    )
}