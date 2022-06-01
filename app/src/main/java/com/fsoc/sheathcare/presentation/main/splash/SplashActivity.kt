package com.fsoc.sheathcare.presentation.main.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.fsoc.sheathcare.presentation.main.MainActivity
import com.fsoc.sheathcare.presentation.main.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.tbruyelle.rxpermissions2.RxPermissions

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val firebaseAuth = FirebaseAuth.getInstance()
        Handler().postDelayed({
            val rxPermissions = RxPermissions(this)
            rxPermissions.request(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
                .subscribe { grandted ->
                    if (grandted) {
                        if (firebaseAuth.currentUser != null) {
                            startMain()
                            print("Hello")
                        } else {
                            startLogin()
                            print("Hell")
                        }
                    }

                }


        }, 2500)
    }

    @SuppressLint("CheckResult")
    private fun startMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}