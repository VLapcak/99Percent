package com.example.lapcak_99game.databaseWord

import androidx.lifecycle.LiveData

class WordRepository(private val wordDao: WordDao) {

    val getAllWords: LiveData<List<Word>> = wordDao.getAllWords()

    suspend fun addWord(word: Word)
    {
        wordDao.addWord(word)
    }
    fun getAllWordsByLvl(level: Int): LiveData<List<Word>>
    {
        return wordDao.getWordsByLevel(level)
    }

    suspend fun clearDatabase()
    {
        wordDao.clear()
    }
}