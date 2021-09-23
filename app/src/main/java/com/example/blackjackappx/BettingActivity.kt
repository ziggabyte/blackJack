package com.example.blackjackappx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class BettingActivity : AppCompatActivity() {

    lateinit var btnPlaceBetAndAStart : Button
    lateinit var etBet : EditText
    lateinit var btnLogout : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_betting)

        btnPlaceBetAndAStart = findViewById(R.id.btn_place_bet_and_start)
        etBet = findViewById(R.id.et_bet)
        btnLogout = findViewById(R.id.btn_logout)

        btnPlaceBetAndAStart.setOnClickListener{
            val placedBet = etBet.text.toString()
            var intent = Intent(this, GameActivity::class.java)
            intent.putExtra("placedBet", placedBet)
            intent.putExtra("username", getIntent().getStringExtra("username"))
            intent.putExtra("password", getIntent().getStringExtra("password"))

            startActivity(intent)
        }

        btnLogout.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("logoutMessage", "Du är utloggad, välkommen åter!")
            startActivity(intent)
        }
    }
}