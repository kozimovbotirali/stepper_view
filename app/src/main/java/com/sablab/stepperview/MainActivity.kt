package com.sablab.stepperview

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        start()
    }

    private fun start() {
        val stepper = findViewById<StepperView>(R.id.stepper)

        stepper.stepSize = 9
        lifecycleScope.launchWhenCreated {
            for (i in 1..9) {
                delay(200)
                stepper.currentStep++
            }

            for (i in 9 downTo 2) {
                delay(200)
                stepper.currentStep--
            }
        }

        stepper.setOnCompleteListener {
            Toast.makeText(this, "Completed", Toast.LENGTH_SHORT).show()
        }
    }
}