package yong.aop.aop.diseaseindex.Adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.balysv.materialripple.MaterialRippleLayout
import com.bumptech.glide.Glide
import yong.aop.aop.diseaseindex.Activity.InfofluActivity
import yong.aop.aop.diseaseindex.Model.Image
import yong.aop.aop.diseaseindex.R


class ImageSlidAdapter(private val act: Activity, var items: ArrayList<Image>) : PagerAdapter() {
    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener { fun onItemClick(view: View?, obj: Image?) }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }
    override fun getCount(): Int {
        return items.size
    }
    fun setItems(items: List<Image>) {
        this.items.apply {
            clear()
            addAll(items)
        }
    }
    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val obj: Image = items[position]
        val inflater = act.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_image_slider, container, false)
        val image = view.findViewById<View>(R.id.image) as ImageView
        val lytParent: MaterialRippleLayout = view.findViewById<View>(R.id.lyt_parent) as MaterialRippleLayout
        val myWebView: WebView = view.findViewById(R.id.webView)
        obj.image.let {
            Glide.with(act)
                .load(it)
                .into(image)
        }
        lytParent.setOnClickListener { v ->
            if (onItemClickListener != null) {
                onItemClickListener!!.onItemClick(v, obj)
            }
            if(obj.name.toString().equals("https://health.kdca.go.kr")){
                myWebView.loadUrl(obj.name.toString())
            } else if(obj.name.toString().equals("http://www.nhis.or.kr/nhis/healthin/retrieveMdcAdminSknsClinic.do")){
                myWebView.loadUrl(obj.name.toString())
            } else if(obj.name.toString().equals("infoflu")){
                val intent = Intent(container.rootView?.context, InfofluActivity::class.java)
                intent.putExtra("Key", "감기")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(container.rootView.context,intent,null)
            } else if(obj.name.toString().equals("infopoison")){
                val intent = Intent(container.rootView?.context, InfofluActivity::class.java)
                intent.putExtra("Key", "식중독")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(container.rootView.context,intent,null)
            } else if(obj.name.toString().equals("infoasthma")){
                val intent = Intent(container.rootView?.context, InfofluActivity::class.java)
                intent.putExtra("Key", "천식")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(container.rootView.context,intent,null)
            }
        }
        (container as ViewPager).addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        (container as ViewPager).removeView(obj as RelativeLayout)
    }
}