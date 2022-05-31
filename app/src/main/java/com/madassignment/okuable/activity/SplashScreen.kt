package com.madassignment.okuable.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.madassignment.okuable.R

class SplashScreen : AppCompatActivity() {

    private val SPLASH_TIME_OUT:Long = 2500 // 2.5 sec

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({

            startActivity(Intent(this,Login::class.java))

            finish()
        }, SPLASH_TIME_OUT)
    }
}

