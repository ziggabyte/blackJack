package com.example.blackjackappx

import com.example.blackjackappx.entities.Deck
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitInterface {


    @GET("/api/deck/new/shuffle/?deck_count=6")
    fun getDeck() : Call<Deck>


    @GET("/api/deck/{id}/shuffle/")
    fun shuffleDeck(@Path("id") deckID : String) : Call<Deck>

    @GET("/api/deck/{id}/draw/?count=")
    fun getStartCards(@Path("id") deckID : String, @Query("count") count : Int) : Call<Deck>

    @GET("/api/deck/{id}/draw/?count=")
    fun getDrawnCard(@Path("id") deckID : String, @Query("count") count : Int) : Call<Deck>

    companion object {

    }
}