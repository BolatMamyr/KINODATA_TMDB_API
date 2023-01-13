package com.example.kinodata.fragments.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.kinodata.R
import com.example.kinodata.databinding.FragmentProfileBinding
import com.example.kinodata.fragments.profile.account.AccountFragment
import com.example.kinodata.fragments.profile.signIn.SignInFragment
import com.example.kinodata.repo.DataStoreRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

// TODO: Not showing anything

private const val TAG = "ProfileFragment"

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

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
        val signInFrag = SignInFragment()
        val accountFrag = AccountFragment()

        val fragmentTransaction = childFragmentManager.beginTransaction()

        lifecycleScope.launch {
            dataStoreRepository.isSignedIn.collectLatest { isSignedIn ->
                if (isSignedIn) {
                    fragmentTransaction.replace(R.id.viewpager_profile, accountFrag)
                } else {
                    fragmentTransaction.replace(R.id.viewpager_profile, signInFrag)
                }
            }
        }


    }


}