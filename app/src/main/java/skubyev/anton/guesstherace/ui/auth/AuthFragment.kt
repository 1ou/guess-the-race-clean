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
        private const val INFO_TAG = "info_tag"
        private const val MIN_LOGIN_LENGTH = 3
    }

    override val layoutRes = R.layout.fragment_auth

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
            val login = loginEditText.text.toString()
            if (login.length > MIN_LOGIN_LENGTH) {
                presenter.testLogin(login)
            } else {
                showErrorDialog(getString(R.string.error_login_length))
            }
        }

        loginEditText.setOnFocusChangeListener { _, _ -> clearLoginEditText() }
    }

    private fun clearLoginEditText() {
        if (loginEditText.text.toString() == getString(R.string.name)) {
            loginEditText.setText("")
        }
    }

    override fun showErrorDialog(msg: String) {
        InfoDialog
                .newInstants(
                        msg = msg,
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