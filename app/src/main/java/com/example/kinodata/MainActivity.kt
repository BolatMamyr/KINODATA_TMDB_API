package com.example.kinodata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.kinodata.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    // TODO: Change height of images in AllImages frags
    // TODO: SearchFrag navigate to another frag when clicking on EditText !!!!!!!!!!!!
    // TODO: photos(clickable) and videos. Photos of persons !!!!!!!!!!!
    // TODO: SearchFragment: Recommended to watch: change photos and add more data(trending?) !!!!!!!!!!!

    // TODO: when showing keyboard bottomNav going up in SearchFrag
    // TODO: toolbars with animation
    // TODO: implement Retrofit with Flow to get data after re-connecting to Internet
    // TODO: RemoteMediator for offline access of already loaded data

    private lateinit var binding: ActivityMainBinding

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

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.movieFullImageFragment, R.id.tvFullImageFragment, R.id.personFullImageFragment -> {
                    hideBottomNav()
                    //TODO: change System color (on top) to dark
                }
            }
            else -> showBottomNav()
        }
    }

}

private fun hideBottomNav() {
    binding.bottomNav.visibility = View.GONE
}


private fun showBottomNav() {
    binding.bottomNav.visibility = View.VISIBLE
}
}