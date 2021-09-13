package yong.aop.aop.diseaseindex.Json

import com.google.gson.annotations.SerializedName

data class Body(
    @SerializedName("items")
    val nices: List<nice>? = null,
    @SerializedName("numOfRows")
    val numOfRows: Int? = null,
    @SerializedName("pageNo")
    val pageNo: Int? = null,
    @SerializedName("totalCount")
    val totalCount: Int? = null
)