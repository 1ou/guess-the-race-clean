package skubyev.anton.guesstherace.presentation.browser

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface BrowserView : MvpView {
    fun showPage(link: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}