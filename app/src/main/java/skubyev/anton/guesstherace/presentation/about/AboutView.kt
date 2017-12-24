package skubyev.anton.guesstherace.presentation.about

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface AboutView : MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}