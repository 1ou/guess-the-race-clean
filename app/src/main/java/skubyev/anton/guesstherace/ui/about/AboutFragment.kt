package skubyev.anton.guesstherace.ui.about

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_settings.*
import skubyev.anton.guesstherace.R
import skubyev.anton.guesstherace.presentation.about.AboutPresenter
import skubyev.anton.guesstherace.presentation.about.AboutView
import skubyev.anton.guesstherace.toothpick.DI
import skubyev.anton.guesstherace.ui.global.BaseFragment
import toothpick.Toothpick

class AboutFragment : BaseFragment(), AboutView {

    override val layoutRes = R.layout.fragment_about

    @InjectPresenter lateinit var presenter: AboutPresenter

    @ProvidePresenter
    fun providePresenter(): AboutPresenter {
        return Toothpick.openScopes(DI.MAIN_ACTIVITY_SCOPE)
                .getInstance(AboutPresenter::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.setNavigationOnClickListener { presenter.onBackPressed() }
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    override fun onBackPressed() = presenter.onBackPressed()
}