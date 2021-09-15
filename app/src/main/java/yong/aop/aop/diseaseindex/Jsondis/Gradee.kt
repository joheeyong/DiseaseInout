package yong.aop.aop.diseaseindex.Jsondis

import androidx.annotation.ColorRes
import com.google.gson.annotations.SerializedName
import yong.aop.aop.diseaseindex.R

enum class Gradee(
    val label: String,
    @ColorRes val colorResId: Int,
    @ColorRes val colorResIdd: Int
)

{
    @SerializedName("1")
    GOOD("관심", R.drawable.gauge, R.color.blgreen),

    @SerializedName("2")
    NORMAL("주의",  R.drawable.gauge2, R.color.blyellow),

    @SerializedName("3")
    BAD("경고", R.drawable.gauge3, R.color.orange),

    @SerializedName("4")
    AWFUL("위험", R.drawable.gauge4, R.color.blred),

    UNKNOWN("미측정", R.drawable.gauge4, R.color.blred);

    override fun toString(): String {
        return "$label"
    }
}
