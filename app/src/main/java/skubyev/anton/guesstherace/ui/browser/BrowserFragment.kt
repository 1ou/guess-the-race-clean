package skubyev.anton.guesstherace.ui.browser

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_browser.*
import skubyev.anton.guesstherace.R
import skubyev.anton.guesstherace.presentation.browser.BrowserPresenter
import skubyev.anton.guesstherace.presentation.browser.BrowserView
import skubyev.anton.guesstherace.toothpick.DI
import skubyev.anton.guesstherace.toothpick.qualifier.Link
import skubyev.anton.guesstherace.ui.global.BaseFragment
import toothpick.Toothpick
import toothpick.config.Module

class BrowserFragment : BaseFragment(), BrowserView {

    companion object {
        private const val ARG_LINK = "link"

        fun createNewInstance(data: String) = BrowserFragment().apply {
            arguments = Bundle().also {
                it.putString(ARG_LINK, data)
            }
        }
    }

    override val layoutRes = R.layout.fragment_browser

    @InjectPresenter lateinit var presenter: BrowserPresenter

    @ProvidePresenter
    fun createPresenter(): BrowserPresenter {
        val scopeName = "browser scope"
        val scope = Toothpick.openScopes(DI.MAIN_ACTIVITY_SCOPE, scopeName)
        scope.installModules(object : Module() {
            init {
                bind(String::class.java)
                        .withName(Link::class.java)
                        .toInstance(arguments?.getString(ARG_LINK))
            }
        })

        return scope.getInstance(BrowserPresenter::class.java).also {
            Toothpick.closeScope(scopeName)
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.setNavigationOnClickListener { presenter.onBackPressed() }

        webView.settings.apply {
            javaScriptEnabled = true
            setSupportZoom(true)
            useWideViewPort = true
            setAppCacheEnabled(true)
            loadWithOverviewMode = true
            builtInZoomControls = true
            displayZoomControls = false
        }
    }

    override fun showPage(link: String) {
        webView.loadUrl(link)
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    override fun onBackPressed() = presenter.onBackPressed()
}