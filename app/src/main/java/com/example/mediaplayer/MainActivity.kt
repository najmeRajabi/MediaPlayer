package com.example.mediaplayer

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.AudioRecord
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.PackageManagerCompat.LOG_TAG
import androidx.viewbinding.ViewBinding
import com.example.mediaplayer.databinding.ActivityMainBinding
import java.io.IOException

private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var mediaPlayer: MediaPlayer
    lateinit var btnPlayPause:Button
    lateinit var auodioRecorder : AudioRecord
    var player: MediaPlayer? = null

    private var recorder: MediaRecorder? = null
    private var fileName: String = ""
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
    private var permissionToRecordAccepted = false
    var mStartRecording = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnPlayPause = binding.playPauseBtn
        val btnStop = binding.stopBtn
        val btnRecord = binding.recordBtn
        val btnRecordPlay = binding.recordPlayBtn


        initViews()

        fileName = "${externalCacheDir?.absolutePath}/audiorecordtest.3gp"

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)

        btnPlayPause.setOnClickListener { startStop() }
        btnStop.setOnClickListener { stop() }
        btnRecord.setOnClickListener { record() }
        btnRecordPlay.setOnClickListener {
            startPlayingRecorder()
        }

    }

    private fun onPlay()  {

        if (mStartRecording) {
            startPlaying()
        } else {
//                Toast.makeText(this,"stop", Toast.LENGTH_SHORT).show()
            stopPlaying()
        }
        mStartRecording = !mStartRecording

    }

    private fun startPlayingRecorder() {
        onPlay()
    }

    private fun startPlaying() {
        player = MediaPlayer().apply {
            try {
                setDataSource(fileName)
                prepare()
                start()
            } catch (e: IOException) {
                Log.e("LOG_TAG", "prepare() failed")
            }
        }
    }

    private fun stopPlaying() {
        player?.release()
        player = null
    }

    private fun record() {




        if (permissionToRecordAccepted){



            if (mStartRecording) {
                startRecording()
            } else {
//                Toast.makeText(this,"stop", Toast.LENGTH_SHORT).show()
                stopRecording()
            }
            mStartRecording = !mStartRecording

        }



    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }

    fun startRecording() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
            } catch (e: IOException) {
                Log.e("LOG_TAG", "prepare() failed ${e.message
                }")
            }

            start()
        }
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED

        } else {
            false
        }
        if (!permissionToRecordAccepted)
            Toast.makeText(this,"nok",Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(this,"ok",Toast.LENGTH_SHORT).show()
    }
}