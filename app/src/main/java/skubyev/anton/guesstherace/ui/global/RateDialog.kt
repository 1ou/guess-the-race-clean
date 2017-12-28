package skubyev.anton.guesstherace.ui.global

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.dialog_rate.*
import skubyev.anton.guesstherace.R

class RateDialog : DialogFragment() {
    private var clickListener: OnClickListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(context!!, R.style.AppCompatAlertDialogStyle).apply {
            setView(LayoutInflater.from(context).inflate(R.layout.dialog_rate, null))
            setCancelable(true)
            setTitle(arguments?.getString(TITLE))
        }.show()

        dialog.ratingBar.setOnRatingBarChangeListener { _, fl, _ ->
            if (fl > 3.5) {
                clickListener?.dialogRate?.invoke(
                        arguments?.getString(TAG).toString()
                )
            }
            dismiss()
        }

        return dialog
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        clickListener = when {
            parentFragment is RateDialog.OnClickListener -> parentFragment as OnClickListener
            activity is RateDialog.OnClickListener -> activity as OnClickListener
            else -> null
        }
    }

    override fun onDetach() {
        clickListener = null
        super.onDetach()
    }

    companion object {
        private const val TITLE = "title"
        private const val TAG = "tag"
        private const val TEXT = "text"

        fun newInstants(
                title: String? = null,
                text: String,
                tag: String
        ) =
                RateDialog().apply {
                    arguments = Bundle().apply {
                        putString(TITLE, title)
                        putString(TEXT, text)
                        putString(TAG, tag)
                    }
                }
    }

    interface OnClickListener {
        val dialogRate: (tag: String) -> Unit
    }
}