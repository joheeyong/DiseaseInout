package yong.aop.aop.diseaseindex.Jsondis

import com.google.gson.annotations.SerializedName

data class DiseaseResponse(
    @SerializedName("response")
    val response: Response? = null
)