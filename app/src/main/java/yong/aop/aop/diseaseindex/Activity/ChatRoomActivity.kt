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
    private var database: DatabaseReference? = null
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
        val etMessage = binding!!.etMessage
        val btnSend = binding!!.btnSend
        database = Firebase.database.reference.child("Chats").child("$chatKey")
        scroll()
        chatPresenter = ChatPresenter(this)
        chatPresenter.get(chatKey)
        chatrecycler()

        etMessage?.addTextChangedListener {
            val enable = etMessage.text.isNotEmpty()
            btnSend.isEnabled = enable
        }
    }

    private fun chatrecycler() {
        binding!!.chatRecyclerView.adapter = adapter
        binding!!.chatRecyclerView.layoutManager = LinearLayoutManager(this)
        binding!!.btnSend.setOnClickListener {
            val simpleDateFormat = SimpleDateFormat("hh:mm")
            val date = simpleDateFormat.format(Date())
            val chatItem = auth.currentUser?.let { it1 ->
                Chat(
                    senderId = it1.uid,
                    message = binding!!.etMessage.text.toString(),
                    name = firstname,
                    date = date
                )
            }
            database?.push()?.setValue(chatItem)
            binding!!.etMessage.text = null
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