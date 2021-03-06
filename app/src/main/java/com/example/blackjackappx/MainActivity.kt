package com.example.blackjackappx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.blackjackappx.entities.Game
import kotlinx.coroutines.test.withTestContext

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvLoginError = findViewById<TextView>(R.id.tvLoginError)
        val tvLogoutMessage = findViewById<TextView>(R.id.tvLogoutMessage)

        val databaseHelper = DatabaseHelper(this)
        databaseHelper.initiateDatabase()

        if(intent.hasExtra("logoutMessage")) {
            tvLogoutMessage.text = intent.getStringExtra("logoutMessage").toString()
        }

        btnLogin.setOnClickListener{
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            val isInDatabase = databaseHelper.isInDatabase(username, password)

            val intent = Intent(this, BettingActivity::class.java)

            if (isInDatabase) {
                intent.putExtra("username", username)
                intent.putExtra("password", password)
                startActivity(intent)
            } else {
                tvLoginError.text = "Login failed, user does not exist"
            }
        }
    }
}