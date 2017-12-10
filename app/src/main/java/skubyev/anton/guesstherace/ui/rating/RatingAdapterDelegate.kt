package skubyev.anton.guesstherace.ui.rating

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.item_rating.view.*
import skubyev.anton.guesstherace.R
import skubyev.anton.guesstherace.entity.RatingResponse
import skubyev.anton.guesstherace.extension.inflate
import skubyev.anton.guesstherace.ui.global.list.ListItem

class RatingAdapterDelegate : AdapterDelegate<MutableList<ListItem>>() {
    override fun isForViewType(items: MutableList<ListItem>, position: Int) =
            items[position] is ListItem.RatingItem

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_rating))

    override fun onBindViewHolder(items: MutableList<ListItem>,
                                  position: Int,
                                  viewHolder: RecyclerView.ViewHolder,
                                  payloads: MutableList<Any>) =
            (viewHolder as ViewHolder).bind((items[position] as ListItem.RatingItem).rating)

    private class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var rating: RatingResponse

        fun bind(rating: RatingResponse) {
            this.rating = rating

            view.nameTV.text = rating.name
            view.placeTV.text = rating.place.toString()
            view.ratingTV.text = rating.rating.toString()
        }
    }
}