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

        val databaseHelper = DatabaseHelper(this)

        databaseHelper.initiateDatabase()

        var test21 = arrayListOf<Int>(11,10)

        fun isBlackJack(hand: ArrayList<Int>): Boolean {
            return hand.sum() == 21

        }
        var xx: Boolean = isBlackJack(test21)
        if(xx==true){
            btnLogin.setText("BlackJack")
        }


        btnLogin.setOnClickListener{
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            val isInDatabase = databaseHelper.isInDatabase(username, password)

            val intent = Intent(this, GameActivity::class.java)

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