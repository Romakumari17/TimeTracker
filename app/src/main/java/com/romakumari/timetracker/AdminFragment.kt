package com.romakumari.timetracker

import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.os.Environment
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.romakumari.timetracker.databinding.EmployefilterBinding
import com.romakumari.timetracker.databinding.FragmentAdminBinding
import com.romakumari.timetracker.databinding.ItemadminlayoutBinding
import com.romakumari.timetracker.databinding.ItememployeelayoutBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
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
    var employeelist = mutableListOf<EmployeeDataClass>()
    var filterList = arrayListOf<EmployeeDataClass>()
    val REQUEST_CODE_PERMISSIONS = 1001
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var filterEmp = ""
    var filerin = ""


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
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }


        linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = linearLayoutManager
        adapter = AdminRecyclerAdapter(employeelist, this)
        binding.recyclerView.adapter = adapter
        getEmployee()
        binding.pdffa.setOnClickListener {
//            generatePdfFromRecyclerData()

        }
        binding.btnFilter.setOnClickListener {
            PopupMenu(requireContext(), it).apply {
                menuInflater.inflate(R.menu.popupmenu, menu)
                gravity = Gravity.END
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.filterIn -> Toast.makeText(
                            mainActivity,
                            "third",
                            Toast.LENGTH_SHORT
                        )
                            .show()

                        R.id.filterEmployee ->searchEmployee()


                        R.id.filterShortleave -> Toast.makeText(
                            mainActivity,
                            "third",
                            Toast.LENGTH_SHORT
                        )
                            .show()

                        R.id.filterOut -> Toast.makeText(
                            mainActivity,
                            "third",
                            Toast.LENGTH_SHORT
                        )
                            .show()
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

    private fun getEmployee() {
        employeelist.clear()
        db.collection("employee").addSnapshotListener { snapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            for (dc in snapshot!!) {
                val firestoreClass = dc.toObject(EmployeeDataClass::class.java)
                firestoreClass.id = dc.id
                employeelist.add(firestoreClass)
            }
            adapter.notifyDataSetChanged()
        }
    }
    private fun searchEmployee() {
        val dialog = Dialog(mainActivity)
        val dialogBinding = EmployefilterBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialogBinding.btnremarkadd.setOnClickListener {
            val searchName = dialogBinding.etremark.text.toString().trim()
            if (searchName.isEmpty()) {
                Toast.makeText(mainActivity, "Please enter an employee name", Toast.LENGTH_SHORT)
                    .show()
            } else {
                // Call the filter function
                filterEmployee(searchName)
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun filterEmployee(searchName: String) {
        val formattedName = searchName.trim().lowercase() // Convert search query to lowercase
        Log.d("Firestore", "Searching for employee: '$formattedName'")

        db.collection("markin")
            .whereEqualTo("employeeName", formattedName) // Ensure Firestore data is also stored in lowercase
            .get()
            .addOnSuccessListener { snapshot ->
                Log.d("Firestore", "Query size: ${snapshot.size()}")

                if (!snapshot.isEmpty) {
                    employeelist.clear()
                    for (document in snapshot.documents) {
                        val employee = document.toObject(EmployeeDataClass::class.java)
                        Log.d("Firestore", "Employee Found: ${document.data}")
                        employee?.id = document.id
                        employeelist.add(employee!!)
                    }
                    adapter.notifyDataSetChanged()

                    Toast.makeText(mainActivity, "${employeelist.size} employee(s) found", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("Firestore", "No employee found for '$formattedName'")
                    Toast.makeText(mainActivity, "No employee found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error fetching data", e)
                Toast.makeText(mainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

   


    override fun eyeclick(employeeDataClass: EmployeeDataClass, position: Int) {
        TODO("Not yet implemented")
    }
//    // Check for storage permissions
//    private fun generatePdfFromRecyclerData() {
//        val employeeList = adapter.employeeList // Assuming you have a method to get the list of employees from the adapter.
//
//        if (employeeList.isNotEmpty()) {
//            generatePDF(employeeList)  // Pass the data to your PDF generation function.
//        } else {
//            Toast.makeText(requireContext(), "No employee data to generate PDF", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//
//    private fun generatePDF(employeeList: List<EmployeeDataClass>) {
//        val document = PdfDocument()
//        val paint = Paint()
//
//        // Set page attributes
//        val pageInfo = PdfDocument.PageInfo.Builder(600, 800, 1).create()
//        val page = document.startPage(pageInfo)
//        val canvas: Canvas = page.canvas
//
//        // Set text properties
//        paint.color = Color.BLACK
//        paint.textSize = 12f
//        var yPos = 20f
//
//        // Add content (employee data) to the PDF
//        for (employee in employeeList) {
//            canvas.drawText("Employee: ${employee.employeeName}", 50f, yPos, paint)
//            yPos += 20f
//            canvas.drawText("Mark In: ${employee.datein}", 50f, yPos, paint)
//            yPos += 20f
//            canvas.drawText("Mark Out: ${employee.dateout}", 50f, yPos, paint)
//            yPos += 20f
//            canvas.drawText("Short Leave: ${employee.shortLeave}", 50f, yPos, paint)
//            yPos += 20f
//            canvas.drawText("Remarks: ${employee.remarks}", 50f, yPos, paint)
//            yPos += 40f // Add space for next employee
//        }
//
//        // End the page
//        document.finishPage(page)
//
//        // Get the app's specific external storage directory for PDFs
//        val filePath = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Employee_Attendance.pdf")
//
//        try {
//            // Write the document to the file
//            val outputStream = FileOutputStream(filePath)
//            document.writeTo(outputStream)
//            outputStream.close()
//
//            // Notify the user that the PDF was saved successfully
//            Toast.makeText(requireContext(), "PDF saved at ${filePath.path}", Toast.LENGTH_SHORT).show()
//        } catch (e: IOException) {
//            e.printStackTrace()
//            Toast.makeText(requireContext(), "Error saving PDF", Toast.LENGTH_SHORT).show()
//        }
//
//        // Close the document
//        document.close()
//
//    }


}






