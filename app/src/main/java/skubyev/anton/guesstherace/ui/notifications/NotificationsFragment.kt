package skubyev.anton.guesstherace.ui.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.layout_list_with_update.*
import skubyev.anton.guesstherace.R
import skubyev.anton.guesstherace.entity.Notification
import skubyev.anton.guesstherace.extension.visible
import skubyev.anton.guesstherace.presentation.notifications.NotificationsPresenter
import skubyev.anton.guesstherace.presentation.notifications.NotificationsView
import skubyev.anton.guesstherace.toothpick.DI
import skubyev.anton.guesstherace.ui.global.BaseFragment
import skubyev.anton.guesstherace.ui.global.list.ListItem
import skubyev.anton.guesstherace.ui.global.list.ProgressAdapterDelegate
import toothpick.Toothpick

class NotificationsFragment : BaseFragment(), NotificationsView {

    override val layoutRes = R.layout.fragment_notifications

    private val ratingAdapter = NotificationsAdapter()

    private var listState: Parcelable? = null

    @InjectPresenter lateinit var presenter: NotificationsPresenter

    @ProvidePresenter
    fun providePresenter(): NotificationsPresenter {
        return Toothpick.openScopes(DI.DATA_SCOPE)
                .getInstance(NotificationsPresenter::class.java)
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

    private val messageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            val state = intent.getBooleanExtra("message", false)

            if (state) {
                presenter.loadNotifications()
            }
        }
    }

    override fun onResume() {
        LocalBroadcastManager.getInstance(context!!)
                .registerReceiver(
                        messageReceiver,
                        IntentFilter("notification")
                )

        super.onResume()
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(context!!)
                .unregisterReceiver(messageReceiver)

        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (recyclerView != null) {
            outState.putParcelable("ListState", recyclerView.layoutManager.onSaveInstanceState())
        }
    }

    override fun showEmptyView() {
        empty.visible(true)
    }

    override fun showNotifications(notifications: List<Notification>) {
        empty.visible(false)
        recyclerView.post { ratingAdapter.setData(notifications) }
    }

    override fun showProgress(show: Boolean) {
        showProgressDialog(show)
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    inner class NotificationsAdapter : ListDelegationAdapter<MutableList<ListItem>>() {
        init {
            items = mutableListOf()
            delegatesManager.addDelegate(NotificationsAdapterDelegate({
                presenter.notificationWasShowed(it)
            }))
            delegatesManager.addDelegate(ProgressAdapterDelegate())
        }

        fun setData(rating: List<Notification>) {
            val progress = isProgress()

            items.clear()
            items.addAll(rating.map { ListItem.NotificationItem(it) })
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