package skubyev.anton.guesstherace.presentation.notifications

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import skubyev.anton.guesstherace.entity.Notification

@StateStrategyType(AddToEndSingleStrategy::class)
interface NotificationsView : MvpView {
    fun showEmptyView()
    fun showNotifications(notifications: List<Notification>)
    fun showProgress(show: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}