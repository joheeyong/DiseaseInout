package yong.aop.aop.diseaseindex

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import yong.aop.aop.diseaseindex.databinding.FragmentAfterBinding

class AfterFragment : Fragment(R.layout.fragment_after) {
    private var binding: FragmentAfterBinding? = null

    private lateinit var mainPresenter: MainPresenter
    private val auth: FirebaseAuth by lazy { Firebase.auth }
    var disname: String? = "감기"
    var work: String? =null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAfterBinding.bind(view)

        binding!!.rgTransport.isChecked = true
        mainPresenter = MainPresenter(this)
        disname?.let { mainPresenter.get(it) }

        binding!!.rgRadiogroup.setOnCheckedChangeListener { group, checkedId ->
            checknet("라디오버튼",checkedId)
        }
        binding!!.addFloatingButton.setOnClickListener {
            checknet("추가버튼",1)
        }
        binding!!.btnRestart.setOnClickListener {
            restart()
        }
        restart()
    }

    private fun checknet(workgo: String, checkedId: Int) {
        if (workgo=="라디오버튼"){
            work= movedis(checkedId).toString()
        } else if (workgo=="추가버튼"){
            work= createdis().toString()
        }
        var status = context?.let { NetworkStatus.getConnectivityStatus(it) }
        if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {
                work
        }else{
            Toast.makeText(context, "네트워크 연결이 끊겼습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createdis() {
        context?.let {
            if (auth.currentUser != null) {
                val intent = Intent(it, CreateNoteActivity::class.java)
                startActivity(intent)
            } else {
                view?.let { it1 -> Snackbar.make(it1, "로그인 후 사용해주세요", Snackbar.LENGTH_LONG).show() }
            }
        }
    }

    private fun movedis(checkedId: Int) {
        when (checkedId) {
            R.id.rg_Transport -> disname = "감기"
            R.id.rg_lodgment -> disname = "눈병"
            R.id.rg_food -> disname = "식중독"
            R.id.rg_shopping -> disname = "천식"
            R.id.rg_tourism -> disname = "피부염"
            R.id.rg_other -> disname = "기타" }
        disname?.let { mainPresenter.get(it) }
    }

    private fun restart() {
        val status = context?.let { NetworkStatus.getConnectivityStatus(it) }
        if (status != NetworkStatus.TYPE_MOBILE && status != NetworkStatus.TYPE_WIFI) {
            binding!!.llyToptext.visibility=View.GONE
            binding!!.llyToptextx.visibility=View.GONE
            binding!!.rvNote.visibility=View.GONE
            binding!!.addFloatingButton.visibility=View.GONE
            binding!!.llyOutnet.visibility=View.VISIBLE
            Toast.makeText(context, "네트워크 연결이 끊겼습니다.", Toast.LENGTH_SHORT).show()
        } else{
            binding!!.llyOutnet.visibility=View.GONE
            binding!!.llyToptext.visibility=View.VISIBLE
            binding!!.llyToptextx.visibility=View.VISIBLE
            binding!!.addFloatingButton.visibility=View.VISIBLE
            binding!!.rvNote.visibility=View.VISIBLE
        }
    }

    fun onSuccess(noinline: MutableList<Dissave>) {
        val adapter = context?.let { DissaveAdapter(noinline) }

        binding!!.rvNote.setHasFixedSize(true)
        binding!!.rvNote.layoutManager =
            LinearLayoutManager(context)
        binding!!.rvNote.adapter = adapter

        adapter!!.setOnItemCLickCallBack(object : DissaveAdapter.OnItemClickCallBack {
            override fun onItemClicked(note: Dissave) {
                var status = context?.let { NetworkStatus.getConnectivityStatus(it) }
                if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                val builder = AlertDialog.Builder(context)
                builder.setNegativeButton("수정"){ dialog, which ->
                    selectedNote(note)
                }
                    .setTitle("작업을 선택해주세요.")
                    .setPositiveButton("삭제") { dialog, which ->
                        mainPresenter.delete(note)
                    }
                builder.show()
                }else{
                    Toast.makeText(context, "네트워크 연결이 끊겼습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun selectedNote(note: Dissave) {
        context?.let {
            val intent = Intent(it, CreateNoteActivity::class.java)
            intent.putExtra(CreateNoteActivity.EXTRA_ID, note.id)
            intent.putExtra(CreateNoteActivity.EXTRA_TITLE, note.title)
            intent.putExtra(CreateNoteActivity.EXTRA_DESC, note.desc)
            intent.putExtra(CreateNoteActivity.EXTRA_LEVEL2, note.level2)
            intent.putExtra(CreateNoteActivity.EXTRA_LEVEL3, note.level3)
            intent.putExtra(CreateNoteActivity.EXTRA_DATE, note.date)
            startActivity(intent)
        }
    }

}