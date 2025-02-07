package com.romakumari.timetracker

import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.TimePicker
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.romakumari.timetracker.databinding.EmployefilterBinding
import com.romakumari.timetracker.databinding.FragmentAdminBinding
import com.romakumari.timetracker.databinding.ItemadminlayoutBinding
import java.text.SimpleDateFormat
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AdminFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdminFragment : Fragment(),AdminIterface {
    lateinit var binding: FragmentAdminBinding
    lateinit var mainActivity: MainActivity
    var db = FirebaseFirestore.getInstance()
    lateinit var adapter: AdminRecyclerAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    var employeelist = arrayListOf<EmployeeDataClass>()
    var filterList = arrayListOf<EmployeeDataClass>()

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var filterEmp = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdminBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = linearLayoutManager
        adapter = AdminRecyclerAdapter(employeelist, this)
        binding.recyclerView.adapter = adapter
        getEmployee()
        binding.btnFilter.setOnClickListener {
            PopupMenu(requireContext(), it).apply {
                menuInflater.inflate(R.menu.popupmenu, menu)
                gravity = Gravity.END
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.filterIn ->showTimePickerDialog()
                        R.id.filterEmployee -> searchSpecificEmployee()


                        R.id.filterShortleave -> Toast.makeText(
                            mainActivity,
                            "third",
                            Toast.LENGTH_SHORT
                        )
                            .show()

                        R.id.filterOut -> showTimePickerDialog()
                    }

                    return@setOnMenuItemClickListener true

                }
                show()

            }
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AdminFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdminFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private fun getEmployee(){
        employeelist.clear()
        db.collection("employee").addSnapshotListener { snapshot, error ->
            if (error != null){
                return@addSnapshotListener
            }
            for (dc in snapshot!!){
                val firestoreClass = dc.toObject(EmployeeDataClass::class.java)
                firestoreClass.id = dc.id
                employeelist.add(firestoreClass)
            }
            adapter.notifyDataSetChanged()
        }
    }
    private fun searchSpecificEmployee() {
        var dialog = Dialog(mainActivity)
        var dialogBindinng = EmployefilterBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBindinng.root)
        dialogBindinng.btnremarkadd.setOnClickListener {
            if (dialogBindinng.etremark.text.isNullOrEmpty()){
                Toast.makeText(mainActivity,"enter the employee name",Toast.LENGTH_SHORT).show()


        }else{
                updateUI(filterList)
                adapter.notifyDataSetChanged()
            dialog.dismiss()
                filterEmployee()

                Toast.makeText(mainActivity,"employee data get",Toast.LENGTH_SHORT).show()
            }

        }
        dialog.show()

//        val searchDialog = AlertDialog.Builder(requireContext())
//        val input = EditText(requireContext())
//        searchDialog.setTitle("Enter Employee Name")
//        searchDialog.setView(input)
//        searchDialog.setPositiveButton("Search") { _, _ ->
//            filterEmp = input.text.toString().trim()
//            if (filterEmp.isNullOrEmpty()){
//            }else{
//
//            }
//            if (nameQuery.isNotEmpty()) {
//                db.collection("employee").get().addOnSuccessListener { snapshot ->
//
//                    for (document in snapshot.documents) {
//                        val employee = document.toObject(EmployeeDataClass::class.java)
//                        if (employee?.employeeName?.contains(
//                                nameQuery,
//                                ignoreCase = true
//                            ) == true
//                        ) {
//                            employee.id = document.id
//                            employeelist.add(employee)
//                            Toast.makeText(mainActivity,"click",Toast.LENGTH_SHORT).show()
//
//                        }
//
//
//
//
//                    }
//                    adapter.notifyDataSetChanged()
//
//                }.addOnFailureListener { e ->
//                    Log.e("Firestore", "Error searching employee", e)
//                }
//            } else {
//                Toast.makeText(requireContext(), "Please enter a name", Toast.LENGTH_SHORT).show()
//            }



//        searchDialog.setNegativeButton("Cancel", null)
//        searchDialog.show()
    }
//    private fun showMarkedOutEmployees() {
//        employeelist.clear() // Clear old data before updating
//
//        db.collection("employee")
//            .whereEqualTo("time", true) // Get only marked-out employees
//            .get()
//            .addOnSuccessListener { snapshot ->
//                for (document in snapshot.documents) {
//                    val employee = document.toObject(EmployeeDataClass::class.java)
//                    employee?.id = document.id
//                    employeelist.add() // Add to existing ArrayList
//                }
//
//                adapter.notifyDataSetChanged() // Update RecyclerView UI
//            }
//            .addOnFailureListener { e ->
//                Log.e("Firestore", "Error fetching marked-out employees", e)
//            }
//    }


    private fun filterEmployee() {
        employeelist.clear()
        db.collection("employee").whereEqualTo("employeeName", filterEmp).get()
            .addOnSuccessListener { snapshot ->
                for (document in snapshot!!) {
                    val employee = document.toObject(EmployeeDataClass::class.java)
                    employee?.id = document.id
                    filterList.add(employee)
                }
            adapter.notifyDataSetChanged()

        }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error fetching  employee", e)
            }
    }

    fun updateUI(newlist: ArrayList<EmployeeDataClass>) {
        employeelist.addAll(newlist)
        adapter.notifyDataSetChanged()

    }

    override fun eyeclick(employeeDataClass: EmployeeDataClass, position: Int) {

    }

//    fun onclick(){
//        var dialog = Dialog(this)
//        dialog.setContentView(R.layout.itememployeelayout)
//        dialog.getWindow()?.setLayout(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//
//    }

    private fun showTimePickerDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select Time")

        // Get the current time
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        builder.setPositiveButton("Pick Time") { _, _ ->
            // Open TimePicker when the user clicks "Pick Time"
            val timePickerDialog = TimePickerDialog(requireContext(),
                { _, selectedHour, selectedMinute ->
                    // Convert to 12-hour format
                    val amPm = if (selectedHour >= 12) "PM" else "AM"
                    val hour12 = if (selectedHour % 12 == 0) 12 else selectedHour % 12
                    val selectedTime = String.format("%02d:%02d %s", hour12, selectedMinute, amPm)

                    Toast.makeText(requireContext(), "Selected Time: $selectedTime", Toast.LENGTH_SHORT).show()
                }, hour, minute, false // false for 12-hour format (AM/PM)
            )
            timePickerDialog.show()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }


}


