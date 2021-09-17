package com.example.blackjackappx

import com.example.blackjackappx.entities.Deck
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitInterface {


    @GET("/api/deck/new/shuffle/?deck_count=6")
    fun getDeck() : Call<Deck>



    @GET("/api/deck/{id}/draw/?count=1")
    fun getDrawnCard(@Path("id") deckID : String) : Call<Deck>

    companion object {

    }
}