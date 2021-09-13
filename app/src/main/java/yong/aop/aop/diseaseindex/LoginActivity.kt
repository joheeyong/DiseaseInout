package yong.aop.aop.diseaseindex

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import yong.aop.aop.diseaseindex.databinding.ActivityLoginBinding
import java.text.SimpleDateFormat
import java.util.*

class LoginActivity : AppCompatActivity() {
    var binding: ActivityLoginBinding? = null

    private lateinit var auth: FirebaseAuth
    var simpleDateFormat = SimpleDateFormat(" MMMM dd일 hh:mm")
    var date = simpleDateFormat.format(Date())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) { actionBar.hide() }
        auth = Firebase.auth

        binding!!.tvLostpass.setOnClickListener { passwordreset() }

        val emailEditText = binding!!.etEmailedit
        val passwordEditText = binding!!.etPassedit
        val btnLogin = binding!!.btnLogin

        binding!!.btnMoveregster.setOnClickListener {
            val intent = Intent(baseContext, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding!!.btnMovegoogle.setOnClickListener {
            val intent = Intent(baseContext, GoogleLoginActivity::class.java)
            startActivity(intent)
        }
        binding!!.btnLogin.setOnClickListener {
            val status = NetworkStatus.getConnectivityStatus(applicationContext)
            if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()
                auth?.signInWithEmailAndPassword(email, password)
                    ?.addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            successLogin()
                        } else {
                            Toast.makeText(this, "로그인에 실패했습니다. 이메일 또는 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(this, "네트워크 연결이 끊겼습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        emailEditText?.addTextChangedListener {
            val enable = emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()
            btnLogin.isEnabled = enable
        }
        passwordEditText?.addTextChangedListener {
            val enable = emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()
            btnLogin.isEnabled = enable
        }
    }

    private fun passwordreset() {
        var builder = AlertDialog.Builder(this)
        builder .setTitle("비밀번호 초기화")
        builder.setIcon(R.drawable.loding)

        var v1 = layoutInflater.inflate(R.layout.dialog, null)
        builder.setView(v1)
        var listener = DialogInterface.OnClickListener { p0, p1 ->
            var alert = p0 as AlertDialog
            var edit1: EditText? = alert.findViewById<EditText>(R.id.et_emailsand)
            var email: String= edit1?.text.toString()
            auth?.sendPasswordResetEmail(email)
                ?.addOnCompleteListener(this){
                    if(it.isSuccessful){
                        Toast.makeText(this, "메일이 전송되었습니다.", Toast.LENGTH_SHORT).show()
                    } else{
                        Toast.makeText(this, "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        builder.setPositiveButton("전송", listener)
        builder.setNegativeButton("취소", null)
        builder.show()
    }

    private fun successLogin() {
        if (auth?.currentUser == null) {
            Toast.makeText(this, "로그인에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        val userId = auth?.currentUser?.uid.orEmpty()
        val currentUserDb = Firebase.database.reference.child("Users").child(auth.currentUser!!.uid)
        val user = mutableMapOf<String, Any>()
        user["userId"] = userId
        currentUserDb.updateChildren(user)
        simpleDateFormat = SimpleDateFormat(" yyyy. MM. dd.")
        date = simpleDateFormat.format(Date())
        currentUserDb.child("logindate").setValue(date)
        startActivity(Intent(this, MainActivity::class.java))
    }
}