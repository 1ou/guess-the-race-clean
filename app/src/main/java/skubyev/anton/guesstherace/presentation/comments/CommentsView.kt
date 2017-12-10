package skubyev.anton.guesstherace.presentation.comments

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import skubyev.anton.guesstherace.entity.CommentResponse

@StateStrategyType(AddToEndSingleStrategy::class)
interface CommentsView : MvpView {
    fun showEmptyView()
    fun showComments(comments: List<CommentResponse>)
    fun showProgress(show: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}