package yong.aop.aop.diseaseindex.Jsonair

import com.google.gson.annotations.SerializedName

data class AirQualityResponse(
    @SerializedName("response")
    val response: Response? = null
)
