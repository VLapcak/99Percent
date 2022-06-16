package com.example.lapcak_99game.databaseWord

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Trieda [WordViewModel] využíva z triedy [WordRepository] metódy,
 * avšak využíva pri tom Courutines pre optimalizáciu rýchlosti aplikácie.
 */
class WordViewModel (application: Application) : AndroidViewModel(application)
{
    private val getAllWords: LiveData<List<Word>>
    private val repository: WordRepository

    /**
     * Metóda [clearDatabase] sa odvoláva na [WordRepository] pre vymazanie záznamov v databáze.
     * Využíva aj Coroutines [Dispatchers.IO] pre optimalizovanejšie načítanie z databázy.
     */
    fun clearDatabase()
    {
        viewModelScope.launch(Dispatchers.IO) { repository.clearDatabase() }
    }

    /**
     * Metóda [addWord] sĺuži na pridanie slovíčka do databázy
     * odvoláva sa pritom na [WordRepository].
     * Využíva aj Coroutines [Dispatchers.IO] pre optimalizovanejšie načítanie z databázy.
     */
    fun addWord(word: Word) {
        viewModelScope.launch(Dispatchers.IO) { repository.addWord(word) }
    }

    /**
     * Metóda [getAllWByLvl] navracia slovíčka z databázy podľa levelu,
     * odvoláva sa pritom na [WordRepository].
     * @param level reprezetuje číslo levelu.
     * @return slovíčka z databázy podľa levelu.
     */
    fun getAllWByLvl(level: Int): LiveData<List<Word>>
    {
        return repository.getAllWordsByLvl(level)
    }

    /**
     * Inicializačný blok, kde sa inicualituje repozitár a
     * vyberú sa všetky slovíčka z databázy do množiny LiveData getAllWords.
     */
    init {
        val wordDao = WordDatabase.getDatabase(application).wordDao()
        repository = WordRepository(wordDao)
        getAllWords = repository.getAllWords
    }
}