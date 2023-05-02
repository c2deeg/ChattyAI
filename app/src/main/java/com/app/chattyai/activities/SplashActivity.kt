package com.app.chattyai.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.app.chattyai.R
import com.app.chattyai.utils.CSPreferences
import com.app.chattyai.utils.Utils

class SplashActivity : AppCompatActivity() {
    lateinit var logoimageview: ImageView
    lateinit var tvanim: TextView
    private val str = "chatGenix AI"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        logoimageview = findViewById(R.id.logoimageview)
        tvanim = findViewById(R.id.tvanim)



        val rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_picture)
        logoimageview.startAnimation(rotate)
        val handler = Handler()

        val runnable = object : Runnable {
            var index = 0

            override fun run() {
                if (index < str.length) {
                    tvanim.append(str[index].toString())
                    index++
                    handler.postDelayed(this, 100)
                }
            }
        }



        handler.post(runnable)
        Handler(Looper.getMainLooper()).postDelayed({
            var value = CSPreferences.readString(this,Utils.BOOLEANVALUE)
            if (value.equals("true")){
                val intent2 = Intent(this, HomeActivity::class.java)
                startActivity(intent2)
            }else{
                val intent2 = Intent(this, LoginActivity::class.java)
                startActivity(intent2)
            }



        }, 2500)
    }
}