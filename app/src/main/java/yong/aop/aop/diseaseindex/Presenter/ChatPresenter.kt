package yong.aop.aop.diseaseindex.Presenter

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import yong.aop.aop.diseaseindex.Activity.ChatRoomActivity
import yong.aop.aop.diseaseindex.Model.Chat

class ChatPresenter(val mainView: ChatRoomActivity)  {
    private var chatDB: DatabaseReference? = null
    private lateinit var database : DatabaseReference
    val chatList = mutableListOf<Chat>()
    private val auth: FirebaseAuth by lazy { Firebase.auth }
    var firstname: String = ""

    fun get(chatKey: String?) {
        chatDB = Firebase.database.reference.child("Chats").child("$chatKey")
                chatDB?.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatItem = snapshot.getValue(Chat::class.java)
                chatItem ?: return
                chatList.add(chatItem)
                mainView.onSuccess(chatList)
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }


}