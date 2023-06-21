package com.mawit.memorygamerpg.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.mawit.memorygamerpg.R.drawable.*
import com.mawit.memorygamerpg.databinding.ActivityFlorest1Binding
import com.mawit.memorygamerpg.memorycard.MemoryCard

class Florest1 : AppCompatActivity() {

    private lateinit var binding : ActivityFlorest1Binding
    private lateinit var buttons: List<ImageView>
    private lateinit var cards: List<MemoryCard>
    private lateinit var images: MutableList<Int>
    private lateinit var imagesShuffled: MutableList<Int>

    private var indexOfSingleSelectedCard: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlorest1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val img1 = binding.imgFlorest1
        val img2 = binding.imgFlorest2
        val img3 = binding.imgFlorest3
        val img4 = binding.imgFlorest4
        val img5 = binding.imgFlorest5
        val img6 = binding.imgFlorest6
        val img7 = binding.imgFlorest7
        val img8 = binding.imgFlorest8
        val img9 = binding.imgFlorest9
        val img10 = binding.imgFlorest10
        val img11 = binding.imgFlorest11
        val img12 = binding.imgFlorest12

        images = mutableListOf(goblin, tree, heart, trail, trail, trail)
        images.addAll(images)
        images.shuffle()

        buttons = listOf(img1, img2, img3, img4, img5, img6, img7, img8, img9, img10, img11, img12)

        cards = buttons.indices.map { index ->
            MemoryCard(images[index])
        }

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                updateModels(index)
                updateViews()
            }
        }

    }

    private fun updateViews() {

        cards.forEachIndexed { index, card ->
            var button = buttons[index]
            if (card.isMatched){
                button.alpha = 0.8f
            }

            if (card.isFaceUp){
                button.setImageResource(images[index])
            }  else {
                button.setImageResource(interrogation)
            }
        }

    }

    private fun updateModels(position: Int) {
        val card = cards[position]
        
        if (card.isFaceUp){
            Toast.makeText(this, "Card is already turned!", Toast.LENGTH_SHORT).show()
            return
        }

        if (indexOfSingleSelectedCard == null){
            restoreCards()
            indexOfSingleSelectedCard = position
        } else {
            checkForMatch(indexOfSingleSelectedCard!!, position)
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

    private fun checkForMatch(position1: Int, position2: Int) {
        if (cards[position1].identifier == cards[position2].identifier){
            cards[position1].isMatched = true
            cards[position2].isMatched = true
        }
    }

}