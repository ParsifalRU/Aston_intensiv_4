package com.example.android_intensiv_4

import android.os.Bundle
import android.util.AttributeSet
import android.util.TypedValue
import android.util.Xml
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import java.nio.file.attribute.AttributeView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val clockView = ClockView(this)

        val width = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            333f,
            resources.displayMetrics
        )
        val height = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            222f,
            resources.displayMetrics
        )

        val linearLayout = findViewById<LinearLayout>(R.id.linear_layout)
        linearLayout.addView(clockView, width.toInt(), height.toInt())
    }
}

