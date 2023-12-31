package com.mawit.memorygamerpg.views

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.mawit.memorygamerpg.MainActivity
import com.mawit.memorygamerpg.R
import com.mawit.memorygamerpg.R.drawable.*
import com.mawit.memorygamerpg.databinding.ActivityFlorest1Binding
import com.mawit.memorygamerpg.databinding.ActivityFlorest2Binding
import com.mawit.memorygamerpg.memorycard.MemoryCard


class Florest1 : AppCompatActivity() {

    private lateinit var binding : ActivityFlorest1Binding
    private lateinit var buttons: List<ImageView>
    private lateinit var cards: List<MemoryCard>
    private lateinit var images: MutableList<Int>
    private var mInterstitialAd: InterstitialAd? = null
    lateinit var mAdView: AdView
    private var indexOfSingleSelectedCard: Int? = null
    var totalCardsTurned = 0
    var txtLifesInt = 3
    var txtTrailInt = 0
    var txtCounterInt = 16

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlorest1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeAds()

        var img1 = binding.btn1
        var img2 = binding.btn2
        var img3 = binding.btn3
        var img4 = binding.btn4
        var img5 = binding.btn5
        var img6 = binding.btn6
        var img7 = binding.btn7
        var img8 = binding.btn8

        var txtlifes = binding.txtLifes
        var txtTrail = binding.txtTrail

        var btnInfo = binding.btnInfo
        var txtCounter = binding.txtCounter

        txtlifes.text = "3"
        txtTrail.text = "0"
        txtCounter.text = "16"

        alertInfo()

        btnInfo.setOnClickListener {
            alertInfo()
        }

        images = mutableListOf(heart, goblin, trail, trail)
        images.addAll(images)
        images.shuffle()

        buttons = listOf(img1, img2, img3, img4, img5, img6, img7, img8)

        cards = buttons.indices.map { index ->
            MemoryCard(images[index])
        }

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                updateModels(index, txtlifes, txtTrail)
                updateViews()

                txtCounterInt--
                if (txtCounterInt <= 0){
                    alertGameOver()
                }
                txtCounter.text = txtCounterInt.toString()
            }

        }
    }

    private fun updateViews() {

        cards.forEachIndexed { index, card ->
            var button = buttons[index]
            if (card.isMatched){
                button.alpha = 0.2f
            }

            if (card.isFaceUp){
                button.setImageResource(images[index])

            }  else {
                button.setImageResource(interrogation)
            }
        }

    }

    private fun updateModels(position: Int, txtLifes: TextView, txtTrail: TextView) {
        val card = cards[position]

        if (card.isFaceUp){
            Toast.makeText(this, "Card is already turned!", Toast.LENGTH_SHORT).show()
            return
        }

        if (indexOfSingleSelectedCard == null){
            restoreCards()
            indexOfSingleSelectedCard = position
        } else {
            checkForMatch(indexOfSingleSelectedCard!!, position, txtLifes, txtTrail)
            indexOfSingleSelectedCard = null
        }

        card.isFaceUp = !card.isFaceUp
    }

    private fun restoreCards() {
        for (card in cards){
            if (!card.isMatched){
                card.isFaceUp = false
            }
        }
    }

    private fun checkForMatch(position1: Int, position2: Int, txtLifes: TextView, txtTrail: TextView) {
        if (cards[position1].identifier == cards[position2].identifier){
            cards[position1].isMatched = true
            cards[position2].isMatched = true
            totalCardsTurned += 2

            when(cards[position1].identifier){
                trail -> {
                    txtTrailInt += 2
                    txtTrail.text = txtTrailInt.toString()
                }
                goblin -> {
                    txtLifesInt -= 1
                    txtLifes.text = txtLifesInt.toString()
                }
                heart -> {
                    txtLifesInt++
                    txtLifes.text = txtLifesInt.toString()
                }
                tree -> {
                    txtLifesInt -= 2
                    txtLifes.text = txtLifesInt.toString()
                }
            }

            if (txtTrailInt == 4){
                alertNextPhase()
            }
            if (txtLifesInt <= 0){
                alertGameOver()
            }
        }
    }

    fun alertGameOver(){

        val alertBuilder = AlertDialog.Builder(this)

        val view = layoutInflater.inflate(R.layout.alert_dialog_florest_gameover, null)
        alertBuilder.setView(view)

        val btnOK = view.findViewById<ImageButton>(R.id.btnOK)
        btnOK.setOnClickListener {
            startActivity(Intent(this, Florest1::class.java))
            finish()
            if (mInterstitialAd != null) {
                mInterstitialAd?.show(this)
            } else {
                Log.d("TAG", "The interstitial ad wasn't ready yet.")
            }
        }

        val alertDialog = alertBuilder.create()
        alertDialog.show()
    }

    fun alertNextPhase(){

        val alertBuilder = AlertDialog.Builder(this)

        val view = layoutInflater.inflate(R.layout.alert_dialog_next_phase, null)
        alertBuilder.setView(view)

        val btnOK = view.findViewById<ImageButton>(R.id.btnOK)
        btnOK.setOnClickListener {

            var intent = Intent(this, Florest2::class.java)
            intent.putExtra("lifes", txtLifesInt)
            intent.putExtra("counter", txtCounterInt)
            startActivity(intent)
            finish()

        }

        val alertDialog = alertBuilder.create()
        alertDialog.show()
    }

    fun alertInfo(){

        val alertBuilder = AlertDialog.Builder(this)

        val view = layoutInflater.inflate(R.layout.alert_dialog_florest1_info, null)
        alertBuilder.setView(view)

        val alertDialog = alertBuilder.create()
        val btnOK = view.findViewById<ImageButton>(R.id.btnOK)
        btnOK.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()

    }

    fun initializeAds(){
        MobileAds.initialize(this)
        val banner = AdView(this)
        val adRequest = AdRequest.Builder().build()
        banner.setAdSize(AdSize.BANNER)
        banner.adUnitId = "ca-app-pub-5618593123155937/5577178315"
        mAdView = binding.adView
        mAdView.loadAd(adRequest)



        InterstitialAd.load(
            this,"ca-app-pub-5618593123155937/4724945853",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    adError.toString().let { Log.d(TAG, it) }
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(TAG, "Ad was loaded.")
                    mInterstitialAd = interstitialAd

                    mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
                        override fun onAdClicked() {}

                        override fun onAdDismissedFullScreenContent() {
                            mInterstitialAd = null
                        }

                        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                            mInterstitialAd = null
                        }

                        override fun onAdImpression() {}

                        override fun onAdShowedFullScreenContent() {}
                    }
                }
            })

    }


}