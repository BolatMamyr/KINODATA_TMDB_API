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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.kinodata.R
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.databinding.FragmentProfileBinding
import com.example.kinodata.repo.DataStoreRepository
import com.example.kinodata.utils.NetworkState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ProfileFragment"

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    private var sessionId = ""
    private var accountId = 0

    // DataStore which has isSignedIn pref. If true shows layout_profileInfo, else layout_signIn
    @Inject
    lateinit var dataStoreRepository: DataStoreRepository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLayout()

        setClickListenersForSignInLayout()
        setClickListenersForProfileInfoLayout()


    }

    private fun showLayout() {
        lifecycleScope.launch {
            dataStoreRepository.sessionId.collectLatest { id ->
                // Not null means there is a sessionId and user is signed in. then show profileInfo
                Log.d(TAG, "sessionId: $id")
                if (id != "null") {
                    showProfileInfoLayout()
                    getAccountDetails(id)
                    binding.btnSignOut.setOnClickListener {
                        signOut(id)
                    }

                } else {
                    sessionId = id
                    showSignInLayout()
                }
            }
        }
    }

    private fun setClickListenersForProfileInfoLayout() {
        binding.apply {
            btnFavorite.setOnClickListener {
                val action = ProfileFragmentDirections
                    .actionProfileFragmentToFavoriteListFragment(
                        accountId = accountId,
                        sessionId = sessionId
                    )
                findNavController().navigate(action)
            }

            btnWatchList.setOnClickListener {
                val action = ProfileFragmentDirections.actionProfileFragmentToWatchlistFragment(
                    accountId = accountId,
                    sessionId = sessionId
                )
                findNavController().navigate(action)
            }

        }
    }

    private fun signOut(sessionId: String) {
        viewModel.deleteSession(sessionId)
        viewModel.signOutNetworkState.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkState.Loading -> showProgressBar()
                is NetworkState.Success -> {
                    Toast.makeText(
                        context, getString(R.string.signed_out),
                        Toast.LENGTH_SHORT
                    ).show()
                    showSignInLayout()
                }
                else -> {
                    Toast.makeText(
                        context, getString(R.string.couldntSignOut),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setClickListenersForSignInLayout() {
        binding.apply {
            btnSignIn.setOnClickListener {
                signIn()
            }
            txtSignUp.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(MyConstants.URL_SIGN_UP)
                startActivity(intent)
            }
        }
    }

    private fun getAccountDetails(sessionId: String) {
        viewModel.getAccountDetails(sessionId)
        viewModel.accountDetails.observe(viewLifecycleOwner) { accountDetails ->
            binding.apply {
                txtUsername.text = accountDetails.username
                Log.d(TAG, "accountId: ${accountDetails.id}")
                accountId = accountDetails.id
                Glide.with(requireContext())
                    .load(MyConstants.IMG_BASE_URL + accountDetails.getAvatarPath())
                    .into(imgProfile)
            }
        }
    }

    private fun signIn() {
        binding.apply {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            if (username.isNotEmpty() && password.isNotEmpty()) {
                viewModel.signIn(username, password)
                viewModel.signInNetworkState.observe(viewLifecycleOwner) {
                    when (it) {
                        is NetworkState.Loading -> showProgressBar()
                        is NetworkState.Success -> {
                            Toast.makeText(
                                context, getString(R.string.signedIn),
                                Toast.LENGTH_SHORT
                            ).show()
                            showProfileInfoLayout()
                        }
                        else -> {
                            // else error
                            hideProgressBar()
                            Toast.makeText(
                                context,
                                getString(R.string.couldntSignIn),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                }
            } else {
                // TODO: Error: please fill out all req fields
            }
        }
    }

    private fun showProgressBar() {
        binding.pbProfile.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.pbProfile.visibility = View.GONE
    }

    private fun showSignInLayout() {
        hideProgressBar()
        binding.layoutProfileInfo.visibility = View.GONE
        binding.layoutSignIn.visibility = View.VISIBLE
    }

    private fun showProfileInfoLayout() {
        hideProgressBar()
        binding.layoutProfileInfo.visibility = View.VISIBLE
        binding.layoutSignIn.visibility = View.GONE
    }


}