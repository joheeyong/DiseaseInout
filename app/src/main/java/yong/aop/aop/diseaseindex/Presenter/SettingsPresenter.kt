package yong.aop.aop.diseaseindex.Presenter

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import yong.aop.aop.diseaseindex.Fragment.ChatListFragment
import yong.aop.aop.diseaseindex.Fragment.SettingsFragment


class SettingsPresenter(val mainView: SettingsFragment) {
    private val auth: FirebaseAuth by lazy { Firebase.auth }

    fun get() {
        var name: String? =null
        var registerdate: String? =null
        var logindate: String? =null
        var statemsg: String? =null
        var ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(auth.currentUser!!.uid).get().addOnSuccessListener {
            if (it.exists()) {
                if(it.child("name").value.toString()!="null") {
                    name=it.child("name").value.toString()
                    registerdate=it.child("registerdate").value.toString()
                    logindate=it.child("logindate").value.toString()
                    statemsg=it.child("statemsg").value.toString()
                }
                name?.let { it1 -> mainView.onSuccess(it1, registerdate, logindate, statemsg) }
            }
        }
    }

}