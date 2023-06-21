package com.mawit.memorygamerpg.memorycard

data class MemoryCard(
    var identifier: Int,
    var isFaceUp: Boolean = false,
    var isMatched: Boolean = false
)
