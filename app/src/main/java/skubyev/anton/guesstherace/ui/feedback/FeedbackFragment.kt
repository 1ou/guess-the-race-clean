package skubyev.anton.guesstherace.ui.feedback

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.animation.OvershootInterpolator
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import skubyev.anton.guesstherace.ui.global.BaseFragment
import skubyev.anton.guesstherace.ui.global.InfoDialog
import kotlinx.android.synthetic.main.fragment_feedback.*
import skubyev.anton.guesstherace.R
import skubyev.anton.guesstherace.presentation.feedback.FeedbackPresenter
import skubyev.anton.guesstherace.presentation.feedback.FeedbackView
import skubyev.anton.guesstherace.toothpick.DI
import toothpick.Toothpick

class FeedbackFragment : BaseFragment(), FeedbackView {

    private companion object {
        private val PREVIEW_TAG = "preview_tag"
    }

    override val layoutRes = R.layout.fragment_feedback

    @InjectPresenter lateinit var presenter: FeedbackPresenter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.setNavigationOnClickListener { presenter.onBackPressed() }

        rateApp.setOnClickListener { launchMarket() }

        expandableLayout.setInterpolator(OvershootInterpolator())
        expandButton.isSelected = false
        expandableLayout.collapse(false)

        expandButton.setOnClickListener {
            expandButton.isSelected = !expandButton.isSelected
            if (expandButton.isSelected) expandableLayout.expand() else expandableLayout.collapse()
        }

        buttonPreview.setOnClickListener {
            var msg = "Text:\n" + feedbackText.text.toString() + "\n\n"
            if (infoSystem.isChecked) msg += "System information:\n" + presenter.getDeviceInfo()

            InfoDialog
                    .newInstants(
                            msg = msg,
                            title = getString(R.string.preview_button)
                    )
                    .show(childFragmentManager, PREVIEW_TAG)
        }

        buttonSend.setOnClickListener {
            var msg = feedbackText.text.toString()
            if (infoSystem.isChecked) msg += "\n\nSystem information:\n" + presenter.getDeviceInfo()

            val mailto = "mailto:antonskubiiev@gmail.com" +
                    "&subject=" + Uri.encode("Feedback for Guess The Race Android App") +
                    "&body=" + Uri.encode(msg)

            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.data = Uri.parse(mailto)

            try {
                startActivity(emailIntent)
            } catch (e: ActivityNotFoundException) {
            }
        }
    }

    override fun onBackPressed() = presenter.onBackPressed()

    private fun launchMarket() {
        val uri = Uri.parse("market://details?id=" + context?.packageName)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            showSnackMessage("Error")
        }
    }

    @ProvidePresenter
    fun providePresenter(): FeedbackPresenter {
        return Toothpick.openScopes(DI.MAIN_ACTIVITY_SCOPE)
                .getInstance(FeedbackPresenter::class.java)
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }
}