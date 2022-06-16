package com.example.lapcak_99game.databaseWord

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Interface [WordDao] zabezpečuje priame vkladanie,
 * vyberanie slovíčok z databázy na základe levelu alebo bez obmedzení a
 * vymazanie záznamov v databáze.
 */

@Dao
interface WordDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addWord(word: Word)

    @Query("SELECT * FROM words_table ORDER BY id ASC")
    fun getAllWords(): LiveData<List<Word>>

    @Query("SELECT * FROM words_table WHERE level = :curLevel")
    fun getWordsByLevel(curLevel: Int): LiveData<List<Word>>

    @Query("DELETE FROM words_table")
    suspend fun clear()
}