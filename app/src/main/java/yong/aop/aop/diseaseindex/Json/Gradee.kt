package yong.aop.aop.diseaseindex.Json

import androidx.annotation.ColorRes
import com.google.gson.annotations.SerializedName
import yong.aop.aop.diseaseindex.R

enum class Gradee(
    val label: String,
    @ColorRes val colorResId: Int,
    @ColorRes val colorResIdd: Int
) {

    @SerializedName("1")
    GOOD("관심", R.drawable.gauge, R.color.green),

    @SerializedName("2")
    NORMAL("주의",  R.drawable.gauge2, R.color.yellow),

    @SerializedName("3")
    BAD("경고", R.drawable.gauge3, R.color.orange),

    @SerializedName("4")
    AWFUL("위험", R.drawable.gauge4, R.color.red),

    UNKNOWN("미측정", R.drawable.gauge4, R.color.red);

    override fun toString(): String {
        return "$label"
    }
}






//    val label: String,
//
//) {
//
//    @SerializedName("1")
//    GOOD("관심"),
//
//    @SerializedName("2")
//    NORMAL("주의"),
//
//    @SerializedName("3")
//    BAD("경고"),
//
//    @SerializedName("4")
//    AWFUL("위험"),
//
//    UNKNOWN("미측정");
//
//    override fun toString(): String {
//        return "$label"
//    }
//}
