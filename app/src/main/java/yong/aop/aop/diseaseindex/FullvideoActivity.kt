package yong.aop.aop.diseaseindex

import android.app.Activity
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.MediaController
import android.widget.VideoView


class FullvideoActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullvideo)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);

        val video_view = findViewById<VideoView>(R.id.video_view)
        if(intent.getStringExtra("Key")=="감기") {
            video_view.setVideoPath("android.resource://"+getPackageName()+"/raw/fluworry")
        } else if(intent.getStringExtra("Key")=="식중독"){
            video_view.setVideoPath("android.resource://"+getPackageName()+"/raw/poisonworry")
        }else if(intent.getStringExtra("Key")=="천식"){
            video_view.setVideoPath("android.resource://"+getPackageName()+"/raw/asthmaworry")
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
                finish()
            }
            doubleClick = true
            Handler().postDelayed({ doubleClick = false }, 1000)
        }

    }

}
