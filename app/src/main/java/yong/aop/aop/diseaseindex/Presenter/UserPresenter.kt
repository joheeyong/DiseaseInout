package yong.aop.aop.diseaseindex.Presenter

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import yong.aop.aop.diseaseindex.Activity.EmailEditActivity
import yong.aop.aop.diseaseindex.Fragment.SettingsFragment

class UserPresenter (val mainView: EmailEditActivity) {
    private val auth: FirebaseAuth by lazy { Firebase.auth }
    private lateinit var database : DatabaseReference

    var name: String? =null

    fun get() {
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(auth.currentUser!!.uid).get().addOnSuccessListener {
            if (it.exists()) {
                if(it.child("name").value.toString()!="null") {
                    name= it.child("name").value.toString()
//                    mainView.onSuccess(name!!)
//                    binding!!.tvEmail.text=auth.currentUser!!.email
                    }
                }
            }.addOnFailureListener{
        }
    }

}