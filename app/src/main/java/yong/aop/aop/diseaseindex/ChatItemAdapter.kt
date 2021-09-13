package yong.aop.aop.diseaseindex

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
import yong.aop.aop.diseaseindex.databinding.ItemChatBinding

class ChatItemAdapter : ListAdapter<ChatItem, ChatItemAdapter.ViewHolder>(diffUtil) {
    private lateinit var database : DatabaseReference

    inner class ViewHolder(private val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chatItem: ChatItem) {
            val auth = Firebase.auth
            val set = ConstraintSet()
            val idTextView = binding.idTextViewww.id
            val messageTextView = binding.messageTextView.id
            val layout = binding.messageLayout
            if (chatItem.senderId == auth.currentUser?.uid) {
                binding.idTextViewaaa.text= chatItem.date
                binding.idTextViewaaaa.text= chatItem.date
                binding.idTextViewww.text = chatItem.name
                database = FirebaseDatabase.getInstance().getReference("Users")
                database.child(auth.currentUser!!.uid).get().addOnSuccessListener {
                    if (it.exists()) {
                        if(it.child("name").value.toString()!="null") {
                            binding.idTextViewww.text=it.child("name").value.toString()
                        }
                    }
                }.addOnFailureListener{
                }
                binding.messageTextView.text = chatItem.message
                binding.messageTextView.setBackgroundResource(R.drawable.widhet_background2)
            } else {
                binding.idTextViewaaa.text= chatItem.date
                binding.idTextViewaaaa.text= chatItem.date
                binding.idTextViewww.text = chatItem.name
                binding.messageTextView.text = chatItem.message
                binding.messageTextView.setBackgroundResource(R.drawable.widget_background)
            }
            set.clone(layout)
            if (chatItem.senderId == auth.currentUser?.uid) {
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
        val diffUtil = object : DiffUtil.ItemCallback<ChatItem>() {
            override fun areItemsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
                return oldItem == newItem
            }


        }
    }

}