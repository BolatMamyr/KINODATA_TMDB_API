package com.example.kinodata.fragments.video

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.kinodata.databinding.FragmentVideoBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo

private const val TAG = "VideoFragment"



class VideoFragment : Fragment() {

    companion object {
        private const val TIME_SAVED_INSTANCE_STATE = "time"
    }

    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<VideoFragmentArgs>()

    private var currentTime = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVideoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            currentTime = savedInstanceState.getFloat(TIME_SAVED_INSTANCE_STATE)
        }
        initPlayer()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onDestroy() {
        super.onDestroy()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putFloat(TIME_SAVED_INSTANCE_STATE, currentTime)
    }

    private fun initPlayer() {
        binding.playerView.let { playerView ->
            lifecycle.addObserver(playerView)

            playerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
                override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadOrCueVideo(lifecycle, args.videoUrl, currentTime)
                }
            })
            playerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                    currentTime = second
                }

                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(args.videoUrl, currentTime)
                }
            })
        }
    }

}