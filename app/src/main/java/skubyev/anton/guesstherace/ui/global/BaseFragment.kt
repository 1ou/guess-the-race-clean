package skubyev.anton.guesstherace.ui.global

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView

abstract class BaseFragment : MvpAppCompatFragment() {
    companion object {
        private val PROGRESS_TAG = "bf_progress"
    }

    abstract val layoutRes: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            = inflater.inflate(layoutRes, container, false)

    protected fun showProgressDialog(progress: Boolean) {
        val fragment = childFragmentManager.findFragmentByTag(PROGRESS_TAG)
        if (fragment != null && !progress) {
            (fragment as ProgressDialog).dismissAllowingStateLoss()
            childFragmentManager.executePendingTransactions()
        } else if (fragment == null && progress) {
            ProgressDialog().show(childFragmentManager, PROGRESS_TAG)
            childFragmentManager.executePendingTransactions()
        }
    }

    protected fun showSnackMessage(message: String) {
        view?.let {
            val snackbar = Snackbar.make(it, message, Snackbar.LENGTH_LONG)
            val messageTextView = snackbar.view.findViewById<TextView>(android.support.design.R.id.snackbar_text)
            messageTextView.setTextColor(Color.WHITE)
            snackbar.show()
        }
    }

    protected fun showToastMessage(message: String) {
        view?.let {
            Toast.makeText(it.context, message, Toast.LENGTH_LONG).show()
        }
    }

    private lateinit var presenter: MvpPresenter<MvpView>
    open fun onBackPressed() = Unit
}