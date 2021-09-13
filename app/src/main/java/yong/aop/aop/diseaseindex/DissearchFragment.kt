package yong.aop.aop.diseaseindex

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.res.ColorStateList
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import yong.aop.aop.diseaseindex.Json.Gradee
import yong.aop.aop.diseaseindex.Json.nice
import yong.aop.aop.diseaseindex.Json2.Grade
import yong.aop.aop.diseaseindex.Json2.MeasuredValue
import yong.aop.aop.diseaseindex.databinding.FragmentDissearchBinding
import yong.aop.aop.diseaseindex.service.Repository

class DissearchFragment : Fragment(R.layout.fragment_dissearch){
    var lat: Double = 0.0
    var lng: Double = 0.0
    var abc: String =""
    var w: String ="서울특별시"
    var m: String =""
    var name: String="중구"
    var def: Int=0
    var list: List<Address>? = null
    var zzz: Int=1
    var king: String=""
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var cancellationTokenSource: CancellationTokenSource? = null
    private var binding: FragmentDissearchBinding? = null
    private val scope = MainScope()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentHomeBinding = FragmentDissearchBinding.bind(view)
        binding = fragmentHomeBinding
        fetchAirQualityData()

        binding!!.btnRestart.setOnClickListener {
            fetchAirQualityData()
            restart()
        }
        binding!!.tvInfodis.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val dialogView = layoutInflater.inflate(R.layout.dialog_layout, null)
            builder.setView(dialogView)
                .show()
        }
        binding!!.tvInfoair.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val dialogView = layoutInflater.inflate(R.layout.dialog2_layout, null)
            builder.setView(dialogView)
                .show()
        }
        binding!!.btnAll.setOnClickListener {
            restart()
            val status = context?.let { NetworkStatus.getConnectivityStatus(it) }
            if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {
                initVariables()
                requestLocationPermissions()
                fetchAirQualityDataa()
            }
        }

        binding!!.llyArea.setOnClickListener {
            selectarea()
        }

        binding!!.llySick.setOnClickListener {
            selectsick()
        }

        binding!!.btnArea.setOnClickListener {
            restart()
            val status = context?.let { NetworkStatus.getConnectivityStatus(it) }
            if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {
                w = binding!!.etArea.text.toString()
                m = binding!!.etSick.text.toString()
                if (w.equals("서울특별시")) {
                    def = 11
                    name = "중구"
                } else if (w.equals("부산광역시")) {
                    def = 26
                    name = "재송동"
                } else if (w.equals("대구광역시")) {
                    def = 27
                    name = "만촌동"
                } else if (w.equals("인천광역시")) {
                    def = 28
                    name = "삼산동"
                } else if (w.equals("광주광역시")) {
                    def = 29
                    name = "경안동"
                } else if (w.equals("대전광역시")) {
                    def = 30
                    name = "문평동"
                } else if (w.equals("울산광역시")) {
                    def = 31
                    name = "약사동"
                } else if (w.equals("경기도")) {
                    def = 41
                    name = "정자동"
                } else if (w.equals("강원도")) {
                    def = 42
                    name = "홍천읍"
                } else if (w.equals("충청북도")) {
                    def = 43
                    name = "괴산읍"
                } else if (w.equals("충청남도")) {
                    def = 44
                    name = "동문동"
                } else if (w.equals("전라북도")) {
                    def = 45
                    name = "팔복동"
                } else if (w.equals("전라남도")) {
                    def = 46
                    name = "빛가람동"
                } else if (w.equals("경상북도")) {
                    def = 47
                    name = "명륜동"
                } else if (w.equals("경상남도")) {
                    def = 48
                    name = "의령읍"
                } else if (w.equals("제주도")) {
                    def = 49
                    name = "이도동"
                } else {
                    def = 99
                    name = "null"
                }
                binding!!.tvInfodis.text = w + "의 " + binding!!.etSick.text + " 예측지수"
                if (m.equals("감기")) {
                    zzz = 1
                } else if (m.equals("눈병")) {
                    zzz = 2
                } else if (m.equals("식중독")) {
                    zzz = 3
                } else if (m.equals("천식")) {
                    zzz = 4
                } else if (m.equals("피부염")) {
                    zzz = 5
                }
                scope.launch {
                    binding!!.errorDescriptionTextView.visibility = View.GONE
                    binding!!.errorDescriptionTextVieww.visibility = View.GONE
                    binding!!.btnRestart.visibility = View.GONE
                    try {

                        val mmeasuredValue =
                            Repository.ggetLatestAirQualityData(zzz, def)

                        val measuredValue =
                            Repository.getLatestAirQualityData(name)
                        binding!!.q2.text =
                            "측정소는 " + name + "이며, " + binding!!.etArea.text.toString() + "입니다."
                        displayAirQualityData(mmeasuredValue!!, measuredValue!!)
                    } catch (exception: Exception) {
                        exception.printStackTrace()
                        binding!!.errorDescriptionTextView.visibility = View.VISIBLE
                        binding!!.errorDescriptionTextVieww.visibility = View.VISIBLE
                        binding!!.btnRestart.visibility = View.VISIBLE
                        binding!!.contentsLayout.alpha = 0F
                    } finally {
                        binding!!.progressBar.visibility = View.GONE

                    }
                }
            }
        }
        restart()
    }
    private fun restart() {
        val status = context?.let { NetworkStatus.getConnectivityStatus(it) }
        if (status != NetworkStatus.TYPE_MOBILE && status != NetworkStatus.TYPE_WIFI) {
            binding!!.zxcv.visibility=View.GONE
            binding!!.btnAlll.visibility=View.GONE
            binding!!.fmnv.visibility=View.GONE
            binding!!.fmnvv.visibility=View.GONE
            binding!!.errorDescriptionTextView.visibility = View.VISIBLE
            binding!!.errorDescriptionTextVieww.visibility = View.VISIBLE
            binding!!.btnRestart.visibility = View.VISIBLE
//            binding!!.llyOutnet.visibility=View.VISIBLE
            Toast.makeText(context, "네트워크 연결이 끊겼습니다.", Toast.LENGTH_SHORT).show()
        } else{
            binding!!.zxcv.visibility=View.VISIBLE
            binding!!.btnAlll.visibility=View.VISIBLE
            binding!!.fmnv.visibility=View.VISIBLE
            binding!!.fmnvv.visibility=View.VISIBLE
            binding!!.errorDescriptionTextView.visibility = View.GONE
            binding!!.errorDescriptionTextVieww.visibility = View.GONE
            binding!!.btnRestart.visibility = View.GONE
//            binding!!.llyOutnet.visibility=View.GONE

        }
    }


    private fun selectarea() {
        val items = arrayOf("서울특별시", "부산광역시", "대구광역시", "인천광역시","광주광역시","대전광역시","울산광역시","경기도","강원도","충청북도","충청남도"
            ,"전라북도","전라남도","경상북도","경상남도","제주도")
        var selectedItem: String? = null
        val builder = AlertDialog.Builder(context)
        builder.setNegativeButton("취소", null)
            .setTitle("지역을 선택하세요")
            .setSingleChoiceItems(items, -1) { dialog, which ->
                selectedItem = items[which]
            }
            .setPositiveButton("확인") { dialog, which ->
                if(selectedItem.toString()=="null"){
                }else {
                    binding!!.etArea.text = selectedItem.toString()
                }
            }
        builder.show()

    }

    private fun selectsick() {
        val items = arrayOf("감기", "눈병", "식중독", "천식","피부염")
        var selectedItem: String? = null
        val builder = AlertDialog.Builder(context)
        builder.setNegativeButton("취소", null)
            .setTitle("질병을 선택하세요")
            .setSingleChoiceItems(items, -1) { dialog, which ->
                selectedItem = items[which]
            }
            .setPositiveButton("확인") { dialog, which ->
                if(selectedItem.toString()=="null"){

                }else {
                    binding!!.etSick.text = selectedItem.toString()
                }
            }
        builder.show()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun initVariables() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
    }
    private fun requestLocationPermissions() {
        requestPermissions(
            arrayOf(                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_BACKGROUND_ACCESS_LOCATION_PERMISSIONS
        )
    }
    @SuppressLint("MissingPermission")
    private fun fetchAirQualityDataa() {
        cancellationTokenSource = CancellationTokenSource()


        fusedLocationProviderClient
            .getCurrentLocation(
                LocationRequest.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource!!.token
            ).addOnSuccessListener { location ->

                val geocoder = Geocoder(context)

                lat = location.latitude
                lng = location.longitude
                list = geocoder.getFromLocation(lat, lng, 10)
                abc = (list as MutableList<Address>?)?.get(0)?.adminArea.toString()
                binding!!.etArea.text = abc
                binding!!.tvInfodis.text =
                    "" + binding!!.etArea.text + "의 " + binding!!.etSick.text + " 예측지수 "
                if (abc.equals("서울특별시")) {
                    def = 11
                    name="중구"
                } else if (abc.equals("부산광역시")) {
                    def = 26
                    name="재송동"
                } else if (abc.equals("대구광역시")) {
                    def = 27
                    name="만촌동"
                } else if (abc.equals("인천광역시")) {
                    def = 28
                    name="삼산동"
                } else if (abc.equals("광주광역시")) {
                    def = 29
                    name="경안동"
                } else if (abc.equals("대전광역시")) {
                    def = 30
                    name="문평동"
                } else if (abc.equals("울산광역시")) {
                    def = 31
                    name="약사동"
                } else if (abc.equals("경기도")) {
                    def = 41
                    name="정자동"
                } else if (abc.equals("강원도")) {
                    def = 42
                    name="홍천읍"
                } else if (abc.equals("충청북도")) {
                    def = 43
                    name="괴산읍"
                } else if (abc.equals("충청남도")) {
                    def = 44
                    name="동문동"
                } else if (abc.equals("전라북도")) {
                    def = 45
                    name="팔복동"
                } else if (abc.equals("전라남도")) {
                    def = 46
                    name="빛가람동"
                } else if (abc.equals("경상북도")) {
                    def = 47
                    name="명륜동"
                } else if (abc.equals("경상남도")) {
                    def = 48
                    name="의령읍"
                } else if (abc.equals("제주도")) {
                    def = 49
                    name="이도동"
                } else {
                    def = 99
                    name="null"
                }



                scope.launch {
                    binding!!.errorDescriptionTextView.visibility = View.GONE
                    binding!!.errorDescriptionTextVieww.visibility = View.GONE
                    binding!!.btnRestart.visibility = View.GONE
                    try {

                        val mmeasuredValue =
                            Repository.ggetLatestAirQualityData(zzz,def)

                        val measuredValue =
                            Repository.getLatestAirQualityData(name)
                        binding!!.q2.text="측정소는 "+ name + "이며, " +binding!!.etArea.text.toString()+"입니다."
                        displayAirQualityData(mmeasuredValue!!, measuredValue!!)
                    } catch (exception: Exception) {
                        exception.printStackTrace()
                        binding!!.errorDescriptionTextView.visibility = View.VISIBLE
                        binding!!.errorDescriptionTextVieww.visibility = View.VISIBLE
                        binding!!.btnRestart.visibility = View.VISIBLE
                        binding!!.contentsLayout.alpha = 0F
                    } finally {
                        binding!!.progressBar.visibility = View.GONE

                    }
                }


            }
    }


    @SuppressLint("MissingPermission")
    private fun fetchAirQualityData() {

        abc="서울특별시"
        binding!!.etArea.text = abc
        binding!!.tvInfodis.text =
            "" + binding!!.etArea.text + "의 " + binding!!.etSick.text + " 예측지수 "
        if (abc.equals("서울특별시")) {
            def = 11
            name="중구"
        } else if (abc.equals("부산광역시")) {
            def = 26
            name="재송동"
        } else if (abc.equals("대구광역시")) {
            def = 27
            name="만촌동"
        } else if (abc.equals("인천광역시")) {
            def = 28
            name="삼산동"
        } else if (abc.equals("광주광역시")) {
            def = 29
            name="경안동"
        } else if (abc.equals("대전광역시")) {
            def = 30
            name="문평동"
        } else if (abc.equals("울산광역시")) {
            def = 31
            name="약사동"
        } else if (abc.equals("경기도")) {
            def = 41
            name="정자동"
        } else if (abc.equals("강원도")) {
            def = 42
            name="홍천읍"
        } else if (abc.equals("충청북도")) {
            def = 43
            name="괴산읍"
        } else if (abc.equals("충청남도")) {
            def = 44
            name="동문동"
        } else if (abc.equals("전라북도")) {
            def = 45
            name="팔복동"
        } else if (abc.equals("전라남도")) {
            def = 46
            name="빛가람동"
        } else if (abc.equals("경상북도")) {
            def = 47
            name="명륜동"
        } else if (abc.equals("경상남도")) {
            def = 48
            name="의령읍"
        } else if (abc.equals("제주도")) {
            def = 49
            name="이도동"
        } else {
            def = 99
            name="null"

        }
        scope.launch {
            binding!!.errorDescriptionTextView.visibility = View.GONE
            binding!!.errorDescriptionTextVieww.visibility = View.GONE
            binding!!.btnRestart.visibility = View.GONE
            try {

                val mmeasuredValue =
                    Repository.ggetLatestAirQualityData(zzz,def)

                val measuredValue =
                    Repository.getLatestAirQualityData(name)
                binding!!.q2.text="측정소는 "+ name + "이며, " +binding!!.etArea.text.toString()+"입니다."
                displayAirQualityData(mmeasuredValue!!, measuredValue!!)
            } catch (exception: Exception) {
                exception.printStackTrace()
                binding!!.errorDescriptionTextView.visibility = View.VISIBLE
                binding!!.errorDescriptionTextVieww.visibility = View.VISIBLE
                binding!!.btnRestart.visibility = View.VISIBLE
                binding!!.contentsLayout.alpha = 0F
            } finally {
                binding!!.progressBar.visibility = View.GONE

            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun displayAirQualityData(nice: nice, measuredValue: MeasuredValue) {
        binding!!.contentsLayout.animate()
            .alpha(1F)
            .start()
        binding!!.q1.text=binding!!.etArea.text.toString()
        with(measuredValue) {
            binding!!.qwer3.text =
                "${(pm10Grade ?: Grade.UNKNOWN).label}"
            binding!!.qwer4.text =
                "${(pm25Grade ?: Grade.UNKNOWN).label}"
            binding!!.qwer41.text=
                "${(so2Grade ?: Grade.UNKNOWN).label}"
            binding!!.qwerw.text=
                "${(coGrade ?: Grade.UNKNOWN).label}"
            binding!!.qwerr.text=
                "${(o3Grade ?: Grade.UNKNOWN).label}"
            binding!!.qwers.text=
                "${(no2Grade ?: Grade.UNKNOWN).label}"
        }
//        binding!!.qwer4.setTextColor(ContextCompat.getColor(requireContext(), grade.colorResIdd))
//        binding!!.qwer41.setTextColor(ContextCompat.getColor(requireContext(), grade.colorResIdd))
//        binding!!.qwerw.setTextColor(ContextCompat.getColor(requireContext(), grade.colorResIdd))
//        binding!!.qwerr.setTextColor(ContextCompat.getColor(requireContext(), grade.colorResIdd))
//        binding!!.qwers.setTextColor(ContextCompat.getColor(requireContext(), grade.colorResIdd))
        (measuredValue.pm10Grade ?: Grade.UNKNOWN).let { grade ->
            binding!!.llyBackcolor.backgroundTintList= ColorStateList.valueOf(ContextCompat.getColor(requireContext(), grade.colorResId))
            binding!!.qwer3.setTextColor(ContextCompat.getColor(requireContext(), grade.colorResIdd))
        }
        (measuredValue.pm25Grade ?: Grade.UNKNOWN).let { grade ->
            binding!!.qwer4.setTextColor(ContextCompat.getColor(requireContext(), grade.colorResIdd))
        }
        (measuredValue.so2Grade ?: Grade.UNKNOWN).let { grade ->
            binding!!.qwer41.setTextColor(ContextCompat.getColor(requireContext(), grade.colorResIdd))
        }
        (measuredValue.coGrade ?: Grade.UNKNOWN).let { grade ->
            binding!!.qwerw.setTextColor(ContextCompat.getColor(requireContext(), grade.colorResIdd))
        }
        (measuredValue.o3Grade ?: Grade.UNKNOWN).let { grade ->
            binding!!.qwerr.setTextColor(ContextCompat.getColor(requireContext(), grade.colorResIdd))
        }
        (measuredValue.no2Grade ?: Grade.UNKNOWN).let { grade ->
            binding!!.qwers.setTextColor(ContextCompat.getColor(requireContext(), grade.colorResIdd))
        }
        (nice.risk ?: Gradee.UNKNOWN).let { gradee ->
            binding!!.llyGauge.setBackgroundResource(gradee.colorResId)
            binding!!.valueTextView.setTextColor(ContextCompat.getColor(requireContext(), gradee.colorResIdd))
        }
        with(nice) {
            binding!!.valueTextView.text = risk.toString()
            binding!!.labelTextView.text = "예측진료건수 : "+cnt.toString()+"건"
            binding!!.gradeTextView.text = dissRiskXpln

        }

    }

    companion object {
        const val REQUEST_BACKGROUND_ACCESS_LOCATION_PERMISSIONS = 101
    }
}


