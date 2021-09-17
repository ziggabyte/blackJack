package com.example.blackjackappx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.blackjackappx.entities.User

class GameActivity : AppCompatActivity() {

    lateinit var btnLogout: Button
    lateinit var tvPlayerStack: TextView
    lateinit var tvPlayerName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        btnLogout = findViewById(R.id.btn_logout)
        tvPlayerStack = findViewById(R.id.tv_stack_size)
        tvPlayerName = findViewById(R.id.tvPlayerName)
        val userOne: User = User("Lisa", "123")
        tvPlayerName.setText(userOne.userName)
        tvPlayerStack.setText(userOne.userStack.toString())
    }
}