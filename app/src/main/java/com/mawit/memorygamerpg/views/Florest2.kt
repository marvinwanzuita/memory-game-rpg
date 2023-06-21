package com.mawit.memorygamerpg.views

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.mawit.memorygamerpg.MainActivity
import com.mawit.memorygamerpg.R
import com.mawit.memorygamerpg.R.drawable.*
import com.mawit.memorygamerpg.databinding.ActivityFlorest2Binding
import com.mawit.memorygamerpg.memorycard.MemoryCard


class Florest2 : AppCompatActivity() {

    private lateinit var binding : ActivityFlorest2Binding
    private lateinit var buttons: List<ImageView>
    private lateinit var cards: List<MemoryCard>
    private lateinit var images: MutableList<Int>
    private var indexOfSingleSelectedCard: Int? = null
    var totalCardsTurned = 0
    var txtLifesInt = 3
    var txtTrailInt = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlorest2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        var img1 = binding.btn1
        var img2 = binding.btn2
        var img3 = binding.btn3
        var img4 = binding.btn4
        var img5 = binding.btn5
        var img6 = binding.btn6
        var img7 = binding.btn7
        var img8 = binding.btn8
        var img9 = binding.btn9
        var img10 = binding.btn10
        var img11 = binding.btn11
        var img12 = binding.btn12

        var txtlifes = binding.txtLifes
        var txtTrail = binding.txtTrail

        txtlifes.text = "3"
        txtTrail.text = "0"
        


        images = mutableListOf(goblin, tree, heart, trail, trail, trail)
        images.addAll(images)
        images.shuffle()

        buttons = listOf(img1, img2, img3, img4, img5, img6, img7, img8, img9, img10, img11, img12)

        cards = buttons.indices.map { index ->
            MemoryCard(images[index])
        }

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                updateModels(index, txtlifes, txtTrail)
                updateViews()

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

            if (txtTrailInt == 6){
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
            startActivity(Intent(this, MainActivity::class.java))
            finish()
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
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        val alertDialog = alertBuilder.create()
        alertDialog.show()
    }


}