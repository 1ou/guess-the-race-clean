package skubyev.anton.guesstherace.ui.global.list

import skubyev.anton.guesstherace.entity.CommentResponse
import skubyev.anton.guesstherace.entity.Notification
import skubyev.anton.guesstherace.entity.RatingResponse

sealed class ListItem {
    class ProgressItem : ListItem()
    class CommentItem(val comment: CommentResponse) : ListItem()
    class RatingItem(val rating: RatingResponse) : ListItem()
    class NotificationItem(val notification: Notification) : ListItem()
}