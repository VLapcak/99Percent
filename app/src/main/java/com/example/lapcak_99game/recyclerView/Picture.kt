package com.example.lapcak_99game.recyclerView

/**
 * Dátová trieda [Picture] slúži na preddefinovanie atribútov v triede.
 * Použije sa v triede [PictureAdapter] a ďalej v triede [LevelSelector].
 */

data class Picture(val image: Int, val name: String, var percentage: Int, val level: Int)
