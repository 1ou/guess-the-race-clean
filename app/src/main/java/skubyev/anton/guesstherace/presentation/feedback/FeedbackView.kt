package skubyev.anton.guesstherace.presentation.feedback

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface FeedbackView : MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}