package yong.aop.aop.diseaseindex.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import yong.aop.aop.diseaseindex.Model.Dissave
import yong.aop.aop.diseaseindex.R
import yong.aop.aop.diseaseindex.databinding.NotedItemLayoutBinding


class DissaveAdapter(private val noteList: List<Dissave>) :

    RecyclerView.Adapter<DissaveAdapter.NoteViewHolder>() {
    private val auth: FirebaseAuth by lazy { Firebase.auth }
    private lateinit var onItemClickCallBack: OnItemClickCallBack

    fun setOnItemCLickCallBack(onItemClickCallBack: OnItemClickCallBack) { this.onItemClickCallBack = onItemClickCallBack }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NotedItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) { holder.bind(noteList[position]) }

    override fun getItemCount(): Int = noteList.size

    inner class NoteViewHolder(private val binding: NotedItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Dissave) {
            if(note.sellerId == auth.uid){
                with(binding) {
                    tvTitle.text = note.title
                    tvDesc.text = note.desc
                    tvDate.text = note.date
                    tvLevel2.text =note.level2
                    if(tvTitle.text.toString()=="감기"){
                        ivPick.setBackgroundResource(R.drawable.cough)
                    } else if(tvTitle.text.toString()=="눈병"){
                        ivPick.setBackgroundResource(R.drawable.search)
                    }else if(tvTitle.text.toString()=="식중독"){
                        ivPick.setBackgroundResource(R.drawable.poison)
                    }else if(tvTitle.text.toString()=="천식"){
                        ivPick.setBackgroundResource(R.drawable.nebulizer)
                    }else if(tvTitle.text.toString()=="피부염"){
                        ivPick.setBackgroundResource(R.drawable.dermis)
                    }else{
                        ivPick.setBackgroundResource(R.drawable.loding)
                    }

                    itemView.setOnClickListener {
                        onItemClickCallBack.onItemClicked(note)
                    }

                }
            }
            else{
                binding.cardBg.layoutParams.height=0
            }
        }
    }

    interface OnItemClickCallBack {
        fun onItemClicked(note: Dissave)
    }
}