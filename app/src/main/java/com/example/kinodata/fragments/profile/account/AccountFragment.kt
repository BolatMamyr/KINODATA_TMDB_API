package com.example.kinodata.fragments.profile.account

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.kinodata.R
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.databinding.FragmentAccountBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "AccountFragment"

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AccountViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signOut()
        getAccountDetails()
        setClickListeners(view)
    }

    private fun getAccountDetails() {
        viewModel.getAccountDetails()
        viewModel.accountDetails.observe(viewLifecycleOwner) { account ->
            binding.apply {
                txtUsername.text = account.username
                Glide.with(requireContext())
                    .load(MyConstants.IMG_BASE_URL + account.getAvatarPath())
                    .into(imgProfile)
            }
        }
    }

    private fun signOut() {
        binding.btnSignOut.setOnClickListener {
            viewModel.deleteSession()
            viewModel.signOutSuccess.observe(viewLifecycleOwner) {
                Log.d(TAG, "signOutSuccess: $it")
                if (it) {
                    Toast.makeText(context, getString(R.string.signed_out), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun setClickListeners(view: View) {
        binding.apply {
            btnFavorite.setOnClickListener {
            // TODO: not navigating

//                val fragmentManager = childFragmentManager
//                    .findFragmentById(R.id.profileFragmentContainer) as? NavHostFragment
//                val navController = fragmentManager?.navController
//                navController?.navigate(R.id.action_accountFragment_to_favoriteListFragment2)

            }
        }
    }

}