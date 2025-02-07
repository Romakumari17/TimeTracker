package com.romakumari.timetracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.romakumari.timetracker.databinding.FragmentRegisterInBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterInFragment : Fragment() {
    lateinit var binding: FragmentRegisterInBinding
    lateinit var mainActivity: MainActivity
    lateinit var employeeDataClass: EmployeeDataClass
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        binding = FragmentRegisterInBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnregister.setOnClickListener {
            val bundle = Bundle()
            val name = binding.etUsername.text?.toString() ?: ""
            bundle.putString("name", name)
            val email = binding.etgmail.text?.toString() ?: ""
            findNavController().navigate(R.id.homeFragment, bundle)
             employeeIn()
        }
    }

       private  fun employeeIn(){
             db.collection("employee").add(

                 EmployeeDataClass( employeeName = binding.etUsername.text?.toString(), email = binding.etgmail.text?.toString()  )

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



//        // Register button click listener
//        binding.btnregister.setOnClickListener {
//            val email = binding.etgmail.text.toString()
//            val password = binding.etuserpassword.text.toString()
//
//            // Validate email and password
//            if (isValidEmail(email) && isValidPassword(password)) {
//                registerUser(email, password)
//            } else  {
////                (Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_SHORT)
////                    .show())
//                val bundle = Bundle()
//                val name = binding.etUsername.text?.toString() ?: ""
//                bundle.putString("name", name)
//                findNavController().navigate(R.id.homeFragment, bundle)
//            }
//        }
//    }
//
//    // Function to validate email format
//    private fun isValidEmail(email: String): Boolean {
//        val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
//        return email.matches(Regex(emailPattern))
//    }
//
//    // Function to validate password (must be at least 6 characters)
//    private fun isValidPassword(password: String): Boolean {
//        return password.length >= 6
//    }
//
//    // Function to register user with email and password
//    private fun registerUser(email: String, password: String) {
//        auth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener(requireActivity()) { task ->
//                if (task.isSuccessful) {
//                    // User successfully created
//                    val user = auth.currentUser
//
//                    val userData = hashMapOf(
//                        "email" to email,
//                        "password" to password, // Store password, but it's safer to hash it before storing
//                        "timestamp" to System.currentTimeMillis()
//                    )
//                }
//        }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegisterInFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterInFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}