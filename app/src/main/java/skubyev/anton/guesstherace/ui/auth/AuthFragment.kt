package skubyev.anton.guesstherace.ui.auth

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_auth.*
import skubyev.anton.guesstherace.R
import skubyev.anton.guesstherace.presentation.auth.AuthPresenter
import skubyev.anton.guesstherace.presentation.auth.AuthView
import skubyev.anton.guesstherace.toothpick.DI
import skubyev.anton.guesstherace.ui.global.BaseFragment
import skubyev.anton.guesstherace.ui.global.InfoDialog
import toothpick.Toothpick

class AuthFragment : BaseFragment(), AuthView {

    private companion object {
        private val INFO_TAG = "info_tag"
    }

    override val layoutRes = R.layout.fragment_auth

    private val minLogin = 5

    @InjectPresenter lateinit var presenter: AuthPresenter

    @ProvidePresenter
    fun providePresenter(): AuthPresenter {
        return Toothpick
                .openScope(DI.DATA_SCOPE)
                .getInstance(AuthPresenter::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        okBTN.setOnClickListener {
            val login = loginTV.text.toString()
            if (login.length > minLogin) {
                presenter.testLogin(login)
            } else {
                showSnackMessage(getString(R.string.error_login_length))
            }
        }

        loginTV.setOnClickListener {
            if (loginTV.text.toString() == getString(R.string.name)) {
                loginTV.setText("")
            }
        }
    }

    override fun showErrorDialog() {
        InfoDialog
                .newInstants(
                        msg = getString(R.string.error_login),
                        title = getString(R.string.error)
                )
                .show(childFragmentManager, INFO_TAG)
    }

    override fun showProgress(isVisible: Boolean) {
        showProgressDialog(isVisible)
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    override fun onBackPressed() = presenter.onBackPressed()
}