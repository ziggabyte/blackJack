package com.example.blackjackappx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.blackjackappx.entities.Deck
import com.example.blackjackappx.entities.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GameActivity : AppCompatActivity() {
    val BASE_URL = "https://www.deckofcardsapi.com"
    lateinit var btnLogout: Button
    lateinit var btnBet: Button
    lateinit var tvPlayerStack: TextView
    lateinit var tvPlayerName: TextView
    lateinit var etPlacedBet: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        btnLogout = findViewById(R.id.btn_logout)
        btnBet = findViewById(R.id.btn_bet)
        tvPlayerStack = findViewById(R.id.tv_stack_size)
        tvPlayerName = findViewById(R.id.tvPlayerName)
        etPlacedBet = findViewById(R.id.et_bet)


        val currentUser = User(
            intent.getStringExtra("username").toString(),
            intent.getStringExtra("password").toString())

        tvPlayerName.text = currentUser.username
        tvPlayerStack.text = currentUser.userStack.toString()

        var startStack = tvPlayerStack.text.toString().toInt()

        fun setStartStack(bet: String) {
            startStack -= bet.toInt()
            tvPlayerStack.text = "$startStack"
        }

        btnBet.setOnClickListener{
            etPlacedBet = findViewById(R.id.et_bet)
            var placedBet = etPlacedBet.text.toString()
            setStartStack(placedBet)
        }
    }

    private fun getDeck() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitInterface::class.java)

        val call = retrofit.getDeck()
        call?.enqueue(object: Callback<Deck> {
            override fun onResponse(
                call: Call<Deck>,
                response: Response<Deck>
            ) {
                val deck : Deck? = response.body()
                if (deck != null) {
                    println(deck.deck_id + "  remaining: " + deck.remaining)

                    getDrawnCard(deck.deck_id)
                }
            }

            override fun onFailure(call: Call<Deck>, t: Throwable) {
                Log.d("MainActivity", "did not work $t")
            }
        })
    }


    private fun getDrawnCard(deckId : String){

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitInterface::class.java)

        val call = retrofit.getDrawnCard(deckId)
        call?.enqueue(object: Callback<Deck> {
            override fun onResponse(
                call: Call<Deck>,
                response: Response<Deck>
            ) {
                val deck : Deck? = response.body()
                if (deck != null) {
                    println(deck.deck_id + "  remaining: " + deck.remaining)
                    println(deck.cards[0].value)
                }
            }

            override fun onFailure(call: Call<Deck>, t: Throwable) {
                Log.d("MainActivity", "did not work $t")
            }
        })
    }

}