package yong.aop.aop.diseaseindex.Jsondis

import com.google.gson.annotations.SerializedName

data class DisInfo(
    @SerializedName("cnt")
    val cnt: Int? = null,
    @SerializedName("dissCd")
    val dissCd: String? = null,
    @SerializedName("dissRiskXpln")
    val dissRiskXpln: String? = null,
    @SerializedName("dt")
    val dt: String? = null,
    @SerializedName("lowrnkZnCd")
    val lowrnkZnCd: String? = null,
    @SerializedName("risk")
    val risk: Gradee? = null,
    @SerializedName("znCd")
    val znCd: String? = null
)