package com.example.kinodata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.kinodata.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    // TODO: SearchFrag navigate to another frag when clicking on EditText
    // TODO: when showing keyboard bottomNav going up in SearchFrag
    // TODO: toolbars with animation
    // TODO: clickable photos
    // TODO: videos
    // TODO: implement Retrofit with Flow to get data after re-connecting to Internet
    // TODO: RemoteMediator for offline access of already loaded data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.fragmentContainerView)

        /*
        When reselect menuItem navigates back to home destination. Also solves problem with
        bottomNav item re-selecting while inside child fragment. Now no need in NestedGraph
         */
        binding.bottomNav.apply {
            NavigationUI.setupWithNavController(this, navController)
            setOnItemSelectedListener { item ->
                NavigationUI.onNavDestinationSelected(item, navController)
                true
            }
            setOnItemReselectedListener {
                navController.popBackStack(destinationId = it.itemId, inclusive = false)
            }

        }
    }

}