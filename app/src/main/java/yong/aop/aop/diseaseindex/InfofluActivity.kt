package yong.aop.aop.diseaseindex

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


class InfofluActivity : AppCompatActivity() {

    var key: String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_infoflu)
        getSupportActionBar()?.setTitle(Html.fromHtml("<b>" + intent.getStringExtra("Key")))
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        key= intent.getStringExtra("Key").toString()
        val video_view = findViewById<VideoView>(R.id.video_view)
        if(intent.getStringExtra("Key")=="감기") {
            video_view.setVideoPath("android.resource://"+getPackageName()+"/raw/fluworry")
            findViewById<LinearLayout>(R.id.lly_flu).visibility=View.VISIBLE
            findViewById<LinearLayout>(R.id.lly_flu2).visibility=View.VISIBLE
        } else if(intent.getStringExtra("Key")=="식중독"){
            video_view.setVideoPath("android.resource://"+getPackageName()+"/raw/poisonworry")
            findViewById<LinearLayout>(R.id.lly_poison).visibility=View.VISIBLE
            findViewById<LinearLayout>(R.id.lly_poison2).visibility=View.VISIBLE
        }else if(intent.getStringExtra("Key")=="천식"){
            video_view.setVideoPath("android.resource://"+getPackageName()+"/raw/asthmaworry")
            findViewById<LinearLayout>(R.id.lly_asthma).visibility=View.VISIBLE
            findViewById<LinearLayout>(R.id.lly_asthma2).visibility=View.VISIBLE
        }
        findViewById<Button>(R.id.btn_moveflu).setOnClickListener {
            val intent = Intent(baseContext, InfomationActivity::class.java)
            intent.putExtra("Key", "감기")
            startActivity(intent)
        }
    findViewById<Button>(R.id.btn_movepoison).setOnClickListener {
        val intent = Intent(baseContext, InfomationActivity::class.java)
        intent.putExtra("Key", "식중독")
        startActivity(intent)
    }
    findViewById<Button>(R.id.btn_moveasthma).setOnClickListener {
        val intent = Intent(baseContext, InfomationActivity::class.java)
        intent.putExtra("Key", "천식")
        startActivity(intent)
    }
    
        var mediaController = MediaController(this)
        mediaController.requestFocus()
        video_view.setOnPreparedListener(MediaPlayer.OnPreparedListener { mediaController.show(0) })
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
