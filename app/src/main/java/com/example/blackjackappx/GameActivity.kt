package com.example.blackjackappx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import coil.load
import com.example.blackjackappx.entities.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.properties.Delegates

class GameActivity : AppCompatActivity() {
    val BASE_URL = "https://www.deckofcardsapi.com"
    lateinit var btnStart : Button
    lateinit var btnLogout: Button
    lateinit var btnDraw: Button
    lateinit var btnHold : Button
    lateinit var tvPlayerStack: TextView
    lateinit var tvPlayerName: TextView

    //spelarens iv-kort som dras med btnDraw
    lateinit var playerCard3 : ImageView
    lateinit var playerCard4 : ImageView
    lateinit var playerCard5 : ImageView

    //Dealerns iv-kort som börjar dras med btnHold
    lateinit var dealerCard3 : ImageView
    lateinit var dealerCard4 : ImageView
    lateinit var dealerCard5 : ImageView

    //variabel som styr vilket kort som ska få värde i en lista av korten/imageviews
    var cardCount : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        getDeck()

        //instansiering av spelarens iv-kort
        playerCard4 = findViewById(R.id.playerCard4)
        playerCard5 = findViewById(R.id.playerCard5)
        playerCard3 = findViewById(R.id.playerCard3)

        //instansiering av dealerns iv-kort
        dealerCard3 = findViewById(R.id.dealerCard3)
        dealerCard4 = findViewById(R.id.dealerCard4)
        dealerCard5 = findViewById(R.id.dealerCard5)

        tvPlayerStack = findViewById(R.id.tv_stack_size)
        tvPlayerName = findViewById(R.id.tvPlayerName)

        btnDraw = findViewById(R.id.btn_draw)
        btnHold = findViewById(R.id.btn_hold)
        btnStart = findViewById(R.id.btn_start)
        btnLogout = findViewById(R.id.btn_logout)

        var currentUser = User(
            intent.getStringExtra("username").toString(),
            intent.getStringExtra("password").toString())
        tvPlayerName.text = currentUser.username

        var startStack = 100 - intent.getStringExtra("placedBet")!!.toInt()
        currentUser.userStack = startStack
        tvPlayerStack.text = "$startStack"

        fun updateStack(bet: String) {
            startStack -= bet.toInt()
            tvPlayerStack.text = "$startStack"
        }

        btnStart.setOnClickListener{
            getStartCards(btnStart.tag.toString(), 4)
            btnStart.visibility = View.INVISIBLE
            btnStart.isClickable = false
        }

        btnHold.setOnClickListener{
            getDrawnCardToDealer(btnStart.tag.toString(), 1, cardCount)
            cardCount++
        }

        btnDraw.setOnClickListener{
            getDrawnCard(btnStart.tag.toString(), 1, cardCount)
            cardCount ++
        }
    }

    //Hämtar en kortlek
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
                    btnStart.tag = deck.deck_id

                }
            }

            override fun onFailure(call: Call<Deck>, t: Throwable) {
                Log.d("MainActivity", "did not work $t")
            }
        })
    }

    // Hämtar fyra kort, 2 till spelaren och 2 till dealern
    private fun getStartCards(deckId : String, count : Int){

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitInterface::class.java)

        val call = retrofit.getStartCards(deckId, count)
        call?.enqueue(object: Callback<Deck> {
            override fun onResponse(
                call: Call<Deck>,
                response: Response<Deck>
            ) {
                val deck : Deck? = response.body()
                if (deck != null) {
                    println(deck.deck_id + "  remaining: " + deck.remaining)
                    println(deck.cards[0].value)
                    println(deck.cards[1].value)
                    println(deck.cards[2].value)
                    println(deck.cards[3].value)
                    val card1 : ImageView = findViewById(R.id.card1)
                    val card2 : ImageView = findViewById(R.id.card2)
                    val card3 : ImageView = findViewById(R.id.card3)
                    val card4 : ImageView = findViewById(R.id.card4)
                    card1.load(deck.cards[0].image)
                    card2.load(deck.cards[1].image)
                    card3.load(deck.cards[2].image)
                    card4.load(deck.cards[3].image)
                    val c : Card = deck.cards[0]

                     fun setPoints (c : Card) {
                        c.points =
                            if(c.value == "ACE") {
                                11
                            }
                            else if(c.value == "KING" || c.value == "QUEEN" || c.value == "JACK") {
                                10
                            }
                            else {
                                c.value.toInt()
                            }
                    }
                    setPoints(c)
                    val playerHand : Array<Int> = arrayOf(c.points)
                    //val game : Game = Game()
                    //game.playerHand.set(0,deck.cards[0].points)
                    //game.playerHand.set(1,deck.cards[2].points)
                    println(playerHand[0])


                }
            }

            override fun onFailure(call: Call<Deck>, t: Throwable) {
                Log.d("MainActivity", "did not work $t")
            }
        })
    }

    //hämtar ett kort i taget och skriver ut kort till spelaren
    private fun getDrawnCard(deckId: String, count: Int, cardCount : Int){
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitInterface::class.java)

        val call = retrofit.getDrawnCard(deckId, count)
        call?.enqueue(object: Callback<Deck> {
            override fun onResponse(
                call: Call<Deck>,
                response: Response<Deck>
            ) {
                if(response.isSuccessful) {
                    val deck : Deck? = response.body()
                    if (deck != null) {
                        println(deck.deck_id + "  remaining: " + deck.remaining)
                        println(deck.cards[0].value)
                        var ivList : MutableList<ImageView> = mutableListOf(playerCard3,playerCard4,playerCard5)
                        ivList[cardCount].load(deck.cards[0].image)
                        return
                    }
                }
            }
            override fun onFailure(call: Call<Deck>, t: Throwable) {
                Log.d("MainActivity", "did not work $t")
            }
        })
    }
    //hämtar ett kort i taget och skriver ut kort till dealern
    private fun getDrawnCardToDealer(deckId: String, count: Int, cardCount : Int){

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitInterface::class.java)

        val call = retrofit.getDrawnCard(deckId, count)

        //while (dealer.score < 17){
        call?.enqueue(object: Callback<Deck> {
            override fun onResponse(
                call: Call<Deck>,
                response: Response<Deck>
            ) {
                val deck : Deck? = response.body()
                if (deck != null) {
                    println(deck.deck_id + "  remaining: " + deck.remaining)
                    println(deck.cards[0].value)
                    var ivList : MutableList<ImageView> = mutableListOf(dealerCard3,dealerCard4,dealerCard5)
                    ivList[cardCount].load(deck.cards[0].image)
                    return
                }
            }
            override fun onFailure(call: Call<Deck>, t: Throwable) {
                Log.d("MainActivity", "did not work $t")
            }
        })
    }

}