package yong.aop.aop.diseaseindex.Json2

import com.google.gson.annotations.SerializedName

data class AirQualityResponse(
    @SerializedName("response")
    val response: Response? = null
)
