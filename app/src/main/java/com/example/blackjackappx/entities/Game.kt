package com.example.blackjackappx.entities

class Game {

    //Array<Cards>
    //dealer-lista
    //Spelar-lista


    fun isBlackJack(hand: ArrayList<Int>): Boolean {
        return hand.sum() == 21

    }
    }




