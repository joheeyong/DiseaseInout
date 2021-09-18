package yong.aop.aop.diseaseindex.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import yong.aop.aop.diseaseindex.Fragment.MainActivity
import yong.aop.aop.diseaseindex.Presenter.CreatePresenter
import yong.aop.aop.diseaseindex.Services.NetworkStatus
import yong.aop.aop.diseaseindex.R
import yong.aop.aop.diseaseindex.databinding.ActivityGoogleloginBinding
import java.text.SimpleDateFormat
import java.util.*

class GoogleLoginActivity : AppCompatActivity(),View.OnClickListener {
    var binding: ActivityGoogleloginBinding? = null

    private lateinit var GooglePresenter: GooglePresenter
    private lateinit var auth: FirebaseAuth
    private lateinit var googlesignclient: GoogleSignInClient
    private val RC_SIGN_IN = 99

    var simpleDateFormat = SimpleDateFormat(" yyyy. MM. dd.")
    var date = simpleDateFormat.format(Date())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoogleloginBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) { actionBar.hide() }

        binding!!.btnMovemain.setOnClickListener {
            val intent = Intent(baseContext, MainActivity::class.java)
            intent.putExtra("noregister", 97)
            startActivity(intent)
        }
        binding!!.btnMovelogin.setOnClickListener {
            val intent = Intent(baseContext, LoginActivity::class.java)
            startActivity(intent)
        }
        binding!!.btnMoveregster.setOnClickListener {
            val intent = Intent(baseContext, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding!!.btnGooglesign.setOnClickListener {
            val signInIntent = googlesignclient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
        binding!!.tvRule.setOnClickListener {
            val status = NetworkStatus.getConnectivityStatus(applicationContext)
            if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {
            val myWebView: WebView = binding!!.wvWebview
            myWebView.loadUrl("https://yongprotect.blogspot.com/2021/08/disassi-disassi-30.html")}
            else{
                Toast.makeText(this, "네트워크 연결이 끊겼습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googlesignclient = GoogleSignIn.getClient(this, gso)
        auth = FirebaseAuth.getInstance()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) { }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    toMainActivity(auth?.currentUser)
                } else {
            }
        }
    }

    fun toMainActivity(user: FirebaseUser?) {
        if(user !=null) {
            val userId = auth.currentUser?.uid.orEmpty()
            val currentUserDb = Firebase.database.reference.child("Users").child(userId)
            val user = mutableMapOf<String, Any>()
            user["userId"] = userId
            user["name"] = "처음사용자"
            user["logindate"] = date
            user["registerdate"] = date
            currentUserDb.updateChildren(user)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onClick(v: View?) {
    }

}