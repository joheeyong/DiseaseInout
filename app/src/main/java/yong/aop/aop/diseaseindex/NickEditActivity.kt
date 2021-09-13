package yong.aop.aop.diseaseindex

import android.R
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import yong.aop.aop.diseaseindex.databinding.ActivityLoginBinding
import yong.aop.aop.diseaseindex.databinding.ActivityNickeditBinding

class NickEditActivity : AppCompatActivity() {
    var binding: ActivityNickeditBinding? = null

    private val auth: FirebaseAuth by lazy { Firebase.auth }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNickeditBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        getSupportActionBar()?.setTitle(Html.fromHtml("<b>"+intent.getStringExtra("Key")))
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)


        intent.getStringExtra("Key")?.let { twins(it) }
    }

    private fun twins(key : String) {
        if(key=="닉네임"){
            binding!!.tvMaintv.text="닉네임 변경"
            binding!!.btnEditnick.text="닉네임 변경"
            binding!!.tvDate124.text="닉네임은 앱에서 사용되는 사용자의 이름이며 채팅방에서 닉네임이 사용됩니다. 닉네임은 언제든 변경이 가능합니다."
            binding!!.etEditnick.hint="변경할 닉네임을 입력해 주세요 (최소 2자)"
            binding!!.btnEditnick.setOnClickListener {
                val status = NetworkStatus.getConnectivityStatus(applicationContext)
                if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {
                    if (binding!!.etEditnick.length() < 2) {
                        Toast.makeText(this, "2자 이상을 입력해 주세요.", Toast.LENGTH_SHORT).show()
                    } else {
                        val currentUserDb =
                            Firebase.database.reference.child("Users").child(auth.currentUser!!.uid)
                        currentUserDb.child("name").setValue(binding!!.etEditnick.text.toString())
                        finish()
                    }
                }else{
                    Toast.makeText(this, "네트워크 연결이 끊겼습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }else if(key=="상태메세지"){
            binding!!.tvMaintv.text="상태메세지"
            binding!!.btnEditnick.text="상태메세지 변경"
            binding!!.tvDate124.text="상태메세지는 사용자의 상태를 대변하며 다른사용자도 확인이 가능합니다. 상태메세지는 언제든 변경이 가능합니다."
            binding!!.etEditnick.hint="상태메세지를 입력해 주세요"
            binding!!.btnEditnick.setOnClickListener {
                val status = NetworkStatus.getConnectivityStatus(applicationContext)
                if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {
                    val currentUserDb =
                        Firebase.database.reference.child("Users").child(auth.currentUser!!.uid)
                    currentUserDb.child("statemsg").setValue(binding!!.etEditnick.text.toString())
                    finish()
                }else{
                    Toast.makeText(this, "네트워크 연결이 끊겼습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                val returnIntent = Intent()
                returnIntent.putExtra("returnValue", "name")
                setResult(100, returnIntent)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}