package skubyev.anton.guesstherace.ui.global

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import skubyev.anton.guesstherace.R

class InfoDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(context!!, R.style.AppCompatAlertDialogStyle).apply {
                arguments?.getString(TITLE)?.let { setTitle(it) }
                setMessage(arguments?.getString(MSG))
                setPositiveButton(arguments?.getString(POSITIVE_TEXT) ?: getString(R.string.ok)) { _, _ ->
                    dismiss()
                }
            }.create()

    companion object {
        private const val TITLE = "title"
        private const val MSG = "msg"
        private const val POSITIVE_TEXT = "positive_text"

        fun newInstants(
                title: String? = null,
                msg: String,
                positiveText: String? = null
        ) =
                InfoDialog().apply {
                    arguments = Bundle().apply {
                        putString(TITLE, title)
                        putString(MSG, msg)
                        putString(POSITIVE_TEXT, positiveText)
                    }
                }
    }
}