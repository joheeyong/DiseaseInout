package yong.aop.aop.diseaseindex

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class SettingsPresenter(val mainView: SettingsFragment) {
    private val auth: FirebaseAuth by lazy { Firebase.auth }

    fun get() {
        var abc: String? =null
        var ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(auth.currentUser!!.uid).get().addOnSuccessListener {
            if (it.exists()) {
                if(it.child("name").value.toString()!="null") {
                    abc=it.child("name").value.toString()
                }
                abc?.let { it1 -> mainView.onSuccess(it1) }
            }
//        database = FirebaseDatabase.getInstance().getReference("Users")
//        database.child(auth.currentUser!!.uid).get().addOnSuccessListener {
//            if (it.exists()) {
//
//                if(it.child("name").value.toString()!="null") {
//                    name= it.child("name").value.toString()
//                    binding!!.tvEmail.setText(auth.currentUser!!.email)
//                    binding!!.tvNickname.text=name+" 님"
//                    binding!!.tvNickname2.text=name
//                    binding!!.tvDate1.text="가입한 날짜 : "+it.child("registerdate").value.toString()
//                    binding!!.tvDate2.text="로그인한 날짜 : "+it.child("logindate").value.toString()
//                    binding!!.tvMymessage.text=it.child("statemsg").value.toString()
//
//                    if(binding!!.tvMymessage.text.length>15){
//
//                        binding!!.tvMymessage.text=it.child("statemsg").value.toString().substring(0,15)+"..."
//                    }
//                    if(it.child("statemsg").value.toString()=="null"){
//                        binding!!.tvMymessage.text="상태메세지를 입력해 주세요."
//                    }
//                }
//
//            }
//        }.addOnFailureListener{
//        }
        }
    }
}