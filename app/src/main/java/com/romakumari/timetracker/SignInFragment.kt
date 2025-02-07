package com.romakumari.timetracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.romakumari.timetracker.databinding.FragmentSignInBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignInFragment : Fragment() {
    lateinit var binding: FragmentSignInBinding
    lateinit var mainActivity: MainActivity
    lateinit var navController: NavController


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity=activity as MainActivity
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
        binding= FragmentSignInBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnregister.setOnClickListener {
            findNavController().navigate(R.id.registerInFragment)
        }

        binding.btnlogin.setOnClickListener {
            val  adminusername="Admin "
            val  password="12345"
            // Check if the username is empty
            if (binding.etUsername.text.toString().isNullOrEmpty()) {
                binding.etUsername.error = "Enter Your User Name"
            }
            // Check if the password is empty
            else if (binding.etuserpassword.text.toString().isNullOrEmpty()) {
                binding.etuserpassword.error = "Enter Your Password"
            }
            // Check if the entered username is correct
            else if (binding.etUsername.text.toString() != adminusername) {
                Toast.makeText(mainActivity, "Username is incorrect", Toast.LENGTH_SHORT).show()
            }
            // Check if the entered password is correct
            else if (binding.etuserpassword.text.toString() != password) {
                Toast.makeText(mainActivity, "Password is incorrect", Toast.LENGTH_SHORT).show()
            }
            // Proceed to the next fragment if username and password are correct
            else {
                val bundle = Bundle()
                val name = binding.etUsername.text.toString()  // Get the username text
                bundle.putString("name", name)
                findNavController().navigate(R.id.adminFragment, bundle)
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
         * @return A new instance of fragment SignInFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignInFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}