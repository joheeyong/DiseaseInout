package yong.aop.aop.diseaseindex

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Dissave(
    val id: String? = null,
    val title: String? = null,
    val desc: String? = null,
    val level2: String? = null,
    val level3: String? = null,
    val date: String? = null,
    val sellerId : String? = null,
) : Parcelable