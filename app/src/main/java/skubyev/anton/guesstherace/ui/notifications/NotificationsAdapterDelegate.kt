package skubyev.anton.guesstherace.ui.notifications

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.item_notification.view.*
import skubyev.anton.guesstherace.R
import skubyev.anton.guesstherace.entity.Notification
import skubyev.anton.guesstherace.extension.inflate
import skubyev.anton.guesstherace.ui.global.list.ListItem

class NotificationsAdapterDelegate(private val initListener: (Notification) -> Unit) : AdapterDelegate<MutableList<ListItem>>() {
    override fun isForViewType(items: MutableList<ListItem>, position: Int) =
            items[position] is ListItem.NotificationItem

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_notification), initListener)

    override fun onBindViewHolder(items: MutableList<ListItem>,
                                  position: Int,
                                  viewHolder: RecyclerView.ViewHolder,
                                  payloads: MutableList<Any>) =
            (viewHolder as ViewHolder).bind((items[position] as ListItem.NotificationItem).notification)

    private class ViewHolder(
            val view: View,
            val clickListener: (Notification) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private lateinit var notification: Notification

        fun bind(notification: Notification) {
            this.notification = notification

            notification.show = false
            clickListener.invoke(notification)

            view.authorTV.text = notification.title
            view.commentTV.text = notification.message
        }
    }
}