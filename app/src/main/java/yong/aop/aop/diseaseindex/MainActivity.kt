package yong.aop.aop.diseaseindex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private  val auth: FirebaseAuth = FirebaseAuth.getInstance()
    var def: Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getSupportActionBar()?.setTitle(Html.fromHtml("<b>"+"정보"))
        val DissearchFragment = DissearchFragment()
        val chatListFragment = ChatListFragment()
        val myPageFragment = SettingsFragment()
        val beforeFragment = InfoFragment()
        val afterFragment = AfterFragment()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        replaceFragment(DissearchFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    replaceFragment(DissearchFragment)
                    getSupportActionBar()?.setTitle(Html.fromHtml("<b>"+"조회"))
                }
                R.id.chatList -> {
                    replaceFragment(chatListFragment)
                    getSupportActionBar()?.setTitle(Html.fromHtml("<b>"+"채팅"))
                }
                R.id.infobefore -> {
                    replaceFragment(beforeFragment)
                    getSupportActionBar()?.setTitle(Html.fromHtml("<b>"+"정보"))
                }
                R.id.infoafter -> {
                    replaceFragment(afterFragment)
                    getSupportActionBar()?.setTitle(Html.fromHtml("<b>"+"저장"))
                }
                R.id.myPage -> {

                        replaceFragment(myPageFragment)
                        getSupportActionBar()?.setTitle(Html.fromHtml("<b>" + "마이페이지"))
                    }


            }
            true
        }

    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.fragmentContainer, fragment)
                commit()
            }


    }
    override fun onStart() {
        super.onStart()
        if (intent.hasExtra("noregister"))
        {
           def = intent.getIntExtra("noregister",0)
        }
        if (auth.currentUser == null && def!=97)
            startActivity(Intent(this, GoogleLoginActivity::class.java))
        }
    }




