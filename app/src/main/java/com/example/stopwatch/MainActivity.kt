package com.example.stopwatch

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import java.text.MessageFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var textView: TextView
    private lateinit var reset: MaterialButton
    private lateinit var start: MaterialButton
    private lateinit var stop: MaterialButton
    private var seconds = 0
    private var minutes = 0
    private var milliSeconds = 0
    private var millisecondTime: Long = 0
    private var startTime: Long = 0
    private var timeBuff: Long = 0
    private var updateTime: Long = 0
    private val handler = Handler(Looper.getMainLooper())

    private val runnable: Runnable = object : Runnable {
        override fun run() {
            millisecondTime = SystemClock.uptimeMillis() - startTime
            updateTime = timeBuff + millisecondTime
            seconds = (updateTime / 1000).toInt()
            minutes = seconds / 60
            seconds %= 60
            milliSeconds = (updateTime % 1000).toInt()

            textView.text = MessageFormat.format(
                "{0}:{1}:{2}",
                minutes,
                String.format(Locale.getDefault(), "%02d", seconds),
                String.format(Locale.getDefault(), "%01d", milliSeconds)
            )
            handler.postDelayed(this, 0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.timeTV)
        reset = findViewById(R.id.resetButton)
        start = findViewById(R.id.startButton)
        stop = findViewById(R.id.stopButton)

        start.setOnClickListener {
            startTime = SystemClock.uptimeMillis()
            handler.postDelayed(runnable, 0)
            reset.isEnabled = false
            stop.isEnabled = true
            start.isEnabled = false
        }

        stop.setOnClickListener {
            timeBuff += millisecondTime
            handler.removeCallbacks(runnable)
            reset.isEnabled = true

            stop.isEnabled = false
            start.isEnabled = true
        }

        reset.setOnClickListener {
            millisecondTime = 0
            startTime = 0
            timeBuff = 0
            updateTime = 0
            seconds = 0
            minutes = 0
            milliSeconds = 0
            textView.text = "00:00:00"
        }

        textView.text = "00:00:00"
    }
}
