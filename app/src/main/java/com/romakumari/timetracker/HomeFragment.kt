package com.romakumari.timetracker

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import com.romakumari.timetracker.databinding.FragmentHomeBinding
import com.romakumari.timetracker.databinding.FragmentRegisterInBinding
import com.romakumari.timetracker.databinding.ShortRemarkDailogBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    lateinit var binding:FragmentHomeBinding
    lateinit var mainActivity: MainActivity
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var fragmentRegisterInBinding: FragmentRegisterInBinding
     var db = FirebaseFirestore.getInstance()
    private val employeeId = ""
    private val LOCATION_PERMISSION_REQUEST_CODE = 100
    var  name = ""
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity=activity as MainActivity
        arguments?.let {
            name = it.getString("name","")
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvpersonname.setText(name)
        // Get the current date
        val today = LocalDate.now()

        // Format the date (Example: "2 February 2025")
        val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH)
        val formattedDate = today.format(formatter)

        // Set the formatted date to your TextView
        binding.tvdate.text = formattedDate


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

//         Set up button click listeners
        binding.markInButton.setOnClickListener {
            markInAttendance()

        }

        binding.markOutButton.setOnClickListener {
            markOutAttendance()

        }

        binding.shortLeaveButton.setOnClickListener {
            markShortLeave()


        }
    }
        private fun checkLocationPermission() {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            } else {
                checkLocationSettings()
            }

    }
    private fun checkLocationSettings() {
        if (!isLocationEnabled()) {
            Toast.makeText(
                requireContext(),
                "Please enable location services to use this feature.",
                Toast.LENGTH_SHORT
            ).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        } else {
            getLastLocation()
        }
    }
    private fun isLocationEnabled(): Boolean {
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
    }


    private fun markInAttendance() {

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
         {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
            return
        }

     fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
      if (location != null) {
         val currentTime =SimpleDateFormat("dd/MM/yyyy hh:mm:a").format(Calendar.getInstance().time)
            val currentLocation = getAddressFromLatLng(mainActivity, location.latitude, location.longitude)

           db.collection("markin").add(
            EmployeeDataClass(datein = currentTime, employeeName = name, location = currentLocation, markIn =true )
           )

            .addOnSuccessListener {
                Toast.makeText(mainActivity,"added",Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener {
                Toast.makeText(mainActivity, "failure", Toast.LENGTH_SHORT).show()
            }
            .addOnCanceledListener {
                Toast.makeText(mainActivity, "cancel", Toast.LENGTH_SHORT).show()

            }


           }
        }
    }
    private fun markOutAttendance() {

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val currentTimeDate =SimpleDateFormat("dd/MM/yyyy hh:mm:a").format(Calendar.getInstance().time)
                val currentLocation = getAddressFromLatLng(mainActivity, location.latitude, location.longitude)


                db.collection("markout").add(
                    EmployeeDataClass(dateout = currentTimeDate, employeeName = name ,location = currentLocation, markOut =true)

                )

                    .addOnSuccessListener {
                        Toast.makeText(mainActivity,"added",Toast.LENGTH_SHORT).show()

                    }
                    .addOnFailureListener {
                        Toast.makeText(mainActivity, "failure", Toast.LENGTH_SHORT).show()
                    }
                    .addOnCanceledListener {
                        Toast.makeText(mainActivity, "cancel", Toast.LENGTH_SHORT).show()

                    }

            }
        }
    }

    private fun markShortLeave() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val currentdateTime =SimpleDateFormat("dd/MM/yyyy hh:mm:a").format(Calendar.getInstance().time)
                val currentLocation =
                    getAddressFromLatLng(mainActivity, location.latitude, location.longitude)
                var dialog = Dialog(requireContext())
                var dialogbinding =ShortRemarkDailogBinding.inflate(layoutInflater)
                dialog.setContentView(dialogbinding.root)
                dialog.getWindow()?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

                dialogbinding.btnremarkadd.setOnClickListener {
                    val remarks= dialogbinding.etremark.text.toString().trim()
                    db.collection("markshortleave").add(
                        EmployeeDataClass(
                            dateshortleave =currentdateTime,
                            employeeName = name,
                            location = currentLocation,
                            shortLeave = true,
                            remarks = "Remarks: $remarks"
                        )

                    )
                        .addOnSuccessListener {
                            Toast.makeText(mainActivity, "added", Toast.LENGTH_SHORT).show()

                        }
                        .addOnFailureListener {
                            Toast.makeText(mainActivity, "failure", Toast.LENGTH_SHORT).show()
                        }
                        .addOnCanceledListener {
                            Toast.makeText(mainActivity, "cancel", Toast.LENGTH_SHORT).show()

                        }
                    dialog.dismiss()



                }
                dialog.show()


            }


            }


        }

    fun getAddressFromLatLng(context: Context, latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        return try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                addresses[0].getAddressLine(0) // Full address
            } else {
                "Address not found"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Unable to fetch address"
        }
    }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkLocationSettings()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Location permission is required to mark attendance.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
      private fun settime(){

      }






        companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}