package com.example.lapcak_99game.recyclerView

/**
 * Dátová trieda [Picture] sĺuži na preddefinovanie atribútov v triede.
 * Použije sa v triede [PictureAdapter] a ďalej v tride [LevelSelector]
 */
data class Picture(val image: Int, val name: String, var percentage: Int, val level: Int)
