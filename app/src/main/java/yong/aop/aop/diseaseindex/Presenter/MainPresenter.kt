package yong.aop.aop.diseaseindex.Presenter

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import yong.aop.aop.diseaseindex.Model.Dissave
import yong.aop.aop.diseaseindex.Fragment.SaveFragment

class MainPresenter(val mainView: SaveFragment) {

    fun get(abca : String) {
        var ref = FirebaseDatabase.getInstance().getReference("Articles").orderByChild("title").equalTo(abca)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val noinline = mutableListOf<Dissave>()

                if (snapshot.exists()){
                    noinline.clear()
                    for (ds in snapshot.children) {
                        val note = ds.getValue(Dissave::class.java)
                        if (note != null) {
                            noinline.add(note)
                            mainView.onSuccess(noinline)
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun delete(note: Dissave){
        val ref = FirebaseDatabase.getInstance().getReference("Articles")
            .child(note.id.toString())
        ref.removeValue()
    }
}