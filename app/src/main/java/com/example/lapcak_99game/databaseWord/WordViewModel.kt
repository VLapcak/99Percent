package com.example.lapcak_99game.databaseWord

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WordViewModel (application: Application) : AndroidViewModel(application)
{
    val getAllWords: LiveData<List<Word>>
    private val repository: WordRepository


    fun clearDatbase()
    {
        viewModelScope.launch(Dispatchers.IO) { repository.clearDatabase() }
    }
    fun addWord(word: Word) {
        viewModelScope.launch(Dispatchers.IO) { repository.addWord(word) }
    }

    fun getAllWByLvl(level: Int): LiveData<List<Word>>
    {
        return repository.getAllWordsByLvl(level)
    }


    init {
        val wordDao = WordDatabase.getDatabase(application).wordDao()
        repository = WordRepository(wordDao)
        getAllWords = repository.getAllWords
    }
}