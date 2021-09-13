package yong.aop.aop.diseaseindex

import android.app.Activity
import android.app.AlertDialog
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import yong.aop.aop.diseaseindex.databinding.FragmentChatlistBinding

class ChatListFragment : Fragment(R.layout.fragment_chatlist) {
    private var binding: FragmentChatlistBinding? = null
    private lateinit var database : DatabaseReference
    private val auth: FirebaseAuth by lazy { Firebase.auth }

    private var viewPager: ViewPager? = null
    private var adapterImageSlider: AdapterImageSlider? = null
    private var runnable: Runnable? = null
    private val handler = Handler()

    private var dataItems = arrayListOf(
        Image(R.drawable.sickpic,"infoflu",""),
        Image(R.drawable.kdcapic,"https://health.kdca.go.kr",""),
        Image(R.drawable.nhispic,"http://www.nhis.or.kr/nhis/healthin/retrieveMdcAdminSknsClinic.do",""),
        Image(R.drawable.poisonpic,"infopoison",""),
        Image(R.drawable.asthmapic,"infoasthma",""))

    val SUBACTIITY_REQUEST_CODE = 100
    var name: String? =null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentChatlistBinding = FragmentChatlistBinding.bind(view)
        binding = fragmentChatlistBinding

        binding!!.conTwo.setOnClickListener {
            checknet()
        }
        binding!!.btnRestart.setOnClickListener {
            restart()
        }
        binding!!.tvChatruel.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val dialogView = layoutInflater.inflate(R.layout.dialogchat_layout, null)
            builder.setView(dialogView)
                .show()
        }
        binding!!.coldroom.setOnClickListener {
            btnclick("감기")
        }
        binding!!.eyedis.setOnClickListener {
            btnclick("눈병")
        }
        binding!!.Foodpoi.setOnClickListener {
            btnclick("식중독")
        }
        binding!!.Asthma.setOnClickListener {
            btnclick("천식")
        }
        binding!!.dermatitisroom.setOnClickListener {
            btnclick("피부염")
        }
        viewPager = binding!!.pager
        adapterImageSlider = AdapterImageSlider(context as Activity, arrayListOf())
        viewPager!!.adapter = adapterImageSlider
        retrieveList(dataItems)
        viewPager!!.currentItem = 0
        addBottomDots(binding!!.llyDots, adapterImageSlider!!.count, 0)
        handleViewPager()
        startAutoSlider(adapterImageSlider!!.count)
        nameread()
        restart()
    }

    private fun checknet() {
        var status = context?.let { NetworkStatus.getConnectivityStatus(it) }
        if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {
            val intent = Intent(context, NickEditActivity::class.java)
            intent.putExtra("Key", "닉네임")
            startActivityForResult(intent, SUBACTIITY_REQUEST_CODE)
        } else {
            Toast.makeText(context, "네트워크 연결이 끊겼습니다.", Toast.LENGTH_SHORT).show() }
    }

    private fun restart() {
        val status = context?.let { NetworkStatus.getConnectivityStatus(it) }
        if (status != NetworkStatus.TYPE_MOBILE && status != NetworkStatus.TYPE_WIFI) {
            binding!!.chatListRecyclerView.visibility=View.GONE
            binding!!.card.visibility=View.GONE
            binding!!.llyOutnet.visibility=View.VISIBLE
            Toast.makeText(context, "네트워크 연결이 끊겼습니다.", Toast.LENGTH_SHORT).show()
        } else{
            binding!!.llyOutnet.visibility=View.GONE
            binding!!.card.visibility=View.VISIBLE
            binding!!.chatListRecyclerView.visibility=View.VISIBLE
            nameread()
        }
    }

    private fun btnclick(abcd : String) {
        var status = context?.let { NetworkStatus.getConnectivityStatus(it) }
        if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {
            context?.let {
                val intent = Intent(it, ChatRoomActivity::class.java)
                intent.putExtra("chatKey", abcd)
                startActivity(intent) }
        }else{
            Toast.makeText(context, "네트워크 연결이 끊겼습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        nameread()
    }

    private fun nameread() {
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(auth.currentUser!!.uid).get().addOnSuccessListener {
            if (it.exists()) {
                if(it.child("name").value.toString()!="null") {
                    name= it.child("name").value.toString()
                    binding!!.tvNickname.text=name
                }
            }
        }.addOnFailureListener{
        }
    }

    private fun retrieveList(items: List<Image>) {
        adapterImageSlider?.apply {
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
            override fun onPageSelected(pos: Int) { addBottomDots(binding!!.llyDots, adapterImageSlider!!.count, pos) }
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

}