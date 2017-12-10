package skubyev.anton.guesstherace.ui.comments

import android.os.Bundle
import android.os.Parcelable
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import kotlinx.android.synthetic.main.fragment_comments.*
import kotlinx.android.synthetic.main.layout_list_with_update.*
import skubyev.anton.guesstherace.R
import skubyev.anton.guesstherace.entity.CommentResponse
import skubyev.anton.guesstherace.extension.visible
import skubyev.anton.guesstherace.presentation.comments.CommentsPresenter
import skubyev.anton.guesstherace.presentation.comments.CommentsView
import skubyev.anton.guesstherace.toothpick.DI
import skubyev.anton.guesstherace.toothpick.PrimitiveWrapper
import skubyev.anton.guesstherace.toothpick.qualifier.ImageId
import skubyev.anton.guesstherace.ui.global.BaseFragment
import skubyev.anton.guesstherace.ui.global.EditDialog
import skubyev.anton.guesstherace.ui.global.list.ListItem
import skubyev.anton.guesstherace.ui.global.list.ProgressAdapterDelegate
import toothpick.Toothpick
import toothpick.config.Module

class CommentsFragment : BaseFragment(), CommentsView, EditDialog.OnClickListener{

    companion object {
        private const val ARG_IMAGE_ID = "image id"
        private const val ADD_COMMENT_TAG = "add_comment_tag"

        fun createNewInstance(data: Int) = CommentsFragment().apply {
            arguments = Bundle().also {
                it.putInt(ARG_IMAGE_ID, data)
            }
        }
    }

    override val layoutRes = R.layout.fragment_comments

    private val commentsAdapter = CommentsAdapter()

    private var listState: Parcelable? = null

    @InjectPresenter lateinit var presenter: CommentsPresenter

    @ProvidePresenter
    fun createPresenter(): CommentsPresenter {
        val scopeName = "comments scope"
        val scope = Toothpick.openScopes(DI.MAIN_ACTIVITY_SCOPE, scopeName)
        scope.installModules(object : Module() {
            init {
                bind(PrimitiveWrapper::class.java)
                        .withName(ImageId::class.java)
                        .toInstance(PrimitiveWrapper(arguments?.getInt(ARG_IMAGE_ID)))
            }
        })

        return scope.getInstance(CommentsPresenter::class.java).also {
            Toothpick.closeScope(scopeName)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.setNavigationOnClickListener { presenter.onBackPressed() }

        listState = savedInstanceState?.getParcelable("ListState")

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = commentsAdapter
        }

        addCommentBTN.setOnClickListener {
            EditDialog
                    .newInstants(
                            tag = ADD_COMMENT_TAG,
                            title = getString(R.string.add_comment),
                            positive = getString(R.string.ok),
                            negative = getString(R.string.cancel),
                            text = ""
                    )
                    .show(childFragmentManager, ADD_COMMENT_TAG)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (recyclerView != null) {
            outState.putParcelable("ListState", recyclerView.layoutManager.onSaveInstanceState())
        }
    }

    override val dialogEdit: (tag: String, msg: String) -> Unit = { tag, msg ->
        when (tag) {
            ADD_COMMENT_TAG -> {
                presenter.addComment(msg)
            }
            else -> showMessage(getString(R.string.error_choose))
        }
    }

    override fun showEmptyView() {
        empty.visible(true)
    }

    override fun showComments(comments: List<CommentResponse>) {
        empty.visible(false)
        recyclerView.post { commentsAdapter.setData(comments) }
    }

    override fun showProgress(show: Boolean) {
        showProgressDialog(show)
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    inner class CommentsAdapter : ListDelegationAdapter<MutableList<ListItem>>() {
        init {
            items = mutableListOf()
            delegatesManager.addDelegate(CommentsAdapterDelegate())
            delegatesManager.addDelegate(ProgressAdapterDelegate())
        }

        fun setData(comments: List<CommentResponse>) {
            val progress = isProgress()

            items.clear()
            items.addAll(comments.map { ListItem.CommentItem(it) })
            if (progress) items.add(ListItem.ProgressItem())

            notifyDataSetChanged()

            recyclerView.layoutManager.onRestoreInstanceState(listState)
        }

        fun showProgress(isVisible: Boolean) {
            val currentProgress = isProgress()

            if (isVisible && !currentProgress) items.add(ListItem.ProgressItem())
            else if (!isVisible && currentProgress) items.remove(items.last())

            notifyDataSetChanged()
        }

        private fun isProgress() = items.isNotEmpty() && items.last() is ListItem.ProgressItem
    }

    override fun onBackPressed() = presenter.onBackPressed()
}