package yong.aop.aop.diseaseindex.Presenter

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import yong.aop.aop.diseaseindex.Fragment.InfoFragment
import java.util.*

class InfoPresenter (val mainView: InfoFragment){
    var disst: String =""
    private lateinit var database : DatabaseReference
    var starray= arrayOf("개요","개요-경과 및 예후","개요-병태생리","개요-원인","개요-정의","역학 및 통계","위험요인 및 예방","자주하는 질문","증상","진단 및 검사","치료","치료-약물 치료","합병증")
    var random = Random()
    var j = random.nextInt(13)
    fun get(dis :String) {
        database = FirebaseDatabase.getInstance().getReference("Disease")
        database.child(dis).get().addOnSuccessListener {
            if(it.child(starray[j]+"-5").value.toString()=="null" && it.child(starray[j]+"-4").value.toString()!="null") {
                disst=it.child(starray[j]+"-1").value.toString()+"\n\n"+it.child(starray[j]+"-2").value.toString()+"\n\n"+it.child(starray[j]+"-3").value.toString()+"\n\n"+it.child(starray[j]+"-4").value.toString()
            }
            else if(it.child(starray[j]+"-4").value.toString()=="null" && it.child(starray[j]+"-3").value.toString()!="null") {
                disst=it.child(starray[j]+"-1").value.toString()+"\n\n"+it.child(starray[j]+"-2").value.toString()+"\n\n"+it.child(starray[j]+"-3").value.toString()
            }
            else if(it.child(starray[j]+"-3").value.toString()=="null" && it.child(starray[j]+"-2").value.toString()!="null"){
                disst=it.child(starray[j]+"-1").value.toString()+"\n\n"+it.child(starray[j]+"-2").value.toString()
            }
            else if(it.child(starray[j]+"-2").value.toString()=="null" && it.child(starray[j]).value.toString()!="null"){
                disst=it.child(starray[j]).value.toString()
            }else{
            }
            mainView.onSuccess(disst,starray[j])
        }.addOnFailureListener{
        }

    }
}