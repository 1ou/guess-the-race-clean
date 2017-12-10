package skubyev.anton.guesstherace.presentation.drawer

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface NavigationDrawerView : MvpView {
    enum class MenuItem {
        HOME,
        COMMENTS,
        SETTINGS,
        RATING,
        FEEDBACK
    }

    fun selectMenuItem(item: MenuItem)
}