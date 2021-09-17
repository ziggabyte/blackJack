package com.example.blackjackappx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.blackjackappx.entities.User

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvLoginError = findViewById<TextView>(R.id.tvLoginError)

        val databaseHelper = DatabaseHelper(this)

        databaseHelper.initiateDatabase()


        btnLogin.setOnClickListener{
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            val isInDatabase = databaseHelper.isInDatabase(email, password)

            val intent = Intent(this, GameActivity::class.java)

            if (isInDatabase) {
                startActivity(intent)
            } else {
                tvLoginError.text = "Login failed, user does not exist"
            }

        }
    }
}