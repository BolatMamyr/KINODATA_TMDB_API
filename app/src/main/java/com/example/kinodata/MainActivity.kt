package com.example.kinodata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.example.kinodata.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    // TODO: Change Search to RecyclerView with search history using Room/DataStore?
    // TODO: add pb to FullImg, also add drag down to close
    // TODO: Sometimes not loading PersonInfo in PersonFragment
    // TODO: SearchFragment: Recommended to watch: change photos and add more data(trending?)

    // TODO: when showing keyboard it is going above bottomNav in SearchFrag
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
                R.id.movieFullImageFragment, R.id.tvFullImageFragment, R.id.personFullImageFragment,
                R.id.videoFragment -> {
                    hideBottomNav()
                    window.statusBarColor = getColor(R.color.super_light_black)
                }
                else -> {
                    showBottomNav()
                    window.statusBarColor = getColor(R.color.light_gray)
                }
            }

        }
    }

    private fun hideBottomNav() {
        binding.bottomNav.visibility = View.GONE
    }


    private fun showBottomNav() {
        binding.bottomNav.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()

        Thread {
            Glide.get(this).clearDiskCache()
        }
        Glide.get(this).clearMemory()
    }
}


