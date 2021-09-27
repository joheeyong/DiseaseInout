package yong.aop.aop.diseaseindex.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import yong.aop.aop.diseaseindex.Services.NetworkStatus
import yong.aop.aop.diseaseindex.Services.NetworkStatus.getConnectivityStatus
import yong.aop.aop.diseaseindex.R
import yong.aop.aop.diseaseindex.databinding.ActivityRegisterBinding
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {
    var binding: ActivityRegisterBinding? = null

    var simpleDateFormat = SimpleDateFormat(" MMMM dd일 hh:mm")
    var date = simpleDateFormat.format(Date())
    private lateinit var auth: FirebaseAuth
    private lateinit var userDB: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        auth = Firebase.auth
        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) { actionBar.hide() }

        userDB = Firebase.database.reference.child("Users")
        val etEmailedit = binding!!.etEmailedit
        val etPassedit = binding!!.etPassedit
        val etPassedit2 = binding!!.etPassedit2
        val btnRegister = binding!!.btnRegister
        val etCreatename = binding!!.etCreatename
        val tvRule = binding!!.tvRule
        val cbRulecheck = binding!!.cbRulecheck

        binding!!.btnMovelogin.setOnClickListener {
            val intent = Intent(baseContext, LoginActivity::class.java)
            startActivity(intent)
        }
        binding!!.btnMovegoogle.setOnClickListener {
            val intent = Intent(baseContext, GoogleLoginActivity::class.java)
            startActivity(intent)
        }
        cbRulecheck.setOnClickListener {
                val enable = cbRulecheck.isChecked && etEmailedit.text.isNotEmpty() && etPassedit.text.isNotEmpty() && etPassedit2.text.isNotEmpty() && etCreatename.text.isNotEmpty()
            btnRegister.isEnabled = enable
            }
        tvRule.setOnClickListener {
                val myWebView: WebView = findViewById(R.id.webView)
                myWebView.loadUrl("https://yongprotect.blogspot.com/2021/08/disassi-disassi-30.html")
            }
        etEmailedit?.addTextChangedListener {
                val enable =
                    cbRulecheck.isChecked && etEmailedit.text.isNotEmpty() && etPassedit.text.isNotEmpty() && etPassedit2.text.isNotEmpty() && etCreatename.text.isNotEmpty()
            btnRegister.isEnabled = enable
            }
        etPassedit2?.addTextChangedListener {
                val enable =
                    cbRulecheck.isChecked && etEmailedit.text.isNotEmpty() && etPassedit.text.isNotEmpty() && etPassedit2.text.isNotEmpty() && etCreatename.text.isNotEmpty()
            btnRegister.isEnabled = enable
            }
        etCreatename?.addTextChangedListener {
                val enable =
                    cbRulecheck.isChecked && etEmailedit.text.isNotEmpty() && etPassedit.text.isNotEmpty() && etPassedit2.text.isNotEmpty() && etCreatename.text.isNotEmpty()
                btnRegister.isEnabled = enable
            }
        etPassedit?.addTextChangedListener {
                val enable =
                    cbRulecheck.isChecked && etEmailedit.text.isNotEmpty() && etPassedit.text.isNotEmpty() && etPassedit2.text.isNotEmpty() && etCreatename.text.isNotEmpty()
            btnRegister.isEnabled = enable
            }
        btnRegister.setOnClickListener {
                val status = getConnectivityStatus(applicationContext)
                if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {
                    val email = etEmailedit.text.toString()
                    val password = etPassedit.text.toString()
                    val password2 = etPassedit2.text.toString()
                    val createname = etCreatename.text.toString()
                    if (email.equals("")) {
                        Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    } else if (password.equals("")) {
                        Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    } else if (password2.equals("")) {
                        Toast.makeText(this, "비밀번호를 재입력해주세요.", Toast.LENGTH_SHORT).show()
                    } else if (!password.equals(password2)) {
                        Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                    } else if (createname.equals("")) {
                        Toast.makeText(this, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    } else {
                        if (cbRulecheck.isChecked()) {
                            auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this) { task ->
                                    if (task.isSuccessful) {
                                        val userId = auth.currentUser!!.uid
                                        val currentUserDB = userDB.child(userId)
                                        val user = mutableMapOf<String, Any>()
                                        user["userId"] = userId
                                        user["name"] = etCreatename.text.toString()
                                        simpleDateFormat = SimpleDateFormat(" yyyy. MM. dd.")
                                        date = simpleDateFormat.format(Date())
                                        user["registerdate"] = date
                                        currentUserDB.updateChildren(user)
                                        Toast.makeText(
                                            this,
                                            "회원가입을 성공했습니다. 로그인 버튼을 눌러 로그인해주세요.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        val intent = Intent(baseContext, LoginActivity::class.java)
                                        startActivity(intent)
                                    } else {
                                        Toast.makeText(
                                            this,
                                            "이미 가입한 이메일이거나, 회원가입에 실패했습니다.",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                    }
                                }
                            } else {
                                Toast.makeText(this, "이용약관에 동의하지 않았습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                Toast.makeText(this, "네트워크 연결이 끊겼습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}

