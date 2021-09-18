package yong.aop.aop.diseaseindex.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import yong.aop.aop.diseaseindex.Fragment.MainActivity
import yong.aop.aop.diseaseindex.R
import yong.aop.aop.diseaseindex.databinding.ActivityLodingBinding

class LodingActivity :  AppCompatActivity() {
    var binding: ActivityLodingBinding? = null

    var anim_FadeIn: Animation? = null
    var anim_ball: Animation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLodingBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        anim_FadeIn = AnimationUtils.loadAnimation(this, R.anim.ani_fadein)
        anim_ball = AnimationUtils.loadAnimation(this, R.anim.ani_ball)

        binding!!.loding1.startAnimation(anim_FadeIn)
        binding!!.loding2.startAnimation(anim_ball)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.hide()
        }
        startLoading()
    }

    private fun startLoading() {
        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(baseContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}