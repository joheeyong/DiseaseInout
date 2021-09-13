package yong.aop.aop.diseaseindex

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import yong.aop.aop.diseaseindex.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    var binding: FragmentSettingsBinding? = null

    private lateinit var SettingsPresenter: SettingsPresenter
    private lateinit var database : DatabaseReference
    private val auth: FirebaseAuth by lazy { Firebase.auth }
    val SUBACTIITY_REQUEST_CODE = 100
    var name: String? =null
    var work: String? =null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)

        binding!!.btnRestart.setOnClickListener { restart() }
        binding!!.conOne.setOnClickListener {
            checknet("이메일수정")
        }
        binding!!.conFour.setOnClickListener {
            checknet("메일전송")

        }
        binding!!.btnRepass.setOnClickListener {
            checknet("메일전송")
        }
        binding!!.conTwo.setOnClickListener {
            checknet("닉네임수정")
        }
        binding!!.conThree.setOnClickListener {
            checknet("상태메세지수정")
        }
        binding!!.btnAuthdel.setOnClickListener {
            checknet("회원탈퇴")
        }

        binding!!.btnLogout.setOnClickListener {
            checknet("로그아웃")
        }
        nameread()
        restart()
    }

    private fun checknet(workgo :String) {
        if (workgo=="메일전송"){
            work= sandemail().toString()
        } else if (workgo=="이메일수정"){
            work=emailedit().toString()
        } else if (workgo=="닉네임수정"){
            work=moveedit("닉네임").toString()
        } else if (workgo=="상태메세지수정"){
            work=moveedit("상태메세지").toString()
        } else if (workgo=="회원탈퇴"){
            work=authdel().toString()
        } else if (workgo=="로그아웃"){
            work=logout().toString()
        }
        val status = context?.let { NetworkStatus.getConnectivityStatus(it) }
        if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {
            work
        } else {
            Toast.makeText(context, "네트워크 연결을 확인해 주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun moveedit(value:String) {
        val status = context?.let { NetworkStatus.getConnectivityStatus(it) }
        if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {
            val intent = Intent(context, NickEditActivity::class.java)
            intent.putExtra("Key", value)
            startActivityForResult(intent, SUBACTIITY_REQUEST_CODE)
        }
    }

    private fun logout() {
        var builder = context?.let { it1 -> AlertDialog.Builder(it1) }
        builder!!.setTitle("로그아웃")
        builder.setIcon(R.drawable.loding)

        var v1 = layoutInflater.inflate(R.layout.dialog3, null)
        builder.setView(v1)
        var listener = DialogInterface.OnClickListener { p0, p1 ->
            auth.signOut()
            val intent = Intent(context, GoogleLoginActivity::class.java)
            startActivity(intent)
        }
        builder.setPositiveButton("확인", listener)
        builder.setNegativeButton("취소", null)
        builder.show()
    }

    private fun authdel() {
        var builder = context?.let { it1 -> AlertDialog.Builder(it1) }
        builder!!.setTitle("회원탈퇴")
        builder.setIcon(R.drawable.loding)
        builder.setView(layoutInflater.inflate(R.layout.dialog4, null))
        var listener = DialogInterface.OnClickListener { p0, p1 ->
            auth.currentUser?.delete()
            val intent = Intent(context, GoogleLoginActivity::class.java)
            startActivity(intent)
        }
        builder.setPositiveButton("확인", listener)
        builder.setNegativeButton("취소", null)
        builder.show()
    }

    private fun restart() {
        val status = context?.let { NetworkStatus.getConnectivityStatus(it) }
        if (status != NetworkStatus.TYPE_MOBILE && status != NetworkStatus.TYPE_WIFI) {
            binding!!.llyMyone.visibility=View.GONE
            binding!!.llyMytwo.visibility=View.GONE
            binding!!.llyMythree.visibility=View.GONE
            binding!!.llyOutnet.visibility=View.VISIBLE
            Toast.makeText(context, "네트워크 연결이 끊겼습니다.", Toast.LENGTH_SHORT).show()
        } else{
            binding!!.llyOutnet.visibility=View.GONE
            binding!!.llyMyone.visibility=View.VISIBLE
            binding!!.llyMytwo.visibility=View.VISIBLE
            binding!!.llyMythree.visibility=View.VISIBLE
            nameread()
        }
    }

    private fun emailedit() {
        val intent = Intent(context, EmailEditActivity::class.java)
        startActivityForResult(intent, SUBACTIITY_REQUEST_CODE)
    }

    private fun sandemail() {
        var builder = context?.let { it1 -> AlertDialog.Builder(it1) }
        builder!!.setTitle("비밀번호 초기화")
        builder.setIcon(R.drawable.loding)

        var v1 = layoutInflater.inflate(R.layout.dialog2, null)
        builder.setView(v1)
        var listener = DialogInterface.OnClickListener { p0, p1 ->
            auth.currentUser?.email?.let { it1 ->
                auth.sendPasswordResetEmail(auth.currentUser!!.email.toString())
                    .addOnCompleteListener(){
                        if(it.isSuccessful){
                            Toast.makeText(context, "메일이 전송되었습니다.", Toast.LENGTH_SHORT).show()

                        } else{
                            Toast.makeText(context, "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
        builder.setPositiveButton("전송", listener)
        builder.setNegativeButton("취소", null)
        builder.show()
    }

    fun onSuccess(abc:String) {
        binding!!.tvNickname.text= abc
    }

    private fun nameread() {
        SettingsPresenter = SettingsPresenter(this)
        SettingsPresenter.get()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        nameread()
    }
}