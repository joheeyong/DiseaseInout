package yong.aop.aop.diseaseindex.Activity

import android.R
import android.app.AlertDialog
import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import yong.aop.aop.diseaseindex.Presenter.CreatePresenter
import yong.aop.aop.diseaseindex.databinding.ActivityCreateNoteBinding
import java.text.SimpleDateFormat
import java.util.*

class CreateSaveActivity : AppCompatActivity(){
    private lateinit var binding: ActivityCreateNoteBinding

    private val auth: FirebaseAuth by lazy { Firebase.auth }
    private lateinit var createPresenter: CreatePresenter

    var simpleDateFormat = SimpleDateFormat(" MMMM dd일 hh:mm")
    var date = simpleDateFormat.format(Date())

    var disname: String ="감기"
    private var noteId: String? = null
    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_DESC = "extra_desc"
        const val EXTRA_LEVEL2 = "extra_level2"
        const val EXTRA_LEVEL3 = "extra_level3"
        const val EXTRA_DATE = "extra_date"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getSupportActionBar()?.setTitle(Html.fromHtml("<b>"+"저장"))
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        createPresenter = CreatePresenter()
        binding.rgTransport.isChecked = true
        noteId = intent.getStringExtra(EXTRA_ID)
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                yong.aop.aop.diseaseindex.R.id.rg_Transport ->  disname="감기"
                yong.aop.aop.diseaseindex.R.id.rg_lodgment ->  disname="눈병"
                yong.aop.aop.diseaseindex.R.id.rg_food ->  disname="식중독"
                yong.aop.aop.diseaseindex.R.id.rg_shopping ->  disname="천식"
                yong.aop.aop.diseaseindex.R.id.rg_tourism ->  disname="피부염"
                yong.aop.aop.diseaseindex.R.id.rg_other ->  disname="기타"
            }
        }

        if (noteId != null) {
            disname==(intent.getStringExtra(EXTRA_TITLE))
            if(intent.getStringExtra(EXTRA_TITLE).toString()=="감기"){
                binding.rgTransport.isChecked = true 
            } else if (intent.getStringExtra(EXTRA_TITLE).toString()=="눈병"){
                binding.rgLodgment.isChecked = true
            } else if (intent.getStringExtra(EXTRA_TITLE).toString()=="식중독"){
                binding.rgFood.isChecked = true
            } else if (intent.getStringExtra(EXTRA_TITLE).toString()=="천식"){
                binding.rgShopping.isChecked = true
            } else if (intent.getStringExtra(EXTRA_TITLE).toString()=="피부염"){
                binding.rgTourism.isChecked = true
            } else if (intent.getStringExtra(EXTRA_TITLE).toString()=="기타"){
                binding.rgOther.isChecked = true
            }
            binding.etLevel.setText(intent.getStringExtra(EXTRA_DESC))
            binding.etLevel2.setText(intent.getStringExtra(EXTRA_LEVEL2))
            binding.etLevel3.setText(intent.getStringExtra(EXTRA_LEVEL3))

        }
        binding.btnCreate.setOnClickListener {
            createNote()
        }
        binding.llyLeveldia.setOnClickListener {
            selectlevel()
        }
    }

    private fun createNote() {
        val title = disname
        val desc = binding.etLevel.text.toString().trim()
        val level2 = binding.etLevel2.text.toString().trim()
        val level3 = binding.etLevel3.text.toString().trim()
        val sellerId = auth.currentUser?.uid.orEmpty()
        if(intent.getStringExtra(EXTRA_DATE).toString()=="null"){
            simpleDateFormat = SimpleDateFormat(" MMMM dd일 hh:mm")
             date = simpleDateFormat.format(Date())
        } else{ date = intent.getStringExtra(EXTRA_DATE).toString() }
        createPresenter.create(noteId, title, desc, level2, level3, sellerId, date,this)
    }

    private fun selectlevel() {
        val items = arrayOf("매우 좋음","좋음","보통","나쁨","매우 나쁨")
        var selectedItem: String? = null
        val builder = AlertDialog.Builder(this)
            .setTitle("증상의 상태는 어떠신가요?")
            .setSingleChoiceItems(items, -1) { dialog, which ->
                binding!!.etLevel.text = selectedItem.toString()
                selectedItem = items[which]
            }
            .setPositiveButton("확인") { dialog, which ->
                if(selectedItem.toString()=="null"){
                }else {
                    binding!!.etLevel.text = selectedItem.toString()
                }
            }
        builder.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}