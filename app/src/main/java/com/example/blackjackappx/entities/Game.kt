package com.example.blackjackappx.entities

class Game {
    //Array<Cards>
    //dealer-lista
    //Spelar-lista

    var dealerWin: Boolean = false
    var playerWin: Boolean = false
    var even: Boolean = false

    var deck = mutableListOf<Card>()
    lateinit var playerHand: Array<Int>
    lateinit var dealerHand: Array<Int>

    fun isBlackJack(hand: ArrayList<Int>): Boolean {
        return hand.sum() == 21
    }

    fun isEven(playerHand: ArrayList<Int>, dealerHand: ArrayList<Int>) {
        var sumOfHands = playerHand.sum()

        if(playerHand.sum() == dealerHand.sum() && sumOfHands>19){
            dealerWin
        }
        else if(playerHand.sum() == dealerHand.sum() && sumOfHands<19){
            even
        }
    }


    }
