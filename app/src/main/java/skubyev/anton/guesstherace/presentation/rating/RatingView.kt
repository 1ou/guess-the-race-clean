package skubyev.anton.guesstherace.presentation.rating

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import skubyev.anton.guesstherace.entity.RatingResponse

@StateStrategyType(AddToEndSingleStrategy::class)
interface RatingView : MvpView {
    fun showEmptyView()
    fun showRating(rating: List<RatingResponse>)
    fun showProgress(show: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}