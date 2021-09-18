package yong.aop.aop.diseaseindex.Activity

import android.R
import android.app.AlertDialog
import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import yong.aop.aop.diseaseindex.Services.NetworkStatus
import yong.aop.aop.diseaseindex.databinding.ActivityInfomationBinding

class InfomationActivity : AppCompatActivity() {
    private var mBinding: ActivityInfomationBinding? = null

    private val binding get() = mBinding!!
    private lateinit var database : DatabaseReference
    var firstname: String=""
    var viewkey: String="개요"
    var viewkey2: String="개요-정의"
    var viewkey3: String="개요-원인"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.setTitle(Html.fromHtml("<b>"+intent.getStringExtra("Key")))
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        mBinding = ActivityInfomationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding!!.btnRestart.setOnClickListener { restart() }
        binding!!.llyAcount.setOnClickListener { selectarea() }

        restart()
        readData()

    }

    private fun selectarea() {
        viewkey= null.toString()
        viewkey2=null.toString()
        viewkey3=null.toString()
        var items = arrayOf("")
        if(intent.getStringExtra("Key")=="감기") {
            items = arrayOf("개요 (정의, 원인)", "개요 (경과 및 병태생리)", "역학통계, 증상","진단 및 검사","치료 ( 약물, 비약물)", "합병증, 위험요인 예방", "자주하는 질문")
        }else if(intent.getStringExtra("Key")=="눈병"){
            items = arrayOf("개요 (정의, 원인)", "개요 (경과 및 병태생리)", "역학통계, 증상","진단 및 검사", "치료 ( 약물, 비약물)","자가 관리, 정기 진찰", "합병증, 위험요인 예방"
                ,"자주하는 질문")
        }else if(intent.getStringExtra("Key")=="식중독"){
            items = arrayOf("개요 (정의, 원인)", "증상, 진단 및 검사", "치료", "생활습관 관리", "대상별 맞춤 정보")
        }else if(intent.getStringExtra("Key")=="천식"){
            items = arrayOf("개요 (정의, 원인)", "병태생리 및 증상", "진단 및 검사", "치료 ( 약물, 비약물)", "대상별 맞춤 정보", "기타")
        }else if(intent.getStringExtra("Key")=="아토피피부염"){
            items = arrayOf("개요 (정의, 원인)", "경과예후 및 역학통계", "증상", "진단 및 검사 ", "치료 ( 약물 )","치료 ( 비약물 )", "합병증, 위험요인 예방","생활습관 관리")
        }else if(intent.getStringExtra("Key")=="지루 피부염"){
            items = arrayOf("개요 (정의, 원인)", "역학통계, 증상", "진단 및 검사, 치료", "예방 및 자주하는 질문")
        }
        var selectedItem: String? = null
        val builder = AlertDialog.Builder(this)
        builder.setNegativeButton("취소", null)
            .setTitle("어떤 정보를 찾으시나요?")
            .setSingleChoiceItems(items, -1) { dialog, which ->
                selectedItem = items[which]
            }
            .setPositiveButton("확인") { dialog, which ->
                restart()
                if(selectedItem.toString()=="null"){
                }else {
                    binding!!.etSick.text = selectedItem.toString()
                }

                if(selectedItem.toString()=="개요 (정의, 원인)"){
                    viewkey="개요"
                    viewkey2="개요-정의"
                    viewkey3="개요-원인"
                }else if(selectedItem.toString()=="개요 (경과 및 병태생리)"){
                    viewkey="개요-경과 및 예후"
                    viewkey2="개요-병태생리"
                }
                else if(selectedItem.toString()=="경과예후 및 역학통계"){
                    viewkey="개요-경과 및 예후"
                    viewkey2="역학 및 통계"
                }
                else if(selectedItem.toString()=="증상"){
                    viewkey="증상"
                }
                else if(selectedItem.toString()=="병태생리 및 증상"){
                    viewkey="개요-병태생리"
                    viewkey2="증상"
                }
                else if(selectedItem.toString()=="역학통계, 증상"){
                    viewkey="역학 및 통계"
                    viewkey2="증상"
                }
                else if(selectedItem.toString()=="진단 및 검사 "){
                    viewkey="진단"
                    viewkey2="검사"
                }
                else if(selectedItem.toString()=="증상, 진단 및 검사"){
                    viewkey="증상"
                    viewkey2="진단 및 검사"
                }
                else if(selectedItem.toString()=="치료 ( 약물, 비약물)"||selectedItem.toString()=="치료"){
                    viewkey="치료"
                    viewkey2="치료-약물 치료"
                    viewkey3="치료-비약물 치료"
                }
                else if(selectedItem.toString()=="치료 ( 약물 )"){
                    viewkey="치료"
                    viewkey2="치료-약물 치료"
                }
                else if(selectedItem.toString()=="치료 ( 비약물 )"){
                    viewkey="치료-비약물 치료"
                }
                else if(selectedItem.toString()=="자가 관리, 정기 진찰"){
                    viewkey="자가 관리"
                    viewkey2="정기 진찰"
                }
                else if(selectedItem.toString()=="합병증, 위험요인 예방"){
                    viewkey="합병증"
                    viewkey2="위험요인 및 예방"
                }
                else if(selectedItem.toString()=="생활습관 관리"){
                    viewkey="여름철 식중독 예방 수칙"
                    viewkey2="겨울철 식중독 예방 수칙"
                    viewkey3="생활습관 관리"
                }
                else if(selectedItem.toString()=="자주하는 질문"){
                    viewkey="자주하는 질문"
                }
                else if(selectedItem.toString()=="대상별 맞춤 정보"){
                    viewkey="대상별 맞춤 정보"
                }
                else if(selectedItem.toString()=="진단 및 검사, 치료"){
                    viewkey="진단 및 검사"
                    viewkey2="치료"
                }
                else if(selectedItem.toString()=="예방 및 자주하는 질문"){
                    viewkey="위험요인 및 예방"
                    viewkey2="자주하는 질문"
                }
                else if(selectedItem.toString()=="기타"){
                    viewkey="기타"
                }
                readData()
            }
        builder.show()
    }

    private fun restart() {
        val status = NetworkStatus.getConnectivityStatus(applicationContext)
        if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {
            binding!!.llyOutnet.visibility= View.GONE
            binding!!.zxcv.visibility= View.VISIBLE
            binding!!.llyInfodis1.visibility= View.VISIBLE
            binding!!.llyInfodis2.visibility= View.VISIBLE
            binding!!.llyInfodis3.visibility= View.VISIBLE
        } else{
            binding!!.llyOutnet.visibility= View.VISIBLE
            binding!!.zxcv.visibility= View.GONE
            binding!!.llyInfodis1.visibility= View.GONE
            binding!!.llyInfodis2.visibility= View.GONE
            binding!!.llyInfodis3.visibility= View.GONE
            Toast.makeText(this, "네트워크 연결이 끊겼습니다.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun readData() {
        binding!!.llyInfodis1.visibility= View.GONE
        binding!!.llyInfodis2.visibility= View.GONE
        binding!!.llyInfodis3.visibility= View.GONE
        database = FirebaseDatabase.getInstance().getReference("Disease")
        database.child(intent.getStringExtra("Key").toString()).get().addOnSuccessListener {
            if (it.exists()) {
                binding!!.tvInfodis1.text = it.child(viewkey).key.toString()
                binding!!.tvInfodis2.text = it.child(viewkey2).key.toString()
                binding!!.tvInfodis3.text = it.child(viewkey3).key.toString()
                if(it.child(viewkey+"-5").value.toString()=="null" && it.child(viewkey+"-4").value.toString()!="null") {
                    binding!!.llyInfodis1.visibility= View.VISIBLE
                    binding!!.tvInfodiss1.text=it.child(viewkey+"-1").value.toString()+"\n\n"+it.child(viewkey+"-2").value.toString()+"\n\n"+it.child(viewkey+"-3").value.toString()+"\n\n"+it.child(viewkey+"-4").value.toString()
                }
                else if(it.child(viewkey+"-4").value.toString()=="null" && it.child(viewkey+"-3").value.toString()!="null") {
                    binding!!.llyInfodis1.visibility= View.VISIBLE
                    binding!!.tvInfodiss1.text=it.child(viewkey+"-1").value.toString()+"\n\n"+it.child(viewkey+"-2").value.toString()+"\n\n"+it.child(viewkey+"-3").value.toString()
                } else if(it.child(viewkey+"-3").value.toString()=="null" && it.child(viewkey+"-2").value.toString()!="null"){
                    binding!!.llyInfodis1.visibility= View.VISIBLE
                    binding!!.tvInfodiss1.text=it.child(viewkey+"-1").value.toString()+"\n\n"+it.child(viewkey+"-2").value.toString()
                } else if(it.child(viewkey+"-2").value.toString()=="null" && it.child(viewkey).value.toString()!="null"){
                    binding!!.llyInfodis1.visibility= View.VISIBLE
                    binding!!.tvInfodiss1.text=it.child(viewkey).value.toString()
                }

                if(it.child(viewkey2+"-5").value.toString()=="null" && it.child(viewkey2+"-4").value.toString()!="null") {
                    binding!!.llyInfodis2.visibility= View.VISIBLE
                    binding!!.tvInfodiss2.text=it.child(viewkey2+"-1").value.toString()+"\n\n"+it.child(viewkey2+"-2").value.toString()+"\n\n"+it.child(viewkey2+"-3").value.toString()+"\n\n"+it.child(viewkey2+"-4").value.toString()
                }
                else if(it.child(viewkey2+"-4").value.toString()=="null" && it.child(viewkey2+"-3").value.toString()!="null") {
                    binding!!.llyInfodis2.visibility= View.VISIBLE
                    binding!!.tvInfodiss2.text=it.child(viewkey2+"-1").value.toString()+"\n\n"+it.child(viewkey2+"-2").value.toString()+"\n\n"+it.child(viewkey2+"-3").value.toString()
                }  else if (it.child(viewkey2+"-3").value.toString()=="null" && it.child(viewkey2+"-2").value.toString()!="null"){
                    binding!!.llyInfodis2.visibility= View.VISIBLE
                    binding!!.tvInfodiss2.text=it.child(viewkey2+"-1").value.toString()+"\n\n"+it.child(viewkey2+"-2").value.toString()
                } else if (it.child(viewkey2+"-2").value.toString()=="null" && it.child(viewkey2).value.toString()!="null"){
                    binding!!.llyInfodis2.visibility= View.VISIBLE
                    binding!!.tvInfodiss2.text=it.child(viewkey2).value.toString()
                }

                if(it.child(viewkey3+"-5").value.toString()=="null" && it.child(viewkey3+"-4").value.toString()!="null") {
                    binding!!.llyInfodis3.visibility= View.VISIBLE
                    binding!!.tvInfodiss3.text=it.child(viewkey3+"-1").value.toString()+"\n\n"+it.child(viewkey3+"-2").value.toString()+"\n\n"+it.child(viewkey3+"-3").value.toString()+"\n\n"+it.child(viewkey3+"-4").value.toString()
                }
                else if(it.child(viewkey3+"-4").value.toString()=="null" && it.child(viewkey3+"-3").value.toString()!="null") {
                    binding!!.llyInfodis3.visibility= View.VISIBLE
                    binding!!.tvInfodiss3.text=it.child(viewkey3+"-1").value.toString()+"\n\n"+it.child(viewkey3+"-2").value.toString()+"\n\n"+it.child(viewkey3+"-3").value.toString()
                }  else if (it.child(viewkey3+"-3").value.toString()=="null" && it.child(viewkey3+"-2").value.toString()!="null"){
                    binding!!.llyInfodis3.visibility= View.VISIBLE
                    binding!!.tvInfodiss3.text=it.child(viewkey3+"-1").value.toString()+"\n\n"+it.child(viewkey3+"-2").value.toString()
                } else if (it.child(viewkey3+"-2").value.toString()=="null" && it.child(viewkey3).value.toString()!="null"){
                    binding!!.llyInfodis3.visibility= View.VISIBLE
                    binding!!.tvInfodiss3.text=it.child(viewkey3).value.toString()
                }
            }
        }.addOnFailureListener{
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}