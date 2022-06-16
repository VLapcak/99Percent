package com.example.lapcak_99game.activity

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.example.lapcak_99game.R
import com.example.lapcak_99game.databaseWord.WordViewModel
import com.example.lapcak_99game.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

/**
 * Trieda [MainActivity] je prvá, ktorá sa zobrazí pri otvorení aplikácie.
 * Zabezpečí tak hlavnú obrazovku, z ktorej je možné sa navigovať do iných obrazoviek.
 */
class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var wordVM: WordViewModel

    private var difficulty: Int = 0
    private var music: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        wordVM = ViewModelProvider(this)[WordViewModel::class.java]

        sidePanel()
        playMusic()
        buttons()
    }

    /**
     * Metóda [sidePanel] inicializuje bočný panel nastavení.
     * Volá metódu [updateSettingsBar] pre získanie aktuálnych informácii.
     * Pre hráča to znamená že môže si zvoliť obtiažnosť,
     * vypnutie/ zapnutie hudby v pozadí,
     * resetovanie progresu,
     * informácie ako hrať.
     */
    private fun sidePanel() {
        val navView: NavigationView = binding.navView
        updateSettingsBar(navView)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_diff_easy -> {
                    Toast.makeText(
                        applicationContext,
                        "Easy difficulty selected",
                        Toast.LENGTH_SHORT
                    ).show()
                    saveData("difficulty", 0)
                    updateSettingsBar(navView)
                }
                R.id.nav_diff_hard -> {
                    Toast.makeText(
                        applicationContext,
                        "Hard difficulty selected",
                        Toast.LENGTH_SHORT
                    ).show()
                    saveData("difficulty", 1)
                    updateSettingsBar(navView)
                }
                R.id.nav_music_on -> {
                    Toast.makeText(applicationContext, "Music ON", Toast.LENGTH_SHORT).show()
                    saveData("music", 0)
                    updateSettingsBar(navView)
                }
                R.id.nav_music_off -> {
                    Toast.makeText(applicationContext, "Music OFF", Toast.LENGTH_SHORT).show()
                    saveData("music", 1)
                    updateSettingsBar(navView)
                }
                R.id.nav_resetProgress -> {
                    Toast.makeText(applicationContext, "Progress Reset", Toast.LENGTH_SHORT).show()
                    wordVM.clearDatabase()
                    saveData("gems", 0)
                }
                R.id.nav_about_game -> {
                    val intent = Intent(this, HowToPlay::class.java)
                    startActivity(intent)
                }
            }
            true
        }
    }

    /**
     * Metóda [buttons] inicializuje talčidlá v triede.
     * Tlačidlo Options otvára bočný panel nastavení zľava.
     * Tlačidlo Play zabezpečí presunutie do triedy [LevelSelector].
     */
    private fun buttons()
    {
        val drawerLayout: DrawerLayout = binding.drawerLayout
        binding.options.setOnClickListener{
            drawerLayout.openDrawer(Gravity.LEFT)
        }

        binding.Play.setOnClickListener{
            val intent = Intent(this, LevelSelector::class.java)
            startActivity(intent)
            println("click")
        }
    }

    //region SharedPrefs
    /**
     * Metóda [saveData] slúži na ukladanie dát pre ich zachovanie aj po ukončení aplikácie.
     * @param prefsName názov do ktorej má uložiť hodnotu.
     * @param value hodnota, ktorá sa zapíše do kľúča s daným názvom.
     */
    private fun saveData(prefsName: String, value: Int)
    {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        when (prefsName)
        {
            "gems" -> editor.apply {putInt("Gem_KEY", value)}.apply()
            "difficulty" -> editor.apply {putInt("Diff_KEY", value)}.apply()
            "music" -> editor.apply {putInt("Music_KEY", value)}.apply()
            else -> println("error has occurred")
        }
    }
    /**
     * Metóda [loadData] slúži na načítanie dát.
     * [difficulty] prepíše na aktulánu hodnotu (0 = EASY, 1 = HARD).
     * [music] prepíše na aktuálnu hodnotu (0 = ON, 1 = OFF).
     */
    private fun loadData()
    {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        difficulty = sharedPreferences.getInt("Diff_KEY", 0)
        music = sharedPreferences.getInt("Music_KEY", 0)
    }


    //endregion

    /**
     * Metóda [playMusic] overí či existuje [MediaPlayer] a v prípade,
     * že [music] je nastavený na ON (0), tak spustí hudbu.
     */
    private fun playMusic() {
        var player: MediaPlayer? = null
        if (music == 0 && player == null)
        {
            player = MediaPlayer.create(this, R.raw.bg_music)
            player!!.isLooping = true
            player.start()

        }
    }

    /**
     * Metóda [updateSettingsBar] zavolá funkciu [loadData] pre získanie aktuálnych hodnôt.
     * @param navigationView sprístupní [NavigationView] vďaka, ktorému sa zabezpečí
     * mannipulácia s jednotlivými položkami v bočnom paneli nastavení.
     * Prebieha kontrola zvýraznenia(označenia) položiek.
     */
    private fun updateSettingsBar(navigationView: NavigationView)
    {
        loadData()
        navigationView.menu.findItem(R.id.nav_music_on).isChecked = false
        navigationView.menu.findItem(R.id.nav_music_off).isChecked = false
        navigationView.menu.findItem(R.id.nav_diff_easy).isChecked = false
        navigationView.menu.findItem(R.id.nav_diff_hard).isChecked = false

        if (music == 0) {
            navigationView.menu.findItem(R.id.nav_music_on).isChecked = true
        }
        else
            navigationView.menu.findItem(R.id.nav_music_off).isChecked = true

        if (difficulty == 0)
            navigationView.menu.findItem(R.id.nav_diff_easy).isChecked = true
        else
            navigationView.menu.findItem(R.id.nav_diff_hard).isChecked = true
    }
}