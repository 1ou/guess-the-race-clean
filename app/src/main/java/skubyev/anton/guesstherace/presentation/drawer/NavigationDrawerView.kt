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
        FEEDBACK,
        ABOUT,
        BROWSER
    }

    fun selectMenuItem(item: MenuItem)

    fun showGreeting(userName: String)
}