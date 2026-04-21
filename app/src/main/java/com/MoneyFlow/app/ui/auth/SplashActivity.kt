package com.MoneyFlow.app.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.MoneyFlow.app.ui.main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            delay(1500)
            val user = FirebaseAuth.getInstance().currentUser
            val intent = if (user != null)
                Intent(this@SplashActivity, MainActivity::class.java)
            else
                Intent(this@SplashActivity, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
