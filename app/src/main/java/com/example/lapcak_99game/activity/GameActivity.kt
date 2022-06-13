package com.example.lapcak_99game.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.lapcak_99game.R
import com.example.lapcak_99game.databaseWord.Word
import com.example.lapcak_99game.databaseWord.WordViewModel
import com.example.lapcak_99game.databinding.ActivityGameBinding
import java.util.*
import kotlin.collections.ArrayList


class GameActivity : AppCompatActivity() {

    lateinit var wordsInPicture: List<String>
    lateinit var wordsPercentage: List<String>
    lateinit var guessedWords: ArrayList<TextView>
    lateinit var wasGuessed: BooleanArray
    lateinit var binding: ActivityGameBinding
    lateinit var wordVM: WordViewModel

    private var level: Int = 1
    private var gems: Int = 0
    private var difficulty: Int = 0
    private var music: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        level = intent.getIntExtra("level", 1)
        val drawableName = intent.getStringExtra("pic")
        binding.picture.setImageResource(resources.getIdentifier(drawableName, "drawable", packageName))

        wordVM = ViewModelProvider(this)[WordViewModel::class.java]

        binding.goBack.setOnClickListener {
            val intent = Intent(this, LevelSelector::class.java)
            startActivity(intent)
        }
        val inputText = binding.textInput
        val sendBtn = binding.send
        sendBtn.setOnClickListener {
            checkForWord(inputText.text.toString())
            inputText.setText("")
            inputText.clearFocus()
        }
        binding.locate.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("location", level)
            startActivity(intent)
        }

        binding.hint.setOnClickListener {
            giveHint()
        }

        guessedWords = arrayListOf(binding.text1, binding.text2, binding.text3, binding.text4, binding.text5, binding.text6, binding.text7, binding.text8, binding.text9, binding.text10)

        setWordData()

    }


    private fun setWordData()
    {
        readDatabase()
        loadData()

        // lvl 1: 0..10, lvl 2: 10..20
        val range = (level - 1) * 10..((level - 1) * 10) + 10

        //words
        val allWords = assets.open("levelWords.txt").bufferedReader().use { it.readLines() }
        wordsInPicture = allWords.slice(range)

        //synonym words
        /*
        val allSynonymWords = assets.open("levelSynonymWords.txt").bufferedReader().use { it.readLines() }
        wordsInPicture = allSynonymWords.slice(range)*/

        //percentages for words
        val allWPer = assets.open("wordPercentage.txt").bufferedReader().use { it.readLines() }
        wordsPercentage = allWPer.slice(range)

        var count = 0
        for(i in 0..9)
        {
            if(wordsPercentage[i] != "")
            {
                guessedWords[i].text = wordsPercentage[i] + "%"
                count ++;
            }
        }
        wasGuessed = BooleanArray(count) {false}
    }

    private fun checkForWord(word: String)
    {
        val wordL = word.lowercase(Locale.getDefault())
        if (wordsInPicture.contains(wordL) && wordL != "")
        {
            val indexOf = wordsInPicture.indexOf(wordL)
            if (!wasGuessed[indexOf])
            {
                val perc = wordsPercentage[indexOf].toInt()
                addPercentage(perc)
                insertToDatabase(level, wordL, perc)
                isLevelCompleted()
                Toast.makeText(applicationContext, "[$wordL] found (+$perc%)", Toast.LENGTH_LONG).show()
                guessedWords[indexOf].text = wordsPercentage[indexOf] + "%    $wordL"
                wasGuessed[indexOf] = true
                addGems(10)
            }
        }
        else
        {
            Toast.makeText(applicationContext, "[$wordL] not found, try again", Toast.LENGTH_LONG).show()
        }

    }
    private fun assignWord(word: String)
    {
        if (wordsInPicture.contains(word) && word != "")
        {
            val indexOf = wordsInPicture.indexOf(word)
            if (!wasGuessed[indexOf]) {
                val perc = wordsPercentage[indexOf].toInt()
                addPercentage(perc)
                guessedWords[indexOf].text = wordsPercentage[indexOf] + "%    $word"
                wasGuessed[indexOf] = true
            }
        }
    }

    @SuppressLint("NewApi") //setProgress
    private fun addPercentage(amount: Int)
    {
        val pb = binding.progressB
        pb.setProgress(pb.progress + amount, true)

    }

    private fun isLevelCompleted()
    {
        if(binding.progressB.max == binding.progressB.progress)
        {
            println("level is completed")
            addGems(20)
        }
    }

    private fun addGems(count: Int)
    {
        gems += count
        updateGemUI()
    }

    private fun takeGems(count: Int)
    {
        gems -= count
        updateGemUI()
    }

    private fun updateGemUI()
    {
        binding.gemCount.text = gems.toString()
        saveData()
    }

    private fun giveHint()
    {
        var value = 25

        if (difficulty == 1)
            value = 35

        if(gems >= value)
        {
            takeGems(value)
            val indexOfWord = wasGuessed.lastIndexOf(false)
            binding.textInputLayout.hint = (wordsInPicture[indexOfWord][0]) + " " + "_ ".repeat(wordsInPicture[indexOfWord].length - 1)
        }
        else
        {
            Toast.makeText(applicationContext, "Not enough Gems [required $value]", Toast.LENGTH_LONG).show()
        }

    }

    //region SharedPrefs
    private fun saveData()
    {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putInt("Gem_KEY", gems)
        }.apply()
    }

    private fun loadData()
    {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        gems = sharedPreferences.getInt("Gem_KEY", 0)
        difficulty = sharedPreferences.getInt("Diff_KEY", 0)
        music = sharedPreferences.getInt("Music_KEY", 0)
        updateGemUI()
    }

    //endregion


    //region database operations
    private fun insertToDatabase(lvl: Int, wordName: String, percentage: Int)
    {
        val word = Word(0, lvl, wordName, percentage)
        wordVM.addWord(word)
    }
    private fun readDatabase()
    {
        wordVM.getAllWByLvl(level).observe(this) {
                word -> for(element in word) {
                    assignWord(element.word)}}
    }

    //endregion

}