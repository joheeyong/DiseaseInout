package yong.aop.aop.diseaseindex.Json2

import androidx.annotation.ColorRes
import com.google.gson.annotations.SerializedName
import yong.aop.aop.diseaseindex.R

enum class Grade(
    val label: String,
    val emoji: String,
    @ColorRes val colorResId: Int,
    @ColorRes val colorResIdd: Int
) {

    @SerializedName("1")
    GOOD("좋음", "😆", R.color.cblue, R.color.bluee),

    @SerializedName("2")
    NORMAL("보통", "🙂", R.color.cgreen, R.color.green),

    @SerializedName("3")
    BAD("나쁨", "😞", R.color.cyellow, R.color.yellow),

    @SerializedName("4")
    AWFUL("매우 나쁨", "😫", R.color.cred, R.color.red),

    UNKNOWN("미측정", "🧐", R.color.cred, R.color.red);

    override fun toString(): String {
        return "$label $emoji"
    }
}
