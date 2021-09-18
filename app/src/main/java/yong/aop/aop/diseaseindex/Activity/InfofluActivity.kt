package yong.aop.aop.diseaseindex.Activity

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import yong.aop.aop.diseaseindex.R
import yong.aop.aop.diseaseindex.databinding.ActivityInfofluBinding


class InfofluActivity : AppCompatActivity() {
    var binding: ActivityInfofluBinding? = null
    var key: String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfofluBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        getSupportActionBar()?.setTitle(Html.fromHtml("<b>" + intent.getStringExtra("Key")))
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        key= intent.getStringExtra("Key").toString()

        val video_view = binding!!.videoView
        if(intent.getStringExtra("Key")=="감기") {
            video_view.setVideoPath("android.resource://"+getPackageName()+"/raw/fluworry")
            binding!!.llyFlu.visibility=View.VISIBLE
            binding!!.llyFlu2.visibility=View.VISIBLE
        } else if(intent.getStringExtra("Key")=="식중독"){
            video_view.setVideoPath("android.resource://"+getPackageName()+"/raw/poisonworry")
            binding!!.llyPoison.visibility=View.VISIBLE
            binding!!.llyPoison2.visibility=View.VISIBLE
        }else if(intent.getStringExtra("Key")=="천식"){
            video_view.setVideoPath("android.resource://"+getPackageName()+"/raw/asthmaworry")
            binding!!.llyAsthma.visibility=View.VISIBLE
            binding!!.llyAsthma2.visibility=View.VISIBLE
        }

    binding!!.btnMoveflu.setOnClickListener {
        val intent = Intent(baseContext, InfomationActivity::class.java)
        intent.putExtra("Key", "감기")
        startActivity(intent)
    }
        binding!!.btnMovepoison.setOnClickListener {
        val intent = Intent(baseContext, InfomationActivity::class.java)
        intent.putExtra("Key", "식중독")
        startActivity(intent)
    }
        binding!!.btnMoveasthma.setOnClickListener {
        val intent = Intent(baseContext, InfomationActivity::class.java)
        intent.putExtra("Key", "천식")
        startActivity(intent)
    }
    
        var mediaController = MediaController(this)
        mediaController.requestFocus()
        video_view.setOnPreparedListener({ mediaController.show(0) })
        mediaController.setAnchorView(video_view)
        video_view.setMediaController(mediaController)
        video_view.start()

        var doubleClick: Boolean? = false
        video_view.setOnClickListener {
            if (doubleClick!!) {
                val intent = Intent(baseContext, FullvideoActivity::class.java)
                intent.putExtra("Key", key)
                startActivity(intent)
            }
            doubleClick = true
            Handler().postDelayed({ doubleClick = false }, 1000)
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
