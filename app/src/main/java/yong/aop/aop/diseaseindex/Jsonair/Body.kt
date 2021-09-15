package yong.aop.aop.diseaseindex.Jsonair

import com.google.gson.annotations.SerializedName

data class Body(
    @SerializedName("items")
    val airInfos: List<AirInfo>? = null,
    @SerializedName("numOfRows")
    val numOfRows: Int? = null,
    @SerializedName("pageNo")
    val pageNo: Int? = null,
    @SerializedName("totalCount")
    val totalCount: Int? = null
)