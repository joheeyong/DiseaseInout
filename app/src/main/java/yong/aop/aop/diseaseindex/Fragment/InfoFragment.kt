package yong.aop.aop.diseaseindex.Fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import yong.aop.aop.diseaseindex.*
import yong.aop.aop.diseaseindex.Activity.InfomationActivity
import yong.aop.aop.diseaseindex.Adapter.ImageSlidAdapter
import yong.aop.aop.diseaseindex.Model.Image
import yong.aop.aop.diseaseindex.Presenter.InfoPresenter
import yong.aop.aop.diseaseindex.Presenter.SettingsPresenter
import yong.aop.aop.diseaseindex.databinding.FragmentInfoBinding
import yong.aop.aop.diseaseindex.Services.NetworkStatus
import java.util.*

class InfoFragment : Fragment(R.layout.fragment_info) {
    private var binding: FragmentInfoBinding? = null

    private lateinit var infoPresenter: InfoPresenter
//    private lateinit var database : DatabaseReference
    private var viewPager: ViewPager? = null
    private var imageSlidAdapter: ImageSlidAdapter? = null
    private var runnable: Runnable? = null
    private val handler = Handler()

    private var dataItems = arrayListOf(
        Image(R.drawable.sickpic,"infoflu",""),
        Image(R.drawable.kdcapic,"https://health.kdca.go.kr",""),
        Image(R.drawable.nhispic,"http://www.nhis.or.kr/nhis/healthin/retrieveMdcAdminSknsClinic.do",""),
        Image(R.drawable.poisonpic,"infopoison",""),
        Image(R.drawable.asthmapic,"infoasthma","")
    )

//    var random = Random()
//    var disarray= arrayOf("감기")
//    var starray= arrayOf("개요","개요-경과 및 예후","개요-병태생리","개요-원인","개요-정의","역학 및 통계","위험요인 및 예방","자주하는 질문","증상","진단 및 검사","치료","치료-약물 치료","합병증")
//    var i = random.nextInt(1)
//    var j = random.nextInt(13)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentInfoBinding.bind(view)

        infoPresenter = InfoPresenter(this)

        binding!!.btnRestart.setOnClickListener {
            restart()
        }
        binding!!.relMovecold.setOnClickListener {
            btnclick("감기")
        }
        binding!!.relMoveeye.setOnClickListener {
            btnclick("눈병")
        }
        binding!!.relMovepoison.setOnClickListener {
            btnclick("식중독")
        }
        binding!!.relMoveryzer.setOnClickListener {
            btnclick("천식")
        }
        binding!!.relMoveskin.setOnClickListener {
            btnclick("아토피피부염")
        }
        binding!!.relMoveskin2.setOnClickListener {
            btnclick("지루 피부염")
        }
        readData()

        viewPager = binding!!.pager
        imageSlidAdapter = ImageSlidAdapter(context as Activity, arrayListOf())
        viewPager!!.adapter = imageSlidAdapter
        retrieveList(dataItems)
        viewPager!!.currentItem = 0
        addBottomDots(binding!!.llyDots, imageSlidAdapter!!.count, 0)
        handleViewPager()
        startAutoSlider(imageSlidAdapter!!.count)
        restart()
    }

    private fun btnclick(workgo : String) {
        var status = context?.let { NetworkStatus.getConnectivityStatus(it) }
        if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {
            context?.let {
                val intent = Intent(it, InfomationActivity::class.java)
                intent.putExtra("Key", workgo)
                startActivity(intent)
            }
        }else{
                Toast.makeText(context, "네트워크 연결이 끊겼습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun restart() {
        val status = context?.let { NetworkStatus.getConnectivityStatus(it) }
        if (status != NetworkStatus.TYPE_MOBILE && status != NetworkStatus.TYPE_WIFI) {
            binding!!.llyOutnet.visibility= View.VISIBLE
            binding!!.card.visibility= View.GONE
            binding!!.llyBefore1.visibility= View.GONE
            binding!!.llyBefore2.visibility= View.GONE
            binding!!.llyBefore3.visibility= View.GONE
            binding!!.llyBefore4.visibility= View.GONE
            Toast.makeText(context, "네트워크 연결이 끊겼습니다.", Toast.LENGTH_SHORT).show()
        } else{
            binding!!.llyOutnet.visibility= View.GONE
            binding!!.card.visibility= View.VISIBLE
            binding!!.llyBefore1.visibility= View.VISIBLE
            binding!!.llyBefore2.visibility= View.VISIBLE
            binding!!.llyBefore3.visibility= View.VISIBLE
            binding!!.llyBefore4.visibility= View.VISIBLE
        }
    }

    private fun retrieveList(items: List<Image>) {
        imageSlidAdapter?.apply {
            setItems(items)
            notifyDataSetChanged()
        }
    }

    private fun handleViewPager() {
        viewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                pos: Int,
                positionOffset: Float,
                positionOffsetPixels: Int){}
            override fun onPageSelected(pos: Int) { addBottomDots(binding!!.llyDots, imageSlidAdapter!!.count, pos) }
            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun addBottomDots(llDots: LinearLayout?, size: Int, current: Int) {
        val dots = arrayOfNulls<ImageView>(size)
        llDots!!.removeAllViews()
        for (i in dots.indices) {
            dots[i] = ImageView(context)
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams(15, 15))
            params.setMargins(10, 10, 10, 10)
            dots[i]!!.layoutParams = params
            dots[i]!!.setImageResource(R.drawable.shape_circle_outline)
            llDots.addView(dots[i])
        }
        if (dots.isNotEmpty()) {
            dots[current]!!.setImageResource(R.drawable.shape_circle)
        }
    }

    private fun startAutoSlider(count: Int) {
        runnable = Runnable {
            var pos = viewPager!!.currentItem
            pos += 1
            if (pos >= count) pos = 0
            viewPager!!.currentItem = pos
            handler.postDelayed(runnable!!, 3000)
        }
        handler.postDelayed(runnable!!, 3000)
    }

    override fun onDestroy() {
        if (runnable != null) handler.removeCallbacks(runnable!!)
        super.onDestroy()
    }

    private fun readData() {
        infoPresenter.get("감기")
    }

    fun onSuccess(disst: String, starray: String) {
        binding!!.tvFrgdis.text="감기"+"의 "+starray
        binding!!.tvInfodiss1.text=disst
    }

}