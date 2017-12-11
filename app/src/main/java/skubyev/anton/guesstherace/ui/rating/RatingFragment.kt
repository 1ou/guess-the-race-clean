package skubyev.anton.guesstherace.ui.rating

import android.os.Bundle
import android.os.Parcelable
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_view.view.*
import kotlinx.android.synthetic.main.fragment_rating.*
import kotlinx.android.synthetic.main.layout_list_with_update.*
import skubyev.anton.guesstherace.R
import skubyev.anton.guesstherace.entity.Rank
import skubyev.anton.guesstherace.entity.RatingResponse
import skubyev.anton.guesstherace.extension.visible
import skubyev.anton.guesstherace.presentation.rating.RatingPresenter
import skubyev.anton.guesstherace.presentation.rating.RatingView
import skubyev.anton.guesstherace.toothpick.DI
import skubyev.anton.guesstherace.ui.global.BaseFragment
import skubyev.anton.guesstherace.ui.global.list.ListItem
import skubyev.anton.guesstherace.ui.global.list.ProgressAdapterDelegate
import toothpick.Toothpick

class RatingFragment : BaseFragment(), RatingView {

    override val layoutRes = R.layout.fragment_rating

    private val ratingAdapter = RatingAdapter()

    private var listState: Parcelable? = null

    @InjectPresenter lateinit var presenter: RatingPresenter

    @ProvidePresenter
    fun providePresenter(): RatingPresenter {
        return Toothpick.openScopes(DI.MAIN_ACTIVITY_SCOPE)
                .getInstance(RatingPresenter::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.setNavigationOnClickListener { presenter.onMenuPressed() }

        listState = savedInstanceState?.getParcelable("ListState")

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = ratingAdapter
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (recyclerView != null) {
            outState.putParcelable("ListState", recyclerView.layoutManager.onSaveInstanceState())
        }
    }

    override fun showRank(rank: Rank) {
        Picasso.with(context).load(rank.url).into(rankIW)
        rankTV.text = rank.rank
    }

    override fun showEmptyView() {
        empty.visible(true)
    }

    override fun showRacistValue(value: Int) {
        racistPB.progress = value
    }

    override fun showRating(rating: List<RatingResponse>) {
        empty.visible(false)
        recyclerView.post { ratingAdapter.setData(rating) }
    }

    override fun showProgress(show: Boolean) {
        showProgressDialog(show)
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    inner class RatingAdapter : ListDelegationAdapter<MutableList<ListItem>>() {
        init {
            items = mutableListOf()
            delegatesManager.addDelegate(RatingAdapterDelegate())
            delegatesManager.addDelegate(ProgressAdapterDelegate())
        }

        fun setData(rating: List<RatingResponse>) {
            val progress = isProgress()

            items.clear()
            items.addAll(rating.map { ListItem.RatingItem(it) })
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
}