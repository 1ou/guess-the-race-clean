package skubyev.anton.guesstherace.presentation.home

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import skubyev.anton.guesstherace.model.data.storage.Image

@StateStrategyType(AddToEndSingleStrategy::class)
interface HomeView : MvpView {
    fun showImage(image: Image)
    fun showAnswer(url: String, state: Boolean)
    fun showProgress(show: Boolean)
    fun showNotifications(count: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}