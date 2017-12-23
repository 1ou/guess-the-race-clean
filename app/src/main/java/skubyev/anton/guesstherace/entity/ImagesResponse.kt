package skubyev.anton.guesstherace.entity

import com.google.gson.annotations.SerializedName

data class ImagesResponse(
        @SerializedName("id") val id: Int,
        @SerializedName("url") val url: String,
        @SerializedName("url_answer") val urlAnswer: String,
        @SerializedName("url_landscape") val urlLandscape: String,
        @SerializedName("url_answer_landscape") val urlAnswerLandscape: String,
        @SerializedName("race") val race: String
)

