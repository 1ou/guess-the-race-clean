package skubyev.anton.guesstherace.presentation.home

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import skubyev.anton.guesstherace.model.data.storage.Image

@StateStrategyType(AddToEndSingleStrategy::class)
interface HomeView : MvpView {
    fun showImage(image: Image)
    fun showProgress(show: Boolean)
    fun showNotifications(count: Int)
    fun showTraining()

    @StateStrategyType(SkipStrategy::class)
    fun showImagesOverInfo()

    @StateStrategyType(SkipStrategy::class)
    fun showAnswer(url: String, state: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}