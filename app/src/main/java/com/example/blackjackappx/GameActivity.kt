package com.example.blackjackappx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import coil.load
import com.example.blackjackappx.entities.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Array
import kotlin.properties.Delegates

class GameActivity : AppCompatActivity() {
    val BASE_URL = "https://www.deckofcardsapi.com"
    lateinit var btnStart : Button
    lateinit var btnLogout: Button
    lateinit var btnDraw: Button
    lateinit var btnHold : Button
    lateinit var tvPlayerStack: TextView
    lateinit var tvCurrentBet : TextView
    lateinit var tvPlayerName: TextView
    lateinit var tvUserScore : TextView
    lateinit var tvDealerScoreNumber : TextView
    lateinit var tvUserScoreNumber : TextView
    lateinit var tvDealerScore: TextView

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
    var dealerCardCount : Int = 0

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

        tvCurrentBet = findViewById(R.id.tv_curretn_bet)
        tvPlayerStack = findViewById(R.id.tv_stack_size)
        tvPlayerName = findViewById(R.id.tvPlayerName)
        tvDealerScore = findViewById(R.id.tv_user_score)
        tvUserScore = findViewById(R.id.tv_dealer_score)
        tvDealerScoreNumber = findViewById(R.id.tv_dealer_score_number)
        tvUserScoreNumber = findViewById(R.id.tv_user_score_number)

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

        //var bet: String = intent.getStringExtra("placedBet")!!
        tvCurrentBet.text = intent.getStringExtra("placedBet")

        fun updateStack(bet: String) {
            startStack -= bet.toInt()
            tvPlayerStack.text = "$startStack"
        }

        btnStart.setOnClickListener{
            getStartCards(btnStart.tag.toString(), 4)
            btnStart.visibility = View.INVISIBLE
            btnStart.isEnabled = false

            btnHold.isEnabled = true
            btnDraw.isEnabled = true
        }

        btnHold.setOnClickListener{
            getDrawnCardToDealer(btnStart.tag.toString(), 1, dealerCardCount)
            dealerCardCount++
            checkForWinner()
        }

        btnDraw.setOnClickListener{
            getDrawnCard(btnStart.tag.toString(), 1, cardCount)
            cardCount ++
        }

        btnLogout.setOnClickListener{
            currentUser.userStack = tvPlayerStack.text.toString().toInt()
            val databaseHelper = DatabaseHelper(this)
            databaseHelper.saveUserData(currentUser)

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("logoutMessage", "Du är utloggad, välkommen åter!")
            startActivity(intent)
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
                    if(deck.remaining > 15) {
                        println(deck.deck_id + "  remaining: " + deck.remaining)
                        println(deck.cards[0].value)
                        println(deck.cards[1].value)
                        println(deck.cards[2].value)
                        println(deck.cards[3].value)
                        val card1: ImageView = findViewById(R.id.card1)
                        val card2: ImageView = findViewById(R.id.card2)
                        val card3: ImageView = findViewById(R.id.card3)
                        val card4: ImageView = findViewById(R.id.card4)
                        card1.load(deck.cards[0].image)
                        card2.load(deck.cards[1].image)
                        card3.load(deck.cards[2].image)
                        card4.load(deck.cards[3].image)

                        val c: Card = deck.cards[0]
                        val c2: Card = deck.cards[2]
                        val d1: Card = deck.cards[1]
                        val d2: Card = deck.cards[3]

                        setPoints(c)
                        setPoints(c2)
                        setPoints(d1)
                        setPoints(d2)

                        var playerPoints = c.points + c2.points
                        var dealerPoints = d1.points + d2.points

                        println("---------------------------------------$playerPoints")

                        //Sätter första user score och dealer score
                        tvUserScoreNumber.text = playerPoints.toString()
                        tvDealerScoreNumber.text = dealerPoints.toString()

                        //göra user score och dealer score synliga
                        tvUserScore.visibility = View.VISIBLE
                        tvUserScoreNumber.visibility = View.VISIBLE
                        tvDealerScore.visibility = View.VISIBLE
                        tvDealerScoreNumber.visibility = View.VISIBLE

                        //kolla ifall någon har vunnit/fått blackjack
                        isBlackJack(playerPoints, dealerPoints)
                    } else{shuffleDeck(deck.deck_id)}
                }
            }

            override fun onFailure(call: Call<Deck>, t: Throwable) {
                Log.d("MainActivity", "did not work $t")
            }
        })
    }

    fun setPoints (c : Card) : Int {
        var value : Int

        if(c.value == "ACE") {
            c.points = 11
            value =  11
        }
        else if(c.value == "KING" || c.value == "QUEEN" || c.value == "JACK") {
            c.points = 10
            value = 10
        }
        else {
            c.points = c.value.toInt()
            value = c.value.toInt()
        }

        return value
    }

    fun updateScoreNumbers(tvScoreNumber : TextView, newCardPoints : Int) {
        val newScore = tvScoreNumber.text.toString().toInt() + newCardPoints
        tvScoreNumber.text = newScore.toString()
    }

    fun checkForWinner() {
        val userScore = tvUserScoreNumber.text.toString().toInt()
        val dealerScore = tvDealerScoreNumber.text.toString().toInt()

        if (!isBlackJack(userScore, dealerScore)) {
            if (!isOver21(userScore, dealerScore)) {
                if (!isNormalWin(userScore, dealerScore)) {
                    //ingen har vunnit, gå vidare
                }
            }
        }
    }

    fun isNormalWin(userScore: Int, dealerScore: Int) : Boolean {
        if (userScore == dealerScore) {
            announceWinner("tie", false)
            return true
        } else if (userScore < dealerScore) {
            announceWinner("dealer", false)
            return true
        } else if (dealerScore < userScore) {
            announceWinner("user", false)
            return true
        } else {
            return false
        }
    }

    fun isOver21(userScore : Int, dealerScore: Int) : Boolean{
        if (userScore > 21) {
            announceWinner("dealer", false)
            return true
        } else if (dealerScore > 21) {
            announceWinner("user", false)
            return true
        } else {
            return false
        }
    }

    fun isBlackJack(userScore : Int, dealerScore : Int) : Boolean {
        if (userScore == 21 && dealerScore == 21) {
            announceWinner("tie", true)
            return true
        } else if (userScore == 21) {
            announceWinner("user", true)
            return true
        } else if (dealerScore == 21) {
            announceWinner("dealer", true)
            return true
        } else {
            return false
        }
    }

    fun createWinnerDialog(title : String, message : String, isBlackJack: Boolean ) : AlertDialog {
        var alertTitle = title;
        if (isBlackJack) {
           alertTitle  += " Black Jack!!"
        }
        return AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Spela igen") { _, _ ->
                val intent = Intent(this, BettingActivity::class.java)
                intent.putExtra("stack", this.tvPlayerStack.text.toString())
                startActivity(intent)
            }
            .setNegativeButton("Avsluta och logga ut") { _, _ ->
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("logoutMessage", "Du är utloggad, välkommen åter!")
                startActivity(intent)
            }.create()
    }

    fun announceWinner(whoWon : String, isBlackJack: Boolean) {
        var bet = intent.getStringExtra("placedBet")?.toInt()
        var wonBet = bet?.times(2)
        when (whoWon) {
            "user" -> createWinnerDialog("Du vann!", "Du vinner ${wonBet.toString()} $", isBlackJack).show()
            "dealer" -> createWinnerDialog("Dealern vann...", "Du vinner inga pengar", isBlackJack).show()
            "tie" -> createWinnerDialog("Oavgjort!!!!", "Du får tillbaka din insats på $bet $", isBlackJack).show()
        }
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

                        //uppdatera score
                        val currentUserScore = this@GameActivity.tvUserScoreNumber.text.toString().toInt()
                        val newUserScore = currentUserScore + setPoints(deck.cards[0])
                        this@GameActivity.tvUserScoreNumber.text = newUserScore.toString()

                        //kolla ifall det blivit blackjack eller nån har gått över 21
                        isBlackJack(this@GameActivity.tvUserScoreNumber.text.toString().toInt(),
                            this@GameActivity.tvDealerScoreNumber.text.toString().toInt())
                        isOver21(this@GameActivity.tvUserScoreNumber.text.toString().toInt(),
                            this@GameActivity.tvDealerScoreNumber.text.toString().toInt())
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
    private fun getDrawnCardToDealer(deckId: String, count: Int, dealerCardCount : Int){
        var dCC = dealerCardCount

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitInterface::class.java)

        val call = retrofit.getDrawnCard(deckId, count)

        if (tvDealerScoreNumber.text.toString().toInt() < 17) {
            call?.enqueue(object : Callback<Deck> {
                override fun onResponse(
                    call: Call<Deck>,
                    response: Response<Deck>
                ) {
                    val deck: Deck? = response.body()
                    if (deck != null) {
                        println(deck.deck_id + "  remaining: " + deck.remaining)
                        println(deck.cards[0].value)
                        var ivList: MutableList<ImageView> =
                            mutableListOf(dealerCard3, dealerCard4, dealerCard5)
                        ivList[dCC].load(deck.cards[0].image)

                        val c = setPoints(deck.cards[0])
                        val currentDealerScore = tvDealerScoreNumber.text.toString().toInt()
                        var newDealerScore: Int = currentDealerScore + c
                        tvDealerScoreNumber.text = newDealerScore.toString()
                        dCC++
                        getDrawnCardToDealer(deck.deck_id,1,dCC)
                    }
                }
                override fun onFailure(call: Call<Deck>, t: Throwable) {
                    Log.d("MainActivity", "did not work $t")
                }
            })
        } else {
            checkForWinner()
        }
    }

    private fun shuffleDeck(deckId: String){
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitInterface::class.java)

        val call = retrofit.shuffleDeck(deckId)
        call?.enqueue(object: Callback<Deck> {
            override fun onResponse(
                call: Call<Deck>,
                response: Response<Deck>
            ) {
                val deck : Deck? = response.body()
                if (deck != null) {
                    getStartCards(deck.deck_id,4)
                }
            }
            override fun onFailure(call: Call<Deck>, t: Throwable) {
                Log.d("MainActivity", "did not work $t")
            }
        })
    }


}