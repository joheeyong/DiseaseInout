package yong.aop.aop.diseaseindex.Activity

import android.annotation.SuppressLint
import android.app.Activity
import android.text.TextUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import yong.aop.aop.diseaseindex.Model.Dissave
import java.text.SimpleDateFormat
import java.util.*

class GooglePresenter {
    private lateinit var auth: FirebaseAuth
    var simpleDateFormat = SimpleDateFormat(" yyyy. MM. dd.")
    var date = simpleDateFormat.format(Date())

    @SuppressLint("SimpleDateFormat")
    fun create() {
        val userId = auth.currentUser?.uid.orEmpty()
        val currentUserDb = Firebase.database.reference.child("Users").child(userId)
        val user = mutableMapOf<String, Any>()
        user["userId"] = userId
        user["name"] = "처음사용자"
        user["logindate"] = date
        user["registerdate"] = date
        currentUserDb.updateChildren(user)
    }
}
