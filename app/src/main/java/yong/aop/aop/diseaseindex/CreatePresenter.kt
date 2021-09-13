package yong.aop.aop.diseaseindex

import android.annotation.SuppressLint
import android.app.Activity
import android.text.TextUtils
import com.google.firebase.database.FirebaseDatabase


class CreatePresenter() {

    @SuppressLint("SimpleDateFormat")
    fun create(noteId: String? = null, title: String, desc: String, level2: String,level3: String, sellerId: String, date: String, activity: Activity) {
        if (!(TextUtils.isEmpty(title) || TextUtils.isEmpty(desc))) {
            val ref = FirebaseDatabase.getInstance().getReference("Articles")
            if (noteId == null) {
                val id = ref.push().key.toString()
                val notes = Dissave(id, title, desc,level2,level3, date, sellerId)
                ref.child(id).setValue(notes).addOnCompleteListener {
                    activity.finish()
                }
            } else {
                val notes = Dissave(noteId, title,desc, level2,level3, date,sellerId)
                ref.child(noteId.toString()).setValue(notes).addOnCompleteListener {
                    activity.finish()
                }
            }
        }
    }

}