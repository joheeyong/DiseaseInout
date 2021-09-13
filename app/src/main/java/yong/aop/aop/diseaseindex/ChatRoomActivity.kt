package yong.aop.aop.diseaseindex

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ChatRoomActivity : AppCompatActivity() {
    var firstname: String=""

    private lateinit var database : DatabaseReference
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    private val chatList = mutableListOf<ChatItem>()
    private val adapter = ChatItemAdapter()
    private var chatDB: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatroom)
        getSupportActionBar()?.setTitle(Html.fromHtml("<b>"+intent.getStringExtra("chatKey")+"채팅방"))
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        val chatKey = intent.getStringExtra("chatKey")
        val emailEditText = findViewById<EditText>(R.id.messageEditText)
        val sendButton = findViewById<Button>(R.id.sendButton)
        chatDB = Firebase.database.reference.child("Chats").child("$chatKey")
        readData()

        chatDB?.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                val chatItem = snapshot.getValue(ChatItem::class.java)
                chatItem ?: return
                chatList.add(chatItem)
                adapter.submitList(chatList)
                adapter.notifyDataSetChanged()
                findViewById<RecyclerView>(R.id.chatRecyclerView).post(Runnable {
                    findViewById<RecyclerView>(R.id.chatRecyclerView).scrollToPosition(
                        findViewById<RecyclerView>(R.id.chatRecyclerView).getAdapter()!!.getItemCount() - 1
                    )
                })
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })

        findViewById<RecyclerView>(R.id.chatRecyclerView).adapter = adapter
        findViewById<RecyclerView>(R.id.chatRecyclerView).layoutManager = LinearLayoutManager(this)
        findViewById<Button>(R.id.sendButton).setOnClickListener {
            val simpleDateFormat = SimpleDateFormat("hh:mm")
            val date = simpleDateFormat.format(Date())
            val chatItem = auth.currentUser?.let { it1 ->
                ChatItem(
                    senderId = it1.uid,
                    message = emailEditText.text.toString(),
                    name = firstname,
                    date = date
                )
            }

            chatDB?.push()?.setValue(chatItem)
            emailEditText.text = null
            findViewById<RecyclerView>(R.id.chatRecyclerView).post(Runnable {
                findViewById<RecyclerView>(R.id.chatRecyclerView).scrollToPosition(
                    findViewById<RecyclerView>(R.id.chatRecyclerView).getAdapter()!!.getItemCount() - 1
                )
            })
        }

        emailEditText?.addTextChangedListener {
            val enable = emailEditText.text.isNotEmpty()
            sendButton.isEnabled = enable
        }
        findViewById<RecyclerView>(R.id.chatRecyclerView).post(Runnable {
            findViewById<RecyclerView>(R.id.chatRecyclerView).scrollToPosition(
                findViewById<RecyclerView>(R.id.chatRecyclerView).getAdapter()!!.getItemCount() - 1
            )
        })
        findViewById<RecyclerView>(R.id.chatRecyclerView).post(Runnable {
            findViewById<RecyclerView>(R.id.chatRecyclerView).scrollToPosition(
                findViewById<RecyclerView>(R.id.chatRecyclerView).getAdapter()!!.getItemCount() - 1
            )
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        adapter.submitList(chatList)
        adapter.notifyDataSetChanged()
    }

    private fun readData() {
        database = FirebaseDatabase.getInstance().getReference("Users")
        auth.currentUser?.let {
            database.child(it.uid).get().addOnSuccessListener {
                if (it.exists()){
                    firstname = it.child("name").value.toString()
                }else{

                }
            }.addOnFailureListener{
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}