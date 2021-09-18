package yong.aop.aop.diseaseindex.Activity

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import yong.aop.aop.diseaseindex.Model.Chat
import yong.aop.aop.diseaseindex.Adapter.ChatItemAdapter
import yong.aop.aop.diseaseindex.Presenter.ChatPresenter
import yong.aop.aop.diseaseindex.databinding.ActivityChatroomBinding
import java.text.SimpleDateFormat
import java.util.*

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ChatRoomActivity : AppCompatActivity() {
    var binding: ActivityChatroomBinding? = null

    private lateinit var chatPresenter: ChatPresenter
    private lateinit var database : DatabaseReference
    private var chatDB: DatabaseReference? = null
    private val auth: FirebaseAuth by lazy { Firebase.auth }

    var firstname: String = ""
    private val chatList = mutableListOf<Chat>()
    private val adapter = ChatItemAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatroomBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        getSupportActionBar()?.setTitle(Html.fromHtml("<b>"+intent.getStringExtra("chatKey")+"채팅방"))
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)


        val chatKey = intent.getStringExtra("chatKey")
        val emailEditText = binding!!.messageEditText
        val sendButton = binding!!.sendButton
        chatDB = Firebase.database.reference.child("Chats").child("$chatKey")
//        readData()
        scroll()
        chatPresenter = ChatPresenter(this)
        chatPresenter.get(chatKey)
        chatrecycler()

        emailEditText?.addTextChangedListener {
            val enable = emailEditText.text.isNotEmpty()
            sendButton.isEnabled = enable
        }
    }

    private fun chatrecycler() {
        binding!!.chatRecyclerView.adapter = adapter
        binding!!.chatRecyclerView.layoutManager = LinearLayoutManager(this)
        binding!!.sendButton.setOnClickListener {
            val simpleDateFormat = SimpleDateFormat("hh:mm")
            val date = simpleDateFormat.format(Date())
            val chatItem = auth.currentUser?.let { it1 ->
                Chat(
                    senderId = it1.uid,
                    message = binding!!.messageEditText.text.toString(),
                    name = firstname,
                    date = date
                )
            }
            chatDB?.push()?.setValue(chatItem)
            binding!!.messageEditText.text = null
            scroll()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        adapter.submitList(chatList)
        adapter.notifyDataSetChanged()
    }
    private fun scroll() {
        binding!!.chatRecyclerView.post(Runnable {
            binding!!.chatRecyclerView.scrollToPosition(
                binding!!.chatRecyclerView.getAdapter()!!.getItemCount() - 1
            )
        })
    }

    fun onSuccess(chatList: MutableList<Chat>) {
                adapter.submitList(chatList)
                adapter.notifyDataSetChanged()
                scroll()
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