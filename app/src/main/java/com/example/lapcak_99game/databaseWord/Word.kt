package com.example.lapcak_99game.databaseWord

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Dátová trieda [Word] určuje prvky pre slovíčko.
 * primárny kľúč pre [id] sa generuje automaticky.
 * [level] ozančuje hodnotu z ktorého levelu to slovíčko pochádza.
 * [word] označuje názov slovíčka.
 * [percentage] označuje percentuálnu hodnotu slovíčka.
 */
@Entity(tableName = "words_table")
data class Word (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val level: Int,
    val word: String,
    val percentage: Int
)