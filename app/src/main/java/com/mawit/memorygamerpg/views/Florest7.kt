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
import com.mawit.memorygamerpg.R.drawable.bear
import com.mawit.memorygamerpg.R.drawable.goblin
import com.mawit.memorygamerpg.R.drawable.heart
import com.mawit.memorygamerpg.R.drawable.interrogation
import com.mawit.memorygamerpg.R.drawable.trail
import com.mawit.memorygamerpg.R.drawable.tree
import com.mawit.memorygamerpg.R.drawable.wolf
import com.mawit.memorygamerpg.databinding.ActivityFlorest4Binding
import com.mawit.memorygamerpg.databinding.ActivityFlorest5Binding
import com.mawit.memorygamerpg.databinding.ActivityFlorest6Binding
import com.mawit.memorygamerpg.databinding.ActivityFlorest7Binding
import com.mawit.memorygamerpg.memorycard.MemoryCard


class Florest7 : AppCompatActivity() {

    private lateinit var binding: ActivityFlorest7Binding
    private lateinit var buttons: List<ImageView>
    private lateinit var cards: List<MemoryCard>
    private lateinit var images: MutableList<Int>
    private var indexOfSingleSelectedCard: Int? = null
    var totalCardsTurned = 0
    var txtLifesInt = 3
    var txtTrailInt = 0
    var txtCounterInt = 62

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlorest7Binding.inflate(layoutInflater)
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
        var img13 = binding.btn13
        var img14 = binding.btn14
        var img15 = binding.btn15
        var img16 = binding.btn16
        var img17 = binding.btn17
        var img18 = binding.btn18
        var img19 = binding.btn19
        var img20 = binding.btn20
        var img21 = binding.btn21
        var img22 = binding.btn22
        var img23 = binding.btn23
        var img24 = binding.btn24
        var img25 = binding.btn25
        var img26 = binding.btn26
        var img27 = binding.btn27
        var img28 = binding.btn28
        var img29 = binding.btn29
        var img30 = binding.btn30
        var img31 = binding.btn31
        var img32 = binding.btn32
        var img33 = binding.btn33
        var img34 = binding.btn34

        var txtlifes = binding.txtLifes
        var txtTrail = binding.txtTrail

        var btnInfo = binding.btnInfo
        var txtCounter = binding.txtCounter

        val bundle = intent.extras
        txtLifesInt = bundle!!.getInt("lifes")
        txtCounterInt += bundle.getInt("counter")

        txtCounter.text = txtCounterInt.toString()
        txtlifes.text = txtLifesInt.toString()
        txtTrail.text = "0"

        alertInfo()

        btnInfo.setOnClickListener {
            alertInfo()
        }

        images = mutableListOf(bear, bear, bear, wolf, wolf, wolf, tree, tree, tree,
            goblin, goblin, goblin, goblin,
            heart, trail, trail, trail)
        images.addAll(images)
        images.shuffle()

        buttons = listOf(
            img1, img2, img3, img4, img5, img6, img7, img8,
            img9, img10, img11, img12, img13, img14, img15, img16,
            img17, img18, img19, img20, img21, img22, img23, img24,
            img25, img26, img27, img28, img29, img30,
            img31, img32, img33, img34
        )

        cards = buttons.indices.map { index ->
            MemoryCard(images[index])
        }

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                updateModels(index, txtlifes, txtTrail)
                updateViews()

                txtCounterInt--
                txtCounter.text = txtCounterInt.toString()
            }
        }
    }

    private fun updateViews() {

        cards.forEachIndexed { index, card ->
            var button = buttons[index]
            if (card.isMatched) {
                button.alpha = 0.2f
            }

            if (card.isFaceUp) {
                button.setImageResource(images[index])

            } else {
                button.setImageResource(interrogation)
            }
        }

    }

    private fun updateModels(position: Int, txtLifes: TextView, txtTrail: TextView) {
        val card = cards[position]

        if (card.isFaceUp) {
            Toast.makeText(this, "Card is already turned!", Toast.LENGTH_SHORT).show()
            return
        }

        if (indexOfSingleSelectedCard == null) {
            restoreCards()
            indexOfSingleSelectedCard = position
        } else {
            checkForMatch(indexOfSingleSelectedCard!!, position, txtLifes, txtTrail)
            indexOfSingleSelectedCard = null
        }

        card.isFaceUp = !card.isFaceUp
    }

    private fun restoreCards() {
        for (card in cards) {
            if (!card.isMatched) {
                card.isFaceUp = false
            }
        }
    }

    private fun checkForMatch(
        position1: Int,
        position2: Int,
        txtLifes: TextView,
        txtTrail: TextView
    ) {
        if (cards[position1].identifier == cards[position2].identifier) {
            cards[position1].isMatched = true
            cards[position2].isMatched = true
            totalCardsTurned += 2

            when (cards[position1].identifier) {
                bear -> {
                    txtLifesInt -= 4
                    txtLifes.text = txtLifesInt.toString()
                }
                wolf -> {
                    txtLifesInt -= 3
                    txtLifes.text = txtLifesInt.toString()
                }

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

            if (txtTrailInt == 6) {
                alertNextPhase()
            }
            if (txtLifesInt <= 0) {
                alertGameOver()
            }
        }
    }

    fun alertGameOver() {

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

    fun alertNextPhase() {

        val alertBuilder = AlertDialog.Builder(this)

        val view = layoutInflater.inflate(R.layout.alert_dialog_game_finished, null)
        alertBuilder.setView(view)
        val alertDialog = alertBuilder.create()

        val btnOK = view.findViewById<ImageButton>(R.id.btnOK)
        btnOK.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        alertDialog.show()
    }

    fun alertInfo() {

        val alertBuilder = AlertDialog.Builder(this)

        val view = layoutInflater.inflate(R.layout.alert_dialog_florest7_info, null)
        alertBuilder.setView(view)

        val alertDialog = alertBuilder.create()

        val btnOK = view.findViewById<ImageButton>(R.id.btnOK)
        btnOK.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }


}