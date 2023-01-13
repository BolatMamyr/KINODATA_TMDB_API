package com.example.kinodata.fragments.profile.signIn

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.kinodata.R
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.databinding.FragmentSignInBinding

private const val TAG = "SignInFragment"
class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignInViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickSignUp()
        binding.btnSignIn.setOnClickListener {
            signIn()
        }

    }

    private fun setOnClickSignUp() {
        binding.txtSignUp.setOnClickListener {
//            val webpage: Uri = Uri.parse(MyConstants.URL_SIGN_UP)
//            val intent = Intent(Intent.ACTION_VIEW, webpage)
//            if (intent.resolveActivity(requireActivity().packageManager) != null) {
//                startActivity(intent)
//            }

            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(MyConstants.URL_SIGN_UP)
            startActivity(intent)
        }
    }

    private fun signIn() {
        binding.apply {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            if (username.isNotEmpty() && password.isNotEmpty()) {
                viewModel.signIn(username, password)
                viewModel.sessionIdResult.observe(viewLifecycleOwner) {
                    Log.d(TAG, "signIn: ${it.success}\nSession id = ${it.session_id}")
                    if (it.success) {
                        Toast.makeText(context, "Signed In", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_profileFragment_to_accountFragment)
                    } else {
                        Toast.makeText(context, "Couldn't Sign In", Toast.LENGTH_SHORT).show()
                    }
                }

                viewModel.sessionId.observe(viewLifecycleOwner) { sessionId ->
                    Log.d(TAG, "sessionId: $sessionId")
                }

            }

        }

    }

}