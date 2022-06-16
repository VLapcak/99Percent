package com.example.lapcak_99game.databaseWord

import androidx.lifecycle.LiveData

/**
 * Trieda [WordRepository] slúži ako repozitár pre databázu,
 * čím má napomôcť prehľadnejšiemu kódu .
 */
class WordRepository(private val wordDao: WordDao) {

    val getAllWords: LiveData<List<Word>> = wordDao.getAllWords()

    /**
     * Metóda [addWord] zavolá z [WordDao] addWord pre pridanie slovíčka do databázy.
     * @param word typu [Word] označuje slovíčko.
     */
    suspend fun addWord(word: Word)
    {
        wordDao.addWord(word)
    }

    /**
     * Metóda [getAllWordsByLvl] vracia slovíčka podľa zvoleného levelu.
     * @param level reprezetuje číslo levelu.
     */
    fun getAllWordsByLvl(level: Int): LiveData<List<Word>>
    {
        return wordDao.getWordsByLevel(level)
    }

    /**
     * Metóda [clearDatabase] zavolá z [WordDao] clear() pre vymazanie záznamov v databáze.
     */
    suspend fun clearDatabase()
    {
        wordDao.clear()
    }
}