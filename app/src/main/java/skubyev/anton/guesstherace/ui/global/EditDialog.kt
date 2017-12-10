package skubyev.anton.guesstherace.ui.global

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.dialog_edit.*
import skubyev.anton.guesstherace.R

class EditDialog : DialogFragment() {
    private var clickListener: OnClickListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(context!!, R.style.AppCompatAlertDialogStyle).apply {
            setView(LayoutInflater.from(context).inflate(R.layout.dialog_edit, null))
            setCancelable(true)
            setTitle(arguments?.getString(TITLE))

            setPositiveButton(getString(R.string.save)) { _, _ ->
                returnEditText(dialog)
            }
            setNegativeButton(getString(R.string.cancel)) { _, _ ->
                dismiss()
            }
        }.show()

        dialog.edit.setText(arguments?.getString(TEXT))
        return dialog
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        clickListener = when {
            parentFragment is OnClickListener -> parentFragment as OnClickListener
            activity is OnClickListener -> activity as OnClickListener
            else -> null
        }
    }

    private fun returnEditText(dialog: Dialog) {
        clickListener?.dialogEdit?.invoke(
                arguments?.getString(TAG).toString(),
                dialog.edit.text.toString()
        )
    }

    override fun onDetach() {
        clickListener = null
        super.onDetach()
    }

    companion object {
        private const val TITLE = "title"
        private const val TAG = "tag"
        private const val POSITIVE_TEXT = "positive_text"
        private const val NEGATIVE_TEXT = "negative_text"
        private const val TEXT = "text"

        fun newInstants(
                title: String? = null,
                positive: String? = null,
                negative: String? = null,
                tag: String,
                text: String
        ) =
                EditDialog().apply {
                    arguments = Bundle().apply {
                        putString(TITLE, title)
                        putString(POSITIVE_TEXT, positive)
                        putString(NEGATIVE_TEXT, negative)
                        putString(TAG, tag)
                        putString(TEXT, text)
                    }
                }
    }

    interface OnClickListener {
        val dialogEdit: (tag: String, text: String) -> Unit
    }
}