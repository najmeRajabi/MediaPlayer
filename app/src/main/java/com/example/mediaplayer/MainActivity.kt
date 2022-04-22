package com.example.mediaplayer

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.viewbinding.ViewBinding
import com.example.mediaplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnPlayPause = binding.playPauseBtn
        val btnStop = binding.stopBtn

        initViews()

        btnPlayPause.setOnClickListener { startStop() }
        btnStop.setOnClickListener { stop() }

    }

    private fun stop() {
        mediaPlayer.stop()
    }

    private fun startStop() {

        if (mediaPlayer.isPlaying){
            mediaPlayer.pause()
        }else {
            mediaPlayer.start()
            Log.d("TAG", "onCreate: ${mediaPlayer.isPlaying} ")
        }
    }

    private fun initViews() {


        val url = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3" // your URL here
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(url)
            prepare() // might take long! (for buffering, etc)



        }
        Log.d("TAG", "onCreate: ${mediaPlayer.isPlaying} ")
    }
}