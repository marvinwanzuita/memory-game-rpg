package com.mawit.memorygamerpg.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.mawit.memorygamerpg.R
import com.mawit.memorygamerpg.R.drawable.*

class Florest3 : AppCompatActivity() {

    private lateinit var buttons: List<ImageView>
    private lateinit var images: MutableList<Int>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_florest3)

        images = mutableListOf(goblin, tree, heart, trail, trail, trail)
        images.addAll(images)
        images.shuffle()

        buttons = listOf(findViewById(R.id.img1), findViewById(R.id.img2), findViewById(R.id.img3),
            findViewById(R.id.img4), findViewById(R.id.img5), findViewById(R.id.img6),
            findViewById(R.id.img7), findViewById(R.id.img8), findViewById(R.id.img9),
            findViewById(R.id.img10), findViewById(R.id.img11), findViewById(R.id.img12),
        )


        var clicked = 0

        var indexPrimeiroBotaoSelecionado : Int = 0
        var indexSegundoBotaoSelecionado : Int = 0
        var primeiraCartaSelecionada : Int = 0
        var segundaCartaSelecioanda : Int = 0
        var formouPar = false
        var paresAcertados = mutableListOf<Int>()

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {



                if (paresAcertados.contains(index)){
                    Toast.makeText(this, "Já está virada", Toast.LENGTH_SHORT).show()
                } else if (clicked == 0){
                    indexPrimeiroBotaoSelecionado = index
                    button.setImageResource(images[index])
                    primeiraCartaSelecionada = images[index]
                    clicked++
                } else if (clicked == 1){

                    indexSegundoBotaoSelecionado = index
                    button.setImageResource(images[index])
                    segundaCartaSelecioanda = images[index]

                    if (primeiraCartaSelecionada == segundaCartaSelecioanda){
                        paresAcertados.add(indexPrimeiroBotaoSelecionado)
                        paresAcertados.add(indexSegundoBotaoSelecionado)
                        formouPar = true
                    }
                    clicked = 0

                }

                if (!formouPar){
                    buttons[indexPrimeiroBotaoSelecionado!!].setImageResource(interrogation)
                    buttons[indexSegundoBotaoSelecionado!!].setImageResource(interrogation)
                }


            }
        }

    }



}