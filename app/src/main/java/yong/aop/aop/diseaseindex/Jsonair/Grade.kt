package yong.aop.aop.diseaseindex.Jsonair

import androidx.annotation.ColorRes
import com.google.gson.annotations.SerializedName
import yong.aop.aop.diseaseindex.R

enum class Grade(
    val label: String,
    val emoji: String,
    @ColorRes val colorResId: Int,
    @ColorRes val colorResIdd: Int,
    @ColorRes val colorResIddd: Int
) {

    @SerializedName("1")
    GOOD("좋음", "😆", R.color.whblue, R.color.blblue,R.drawable.windblue),

    @SerializedName("2")
    NORMAL("보통", "🙂", R.color.whgreen, R.color.blgreen,R.drawable.windgreen),

    @SerializedName("3")
    BAD("나쁨", "😞", R.color.whyellow, R.color.blyellow,R.drawable.windyellow),

    @SerializedName("4")
    AWFUL("매우 나쁨", "😫", R.color.whred, R.color.blred,R.drawable.windred),

    UNKNOWN("미측정", "🧐", R.color.whred, R.color.blred,R.drawable.windred);

    override fun toString(): String {
        return "$label $emoji"
    }
}
