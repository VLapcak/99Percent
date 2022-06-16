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

/**
 * Trieda [GameActivity] je hlavný mozog hry, kde sa manažuje priebeh a kontrolu hry,
 * to kde hráč háda slovíčka.
 */

class GameActivity : AppCompatActivity() {

    private lateinit var wordsInPicture: List<String>
    private lateinit var synonymWordsInPicture: List<String>
    private lateinit var wordsPercentage: List<String>
    private lateinit var guessedWords: ArrayList<TextView>
    private lateinit var wasGuessed: BooleanArray
    private lateinit var binding: ActivityGameBinding
    private lateinit var wordVM: WordViewModel

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

        guessedWords = arrayListOf(binding.text1, binding.text2, binding.text3, binding.text4, binding.text5, binding.text6, binding.text7, binding.text8, binding.text9, binding.text10)


        buttons()
        setWordData()

    }

    /**
     * Metóda [buttons] inicializuje talčidlá v triede.
     * Hint spúšťa metódu [giveHint]
     * Locate prepne obrazovku na [MapActivity], kde pošle dodatočné informácie.
     * Send zavolá metódu [checkForWord], a resetuje pole, kde sa zadávajú slovíčka.
     * GoBack vráti hráča späť na obrazovku manažovanú triedou [LevelSelector].
     */
    private fun buttons()
    {
        binding.hint.setOnClickListener {
            giveHint()
        }

        binding.locate.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("location", level)
            startActivity(intent)
        }

        val inputText = binding.textInput
        binding.send.setOnClickListener {
            checkForWord(inputText.text.toString())
            inputText.setText("")
            inputText.clearFocus()
        }

        binding.goBack.setOnClickListener {
            val intent = Intent(this, LevelSelector::class.java)
            startActivity(intent)
        }
    }

    /**
     * Metóda [setWordData] zabezpečuje načítanie pomocou [readDatabase], [loadData]
     * a inicializovanie slovíčok vyberaných z textových súborov.
     *
     */
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
        val allSynonymWords = assets.open("levelSynonymWords.txt").bufferedReader().use { it.readLines() }
        synonymWordsInPicture = allSynonymWords.slice(range)

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

    /**
     * Metóda [checkForWord] zabezpečí kontrolu slovíčok, ktoré hráč zadal do poľa.
     * Podľa obťiažnosti určí či hráč musí presne uhádnuť dané slovíčko alebo má možnosť synonyma.
     * Používa metódy [addPercentage], [insertToDatabase], [isLevelCompleted], [addGems].
     * Pre oznámenie hráčovi ukáže správu za použitia [Toast].
     */
    private fun checkForWord(word: String)
    {
        var wordL = word.lowercase(Locale.getDefault())
        if(synonymWordsInPicture.contains(wordL) && wordL != "" && difficulty == 0) // difficutly EASY advantage of synonyms
        {
            wordL = wordsInPicture[synonymWordsInPicture.indexOf(wordL)]

        }
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

    /**
     * Metóda [assignWord] slúži hlavne na prvotnú inicializáciu slovíčok z databázy,
     * otestuje či dané slovíčko sa už nachádza v množine slovíčok,
     * ktoré sú načítané pomocou [setWordData].
     * Pre nezaťažovanie procesoru a pamäte kontroluje či bolo slovíčko uhádnuté v množine [wasGuessed].
     * Zavolá metódu [addPercentage], pre pridanie percent.
     */
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

    /**
     * Metóda [addPercentage] nastaví progres resp. percentá v progres bare.
     */
    @SuppressLint("NewApi") //setProgress
    private fun addPercentage(amount: Int)
    {
        val pb = binding.progressB
        pb.setProgress(pb.progress + amount, true)
    }

    /**
     * Metóda [isLevelCompleted] kontroluje či je level ukončený.
     * V prípade že áno, zavolá metódu [addGems].
     */
    private fun isLevelCompleted()
    {
        if(binding.progressB.max == binding.progressB.progress)
        {
            println("level is completed")
            addGems(20)
        }
    }

    /**
     * Metóda [addGems] pridá hráčovi gemy
     * a zavolá metódu [updateGemUI].
     */
    private fun addGems(count: Int)
    {
        gems += count
        updateGemUI()
    }

    /**
     * Metóda [takeGems] odoberie hráčovi gemy
     * a zavolá metódu [updateGemUI].
     */
    private fun takeGems(count: Int)
    {
        gems -= count
        updateGemUI()
    }

    /**
     * Metóda [updateGemUI] aktualizuje hodnotu gemov v UI,
     * aby bol pre hráča viditelný ich aktuálny stav.
     * Zavolá metódu [saveData] pre uloženie zmien.
     */
    private fun updateGemUI()
    {
        binding.gemCount.text = gems.toString()
        saveData()
    }

    /**
     * Metóda [giveHint] dá hráčovi nápovedu.
     * Na základe obťižnosti určí cenu v gemoch.
     * Ak má hráč dostatok gemov, zavolá metódu [takeGems].
     * Hráčovi sa v poli zadávania slovíčok obajví prvé písmenko
     * vždy neuhádnutého slovíčka s najmenším počtom percent.
     */
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
    /**
     * Metóda [saveData] slúži na ukladanie dát pre ich zachovanie aj po ukončení aplikácie.
     * V tejto triede [GameActivity] nie je potrebné ukladať nič iné iba gems, preto nepoužíva parametre.
     */
    private fun saveData()
    {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putInt("Gem_KEY", gems)
        }.apply()
    }

    /**
     * Metóda [loadData] slúži na načítanie dát.
     * [gems] prepíše na aktuálny počet gemov, ktoré hráč nazbieral.
     * [difficulty] prepíše na aktulánu hodnotu (0 = EASY, 1 = HARD).
     * [music] prepíše na aktuálnu hodnotu (0 = ON, 1 = OFF).
     */
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
    /**
     * Metóda [insertToDatabase] zabezpečuje vkladanie uhádnutých slovíčok do databázy.
     * Zavolá [WordViewModel] pre pridanie slovíčka.
     */
    private fun insertToDatabase(lvl: Int, wordName: String, percentage: Int)
    {
        val word = Word(0, lvl, wordName, percentage)
        wordVM.addWord(word)
    }

    /**
     * Metóda [readDatabase] slúži na načítanie slovíčok z databázy
     * a priradenie ho cez [assignWord].
     */
    private fun readDatabase()
    {
        wordVM.getAllWByLvl(level).observe(this) {
                word -> for(element in word) {
                    assignWord(element.word)}}
    }

    //endregion

}