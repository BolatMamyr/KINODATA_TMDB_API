package com.example.kinodata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.kinodata.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    // TODO: all seeAll and BottomNav btns to orange
    // TODO: toolbars with animation
    // TODO: clickable photos
    // TODO: videos
    // TODO: implement Retrofit with Flow to get data after re-connecting to Internet
    // TODO: RemoteMediator for offline access of already loaded data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.fragmentContainerView)
        binding.bottomNav.setupWithNavController(navController)

    }


}