package skubyev.anton.guesstherace.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProfileResponse {
    @SerializedName("id")
    @Expose
    var id: Int = 0
    @SerializedName("name")
    @Expose
    lateinit var name: String
    @SerializedName("guessed")
    @Expose
    val guessed: Int = 0
    @SerializedName("all_amount")
    @Expose
    val allAmount: Int = 0
    @SerializedName("token")
    @Expose
    lateinit var token: String
    @SerializedName("rating")
    @Expose
    val rating: Int = 0
    @SerializedName("place")
    @Expose
    val place: Int = 0
}


