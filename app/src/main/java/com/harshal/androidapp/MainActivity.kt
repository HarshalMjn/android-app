package com.harshal.androidapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        count = savedInstanceState?.getInt(KEY_COUNT) ?: 0
        val counterText = findViewById<TextView>(R.id.counterText)
        val tapButton = findViewById<MaterialButton>(R.id.tapButton)

        counterText.text = getString(R.string.tap_count, count)
        tapButton.setOnClickListener {
            count++
            counterText.text = getString(R.string.tap_count, count)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_COUNT, count)
    }

    companion object {
        private const val KEY_COUNT = "count"
    }
}
