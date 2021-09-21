package com.example.blackjackappx.entities

class User(val username: String,
           val password: String,
           val userStack: Int = 100,
           var score: Int = 0) {

    fun clearScore() {
        score = 0
    }
}