package yong.aop.aop.diseaseindex.Json

import com.google.gson.annotations.SerializedName

data class Header(
    @SerializedName("resultCode")
    val resultCode: Int? = null,
    @SerializedName("resultMsg")
    val resultMsg: String? = null,
    @SerializedName("type")
    val type: String? = null
)