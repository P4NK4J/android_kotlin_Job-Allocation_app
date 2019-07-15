package com.example.android_kotlin_job_allocation_app.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import com.example.android_kotlin_job_allocation_app.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.requestFeature(Window.FEATURE_NO_TITLE)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            startActivity(Intent(this@SplashActivity, MainActivity :: class.java))
        }, 1000)
    }
}