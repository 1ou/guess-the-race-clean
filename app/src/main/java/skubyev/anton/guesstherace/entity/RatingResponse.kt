package skubyev.anton.guesstherace.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class RatingResponse {

    @SerializedName("name")
    @Expose
    lateinit var name: String
    @SerializedName("guessed")
    @Expose
    var guessed: Int = 0
    @SerializedName("all_amount")
    @Expose
    var allAmount: Int = 0
    @SerializedName("rating")
    @Expose
    var rating: Double = 0.0
    @SerializedName("place")
    @Expose
    var place: Int = 0
}