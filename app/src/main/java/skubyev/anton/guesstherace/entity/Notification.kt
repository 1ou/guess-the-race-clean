package skubyev.anton.guesstherace.entity

data class Notification constructor(
    var id: Int,
    var title: String,
    var message: String,
    var show: Boolean
)