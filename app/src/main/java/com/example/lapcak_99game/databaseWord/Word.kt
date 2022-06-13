package com.example.lapcak_99game.databaseWord

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words_table")
data class Word (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val level: Int,
    val word: String,
    val percentage: Int
    //val guessed: Boolean
)