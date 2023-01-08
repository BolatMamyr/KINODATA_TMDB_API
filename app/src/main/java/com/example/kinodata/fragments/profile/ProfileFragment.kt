package com.example.kinodata.fragments.profile

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
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private val TAG = "ProfileFragment"
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
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
                    if (it.success) {
                        Toast.makeText(context, "Signed In", Toast.LENGTH_SHORT).show()
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