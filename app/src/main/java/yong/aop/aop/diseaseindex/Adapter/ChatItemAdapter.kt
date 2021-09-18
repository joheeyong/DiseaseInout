package yong.aop.aop.diseaseindex.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import yong.aop.aop.diseaseindex.Model.Chat
import yong.aop.aop.diseaseindex.R
import yong.aop.aop.diseaseindex.databinding.ItemChatBinding

class ChatItemAdapter : ListAdapter<Chat, ChatItemAdapter.ViewHolder>(diffUtil) {
    private lateinit var database : DatabaseReference

    inner class ViewHolder(private val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Chat) {
            val auth = Firebase.auth
            val set = ConstraintSet()
            val idTextView = binding.idTextViewww.id
            val messageTextView = binding.messageTextView.id
            val layout = binding.messageLayout
            if (chat.senderId == auth.currentUser?.uid) {
                binding.idTextViewaaa.text= chat.date
                binding.idTextViewaaaa.text= chat.date
                binding.idTextViewww.text = chat.name
                database = FirebaseDatabase.getInstance().getReference("Users")
                database.child(auth.currentUser!!.uid).get().addOnSuccessListener {
                    if (it.exists()) {
                        if(it.child("name").value.toString()!="null") {
                            binding.idTextViewww.text=it.child("name").value.toString()
                        }
                    }
                }.addOnFailureListener{
                }
                binding.messageTextView.text = chat.message
                binding.messageTextView.setBackgroundResource(R.drawable.widhet_background2)
            } else {
                binding.idTextViewaaa.text= chat.date
                binding.idTextViewaaaa.text= chat.date
                binding.idTextViewww.text = chat.name
                binding.messageTextView.text = chat.message
                binding.messageTextView.setBackgroundResource(R.drawable.widget_background)
            }
            set.clone(layout)
            if (chat.senderId == auth.currentUser?.uid) {
                set.setHorizontalBias(idTextView, 1F)
                set.setHorizontalBias(messageTextView, 1F)
            } else {
                set.setHorizontalBias(idTextView, 0F)
                set.setHorizontalBias(messageTextView, 0F)
            }
            set.applyTo(layout)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])

    }


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Chat>() {
            override fun areItemsTheSame(old: Chat, aNew: Chat): Boolean {
                return old == aNew
            }

            override fun areContentsTheSame(old: Chat, aNew: Chat): Boolean {
                return old == aNew
            }
        }
    }

}