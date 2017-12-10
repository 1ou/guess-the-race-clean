package skubyev.anton.guesstherace.presentation.auth

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface AuthView : MvpView {
    fun showProgress(isVisible: Boolean)
    fun showErrorDialog()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}