package yong.aop.aop.diseaseindex.Fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import yong.aop.aop.diseaseindex.Jsondis.Gradee
import yong.aop.aop.diseaseindex.Jsondis.DisInfo
import yong.aop.aop.diseaseindex.Jsonair.Grade
import yong.aop.aop.diseaseindex.Jsonair.AirInfo
import yong.aop.aop.diseaseindex.Services.NetworkStatus
import yong.aop.aop.diseaseindex.R
import yong.aop.aop.diseaseindex.databinding.FragmentDissearchBinding
import yong.aop.aop.diseaseindex.Services.Repository

class DissearchFragment : Fragment(R.layout.fragment_dissearch){
    private var binding: FragmentDissearchBinding? = null

    private val scope = MainScope()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var cancellationTokenSource: CancellationTokenSource? = null

    var lat: Double? = null
    var lng: Double? = null
    var etArea: String? =null
    var disst: String =""
    var name: String? =null
    var znCd: Int=0
    var dissCd: Int=1
    var list: List<Address>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentHomeBinding = FragmentDissearchBinding.bind(view)
        binding = fragmentHomeBinding
        searchinfo()

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
                gpsinfo()
            }
        }
        binding!!.llyArea.setOnClickListener { selectarea() }
        binding!!.llySick.setOnClickListener { selectsick() }
        binding!!.btnArea.setOnClickListener {
            restart()
            val status = context?.let { NetworkStatus.getConnectivityStatus(it) }
            if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {
                searchinfo()
            }
        }
    }

    private fun restart() {
        val status = context?.let { NetworkStatus.getConnectivityStatus(it) }
        if (status != NetworkStatus.TYPE_MOBILE && status != NetworkStatus.TYPE_WIFI) {
            Toast.makeText(context, "네트워크 연결이 끊겼습니다.", Toast.LENGTH_SHORT).show()
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
    ) { super.onRequestPermissionsResult(requestCode, permissions, grantResults) }

    private fun initVariables() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
    }

    private fun requestLocationPermissions() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_BACKGROUND_ACCESS_LOCATION_PERMISSIONS
        )
    }

    private fun selectgps() {
        cancellationTokenSource = CancellationTokenSource()
        if (context?.let {
                ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION)
            } != PackageManager.PERMISSION_GRANTED && context?.let {
                ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION)
            } != PackageManager.PERMISSION_GRANTED)
            {
                return }
        fusedLocationProviderClient
            .getCurrentLocation(
                LocationRequest.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource!!.token
            ).addOnSuccessListener { location ->
                val geocoder = Geocoder(context)
                lat = location.latitude
                lng = location.longitude
                list = geocoder.getFromLocation(lat!!, lng!!, 10)
                etArea = (list as MutableList<Address>?)?.get(0)?.adminArea.toString()
                binding!!.etArea.text = etArea
                binding!!.tvInfodis.text =
                "" + binding!!.etArea.text + "의 " + binding!!.etSick.text + " 예측지수 "
        }
    }

    private fun textsave() {
        etArea = binding!!.etArea.text.toString()
        disst = binding!!.etSick.text.toString()
    }

    @SuppressLint("MissingPermission")
    private fun gpsinfo() {
        selectgps()
        work()
    }

    @SuppressLint("MissingPermission")
    private fun searchinfo() {
        textsave()
        work()
    }

    private fun work() {
        if (etArea.equals("서울특별시")) {
            znCd = 11
            name = "청계천로"
        } else if (etArea.equals("부산광역시")) {
            znCd = 26
            name = "재송동"
        } else if (etArea.equals("대구광역시")) {
            znCd = 27
            name = "만촌동"
        } else if (etArea.equals("인천광역시")) {
            znCd = 28
            name = "삼산동"
        } else if (etArea.equals("광주광역시")) {
            znCd = 29
            name = "경안동"
        } else if (etArea.equals("대전광역시")) {
            znCd = 30
            name = "문평동"
        } else if (etArea.equals("울산광역시")) {
            znCd = 31
            name = "약사동"
        } else if (etArea.equals("경기도")) {
            znCd = 41
            name = "정자동"
        } else if (etArea.equals("강원도")) {
            znCd = 42
            name = "홍천읍"
        } else if (etArea.equals("충청북도")) {
            znCd = 43
            name = "괴산읍"
        } else if (etArea.equals("충청남도")) {
            znCd = 44
            name = "동문동"
        } else if (etArea.equals("전라북도")) {
            znCd = 45
            name = "팔복동"
        } else if (etArea.equals("전라남도")) {
            znCd = 46
            name = "빛가람동"
        } else if (etArea.equals("경상북도")) {
            znCd = 47
            name = "명륜동"
        } else if (etArea.equals("경상남도")) {
            znCd = 48
            name = "의령읍"
        } else if (etArea.equals("제주도")) {
            znCd = 49
            name = "이도동"
        } else {
            znCd = 99
            name = "null"
        }
        binding!!.tvInfodis.text = etArea + "의 " + binding!!.etSick.text + " 예측지수"
        if (disst.equals("감기")) {
            dissCd = 1
        } else if (disst.equals("눈병")) {
            dissCd = 2
        } else if (disst.equals("식중독")) {
            dissCd = 3
        } else if (disst.equals("천식")) {
            dissCd = 4
        } else if (disst.equals("피부염")) {
            dissCd = 5
        }
        scope.launch {
            try {
                val mmeasuredValue =
                    Repository.ggetLatestAirQualityData(dissCd,znCd)
                val measuredValue =
                    Repository.getLatestAirQualityData(name!!)
                binding!!.q2.text="측정소는 "+ name + "이며, " +binding!!.etArea.text.toString()+"입니다."
                displayAirQualityData(mmeasuredValue!!, measuredValue!!)
                binding!!.progressBar.visibility = View.GONE
            } catch (exception: Exception) {
                exception.printStackTrace()
                binding!!.progressBar.visibility = View.VISIBLE
                Toast.makeText(context, "네트워크 연결이 끊겼습니다.", Toast.LENGTH_SHORT).show()
            } finally {
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun displayAirQualityData(DisInfo: DisInfo, airInfo: AirInfo) {
        binding!!.contentsLayout.animate()
            .alpha(1F)
            .start()
        binding!!.q1.text=binding!!.etArea.text.toString()
        with(airInfo) {
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
        (airInfo.pm10Grade ?: Grade.UNKNOWN).let { grade ->
            binding!!.llyBackcolor.backgroundTintList= ColorStateList.valueOf(ContextCompat.getColor(requireContext(), grade.colorResId))
            binding!!.qwer3.setTextColor(ContextCompat.getColor(requireContext(), grade.colorResIdd))
            binding!!.ivDisbimg.setBackgroundResource(grade.colorResIddd)
        }
        (airInfo.pm25Grade ?: Grade.UNKNOWN).let { grade ->
            binding!!.qwer4.setTextColor(ContextCompat.getColor(requireContext(), grade.colorResIdd))
        }
        (airInfo.so2Grade ?: Grade.UNKNOWN).let { grade ->
            binding!!.qwer41.setTextColor(ContextCompat.getColor(requireContext(), grade.colorResIdd))
        }
        (airInfo.coGrade ?: Grade.UNKNOWN).let { grade ->
            binding!!.qwerw.setTextColor(ContextCompat.getColor(requireContext(), grade.colorResIdd))
        }
        (airInfo.o3Grade ?: Grade.UNKNOWN).let { grade ->
            binding!!.qwerr.setTextColor(ContextCompat.getColor(requireContext(), grade.colorResIdd))
        }
        (airInfo.no2Grade ?: Grade.UNKNOWN).let { grade ->
            binding!!.qwers.setTextColor(ContextCompat.getColor(requireContext(), grade.colorResIdd))
        }
        (DisInfo.risk ?: Gradee.UNKNOWN).let { gradee ->
            binding!!.llyGauge.setBackgroundResource(gradee.colorResId)
            binding!!.valueTextView.setTextColor(ContextCompat.getColor(requireContext(), gradee.colorResIdd))
        }
        with(DisInfo) {
            binding!!.valueTextView.text = risk.toString()
            binding!!.labelTextView.text = "예측진료건수 : "+cnt.toString()+"건"
            binding!!.gradeTextView.text = dissRiskXpln
        }
    }

    companion object {
        const val REQUEST_BACKGROUND_ACCESS_LOCATION_PERMISSIONS = 101
    }
}


