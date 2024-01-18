package com.example.stopwatch

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var timeTextView: TextView
    private lateinit var startButton: Button
    private lateinit var pauseButton: Button
    private lateinit var resetButton: Button

    private var startTime: Long = 0
    private var elapsedTime: Long = 0
    private var isRunning = false

    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timeTextView = findViewById(R.id.timeTextView)
        startButton = findViewById(R.id.startButton)
        pauseButton = findViewById(R.id.pauseButton)
        resetButton = findViewById(R.id.resetButton)

        startButton.setOnClickListener {
            startTimer()
        }

        pauseButton.setOnClickListener {
            pauseTimer()
        }

        resetButton.setOnClickListener {
            resetTimer()
        }
    }

    private val runnable = object : Runnable {
        override fun run() {
            val currentTime = SystemClock.elapsedRealtime() - startTime
            elapsedTime = currentTime
            val minutes = (currentTime / 60000).toInt()
            val seconds = ((currentTime % 60000) / 1000).toInt()
            val milliseconds = (currentTime % 1000).toInt()

            val timeString = String.format("%02d:%02d:%03d", minutes, seconds, milliseconds)
            timeTextView.text = timeString

            handler.postDelayed(this, 10)
        }
    }

    private fun startTimer() {
        if (!isRunning) {
            startTime = SystemClock.elapsedRealtime() - elapsedTime
            handler.post(runnable)
            isRunning = true

            startButton.isEnabled = false
            pauseButton.isEnabled = true
            resetButton.isEnabled = true
        }
    }

    private fun pauseTimer() {
        if (isRunning) {
            handler.removeCallbacks(runnable)
            isRunning = false

            startButton.isEnabled = true
            pauseButton.isEnabled = false
            resetButton.isEnabled = true
        }
    }

    private fun resetTimer() {
        handler.removeCallbacks(runnable)
        isRunning = false
        elapsedTime = 0

        timeTextView.text = "00:00:000"

        startButton.isEnabled = true
        pauseButton.isEnabled = false
        resetButton.isEnabled = false
    }
}
