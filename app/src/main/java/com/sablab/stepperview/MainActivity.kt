package com.sablab.stepperview

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val stepper = findViewById<StepperView>(R.id.stepper)
        val next = findViewById<MaterialButton>(R.id.next)

        next.setOnClickListener {
            if (stepper.isComplete) {
                next.text = getString(R.string.next)
                stepper.restart()
            } else
                stepper.currentStep++
        }

        stepper.setOnCompleteListener {
            next.text = "Restart"
            Toast.makeText(this, "Completed", Toast.LENGTH_SHORT).show()
        }
    }
}