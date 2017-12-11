package skubyev.anton.guesstherace.ui.comments

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.item_comment.view.*
import skubyev.anton.guesstherace.R
import skubyev.anton.guesstherace.entity.CommentResponse
import skubyev.anton.guesstherace.extension.inflate
import skubyev.anton.guesstherace.ui.global.list.ListItem

class CommentsAdapterDelegate : AdapterDelegate<MutableList<ListItem>>() {
    override fun isForViewType(items: MutableList<ListItem>, position: Int) =
            items[position] is ListItem.CommentItem

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_comment))

    override fun onBindViewHolder(items: MutableList<ListItem>,
                                  position: Int,
                                  viewHolder: RecyclerView.ViewHolder,
                                  payloads: MutableList<Any>) =
            (viewHolder as ViewHolder).bind((items[position] as ListItem.CommentItem).comment)

    private class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var comment: CommentResponse

        fun bind(comment: CommentResponse) {
            this.comment = comment

            view.authorTV.text = comment.name
            view.commentTV.text = comment.comment
            view.dateTV.text = comment.date
        }
    }
}