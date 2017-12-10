package skubyev.anton.guesstherace.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class BaseResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean = false
}