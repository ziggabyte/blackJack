package com.example.blackjackappx.entities

class Card (
    val image : String,
    val value : String,
    val suit : String,
    val code : String,
    var points : Int
        ) {
    init {
        points = setPoints(this.value)
    }

    private fun setPoints (value: String) : Int {
        points =
            if(value == "ACE") {
                11
            }
            else if(value == "KING" || value == "QUEEN" || value == "JACK") {
                10
            }
            else {
                value.toInt()
        }
        return points
    }
}