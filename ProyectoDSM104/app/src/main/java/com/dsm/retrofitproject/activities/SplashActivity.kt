package com.dsm.retrofitproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.dsm.retrofitproject.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, Login1Activity::class.java)
            startActivity(intent)
            finish()
        }, 9000) // espera 2 segundos antes de pasar a MainActivity




    }
}