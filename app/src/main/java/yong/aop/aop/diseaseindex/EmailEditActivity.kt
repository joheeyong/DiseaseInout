package yong.aop.aop.diseaseindex

import android.R
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import yong.aop.aop.diseaseindex.databinding.*

class EmailEditActivity : AppCompatActivity() {

    private var binding: ActivityEmaileditBinding? = null

    private lateinit var database : DatabaseReference
    private val auth: FirebaseAuth by lazy { Firebase.auth }
    var name: String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmaileditBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        getSupportActionBar()?.setTitle(Html.fromHtml("<b>"+"이메일"))
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        nameread()
    }

    private fun nameread() {
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(auth.currentUser!!.uid).get().addOnSuccessListener {
            if (it.exists()) {
                if(it.child("name").value.toString()!="null") {
                    name= it.child("name").value.toString()
                    binding!!.tvEmail.text=auth.currentUser!!.email
                    }
                }
            }.addOnFailureListener{
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