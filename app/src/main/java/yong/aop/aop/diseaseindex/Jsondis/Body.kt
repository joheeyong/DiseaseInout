package yong.aop.aop.diseaseindex.Jsondis

import com.google.gson.annotations.SerializedName

data class Body(
    @SerializedName("items")
    val disInfos: List<DisInfo>? = null,
    @SerializedName("numOfRows")
    val numOfRows: Int? = null,
    @SerializedName("pageNo")
    val pageNo: Int? = null,
    @SerializedName("totalCount")
    val totalCount: Int? = null
)