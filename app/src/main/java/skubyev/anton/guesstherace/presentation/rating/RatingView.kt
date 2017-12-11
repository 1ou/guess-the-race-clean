package skubyev.anton.guesstherace.presentation.rating

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import skubyev.anton.guesstherace.entity.Rank
import skubyev.anton.guesstherace.entity.RatingResponse

@StateStrategyType(AddToEndSingleStrategy::class)
interface RatingView : MvpView {
    fun showEmptyView()
    fun showRating(rating: List<RatingResponse>)
    fun showRacistValue(value: Int)
    fun showProgress(show: Boolean)
    fun showRank(rank: Rank)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}