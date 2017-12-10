package skubyev.anton.guesstherace.entity

import com.google.gson.annotations.SerializedName

data class CommentResponse(
        @SerializedName("id") val id: Int,
        @SerializedName("ImageId") val idImage: Int,
        @SerializedName("comment") val comment: String,
        @SerializedName("idAuthor") val idAuthor: Int,
        @SerializedName("date") val date: String,
        @SerializedName("name") val name: String
)

