package yong.aop.aop.diseaseindex.Fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
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
//    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
//    private var cancellationTokenSource: CancellationTokenSource? = null

    var lat: Double? = null
    var lng: Double? = null
    var etArea: String? =null
    var disst: String =""
    var name: String? =null
    var znCd: Int=0
    var dissCd: Int=1
//    var list: List<Address>? = null

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
            view?.let { it1 -> Snackbar.make(it1, "?????? ????????? ????????? ?????? ????????????", Snackbar.LENGTH_LONG).show() }
//            restart()
//            val status = context?.let { NetworkStatus.getConnectivityStatus(it) }
//            if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {
//                initVariables()
//                requestLocationPermissions()
//                gpsinfo()
//            }
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
            view?.let { it1 -> Snackbar.make(it1, "???????????? ????????? ???????????????", Snackbar.LENGTH_LONG).show() }
        }
    }


    private fun selectarea() {
        val items = arrayOf("???????????????", "???????????????", "???????????????", "???????????????","???????????????","???????????????","???????????????","?????????","?????????","????????????","????????????"
            ,"????????????","????????????","????????????","????????????","?????????")
        var selectedItem: String? = null
        val builder = AlertDialog.Builder(context)
        builder.setNegativeButton("??????", null)
            .setTitle("????????? ???????????????")
            .setSingleChoiceItems(items, -1) { dialog, which ->
                selectedItem = items[which]
            }
            .setPositiveButton("??????") { dialog, which ->
                if(selectedItem.toString()=="null"){
                }else {
                    binding!!.etArea.text = selectedItem.toString()
                }
            }
        builder.show()

    }

    private fun selectsick() {
        val items = arrayOf("??????", "??????", "?????????", "??????","?????????")
        var selectedItem: String? = null
        val builder = AlertDialog.Builder(context)
        builder.setNegativeButton("??????", null)
            .setTitle("????????? ???????????????")
            .setSingleChoiceItems(items, -1) { dialog, which ->
                selectedItem = items[which]
            }
            .setPositiveButton("??????") { dialog, which ->
                if(selectedItem.toString()=="null"){

                }else {
                    binding!!.etSick.text = selectedItem.toString()
                }
            }
        builder.show()

    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) { super.onRequestPermissionsResult(requestCode, permissions, grantResults) }

//    private fun initVariables() {
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
//    }

//    private fun requestLocationPermissions() {
//        requestPermissions(
//            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
//                    Manifest.permission.ACCESS_FINE_LOCATION),
//                    REQUEST_BACKGROUND_ACCESS_LOCATION_PERMISSIONS
//        )
//    }

//    private fun selectgps() {
//        cancellationTokenSource = CancellationTokenSource()
//        if (context?.let {
//                ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION)
//            } != PackageManager.PERMISSION_GRANTED && context?.let {
//                ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION)
//            } != PackageManager.PERMISSION_GRANTED)
//            {
//                return }
//        fusedLocationProviderClient
//            .getCurrentLocation(
//                LocationRequest.PRIORITY_HIGH_ACCURACY,
//                cancellationTokenSource!!.token
//            ).addOnSuccessListener { location ->
//                val geocoder = Geocoder(context)
//                lat = location.latitude
//                lng = location.longitude
//                list = geocoder.getFromLocation(lat!!, lng!!, 10)
//                etArea = (list as MutableList<Address>?)?.get(0)?.adminArea.toString()
//                binding!!.etArea.text = etArea
//                binding!!.tvInfodis.text =
//                "" + binding!!.etArea.text + "??? " + binding!!.etSick.text + " ???????????? "
//        }
//    }

    private fun textsave() {
        etArea = binding!!.etArea.text.toString()
        disst = binding!!.etSick.text.toString()
    }

//    @SuppressLint("MissingPermission")
//    private fun gpsinfo() {
//        selectgps()
//        work()
//    }

    @SuppressLint("MissingPermission")
    private fun searchinfo() {
        textsave()
        work()
    }

    private fun work() {
        if (etArea.equals("???????????????")) {
            znCd = 11
            name = "????????????"
        } else if (etArea.equals("???????????????")) {
            znCd = 26
            name = "?????????"
        } else if (etArea.equals("???????????????")) {
            znCd = 27
            name = "?????????"
        } else if (etArea.equals("???????????????")) {
            znCd = 28
            name = "?????????"
        } else if (etArea.equals("???????????????")) {
            znCd = 29
            name = "?????????"
        } else if (etArea.equals("???????????????")) {
            znCd = 30
            name = "?????????"
        } else if (etArea.equals("???????????????")) {
            znCd = 31
            name = "?????????"
        } else if (etArea.equals("?????????")) {
            znCd = 41
            name = "?????????"
        } else if (etArea.equals("?????????")) {
            znCd = 42
            name = "?????????"
        } else if (etArea.equals("????????????")) {
            znCd = 43
            name = "?????????"
        } else if (etArea.equals("????????????")) {
            znCd = 44
            name = "?????????"
        } else if (etArea.equals("????????????")) {
            znCd = 45
            name = "?????????"
        } else if (etArea.equals("????????????")) {
            znCd = 46
            name = "????????????"
        } else if (etArea.equals("????????????")) {
            znCd = 47
            name = "?????????"
        } else if (etArea.equals("????????????")) {
            znCd = 48
            name = "?????????"
        } else if (etArea.equals("?????????")) {
            znCd = 49
            name = "?????????"
        } else {
            znCd = 99
            name = "null"
        }
        binding!!.tvInfodis.text = etArea + "??? " + binding!!.etSick.text + " ????????????"
        if (disst.equals("??????")) {
            dissCd = 1
        } else if (disst.equals("??????")) {
            dissCd = 2
        } else if (disst.equals("?????????")) {
            dissCd = 3
        } else if (disst.equals("??????")) {
            dissCd = 4
        } else if (disst.equals("?????????")) {
            dissCd = 5
        }
        scope.launch {
            try {
                val mmeasuredValue =
                    Repository.getDisease(dissCd,znCd)
                val measuredValue =
                    Repository.getAir(name!!)
                binding!!.q2.text="???????????? "+ name + "??????, " +binding!!.etArea.text.toString()+"?????????."
                QualityData(mmeasuredValue!!, measuredValue!!)
                binding!!.progressBar.visibility = View.GONE
            } catch (exception: Exception) {
                exception.printStackTrace()
                binding!!.progressBar.visibility = View.VISIBLE
                view?.let { it1 -> Snackbar.make(it1, "???????????? ????????? ???????????????", Snackbar.LENGTH_LONG).show() }
            } finally {
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun QualityData(DisInfo: DisInfo, airInfo: AirInfo) {
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
            binding!!.labelTextView.text = "?????????????????? : "+cnt.toString()+"???"
            binding!!.gradeTextView.text = dissRiskXpln
        }
    }

//    companion object {
//        const val REQUEST_BACKGROUND_ACCESS_LOCATION_PERMISSIONS = 101
//    }
}


