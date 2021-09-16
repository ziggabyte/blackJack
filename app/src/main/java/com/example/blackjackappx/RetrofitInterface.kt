package com.example.blackjackappx

import com.example.blackjackappx.entities.Deck
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitInterface {

    @get:GET("/api/deck/new/shuffle/?deck_count=6")
    val newDeck : Call<Deck>

    @get:GET("/api/deck/<DeckId>/draw/?count=1")
    val drawCard : Call<Deck>

    companion object {
        const val BASE_URL = "https://www.deckofcardsapi.com"
    }
}