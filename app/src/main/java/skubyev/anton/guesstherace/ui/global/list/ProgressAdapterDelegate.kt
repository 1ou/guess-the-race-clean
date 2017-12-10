package skubyev.anton.guesstherace.ui.global.list

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.View
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import skubyev.anton.guesstherace.R
import skubyev.anton.guesstherace.extension.inflate

class ProgressAdapterDelegate : AdapterDelegate<MutableList<ListItem>>() {

    override fun isForViewType(items: MutableList<ListItem>, position: Int) =
            items[position] is ListItem.ProgressItem

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            ProgressViewHolder(parent.inflate(R.layout.item_progress))

    override fun onBindViewHolder(items: MutableList<ListItem>,
                                  position: Int,
                                  viewHolder: RecyclerView.ViewHolder,
                                  payloads: MutableList<Any>) {}

    private class ProgressViewHolder(view: View) : RecyclerView.ViewHolder(view)
}