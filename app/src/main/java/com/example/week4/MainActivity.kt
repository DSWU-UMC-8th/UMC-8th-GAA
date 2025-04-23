package com.example.week4

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.week4.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var isRunning = false
    private var startTime = 0L
    private var timeBuffer = 0L
    private val handler = Handler(Looper.getMainLooper())

    private val updateTime = object : Runnable {
        override fun run() {
            if(isRunning) {
                val elapsedTime = System.currentTimeMillis() - startTime + timeBuffer
                val minutes = (elapsedTime / 1000) / 60
                val seconds = (elapsedTime / 1000) % 60
                val milliseconds = (elapsedTime % 1000) / 10

                binding.timeTxt.text = String.format("%02d:%02d.%02d", minutes, seconds, milliseconds)

                handler.postDelayed(this, 10)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.startBtn.setOnClickListener {
            if(!isRunning) {
                startTime = System.currentTimeMillis()
                isRunning = true
                binding.startBtn.text = "Pause"
                handler.post(updateTime)
            } else {
                timeBuffer += System.currentTimeMillis() - startTime
                isRunning = false
                binding.startBtn.text = "Start"
            }
        }

        binding.clearBtn.setOnClickListener {
            isRunning = false
            timeBuffer = 0L
            binding.timeTxt.text = "00:00.00"
        }
    }

}