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

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    lateinit var wordVM: WordViewModel

    private var gems: Int = 0
    private var difficulty: Int = 0
    private var music: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        wordVM = ViewModelProvider(this)[WordViewModel::class.java]

        val playBtn = binding.Play
        playBtn.setOnClickListener{
            val intent = Intent(this, LevelSelector::class.java)
            startActivity(intent)
            println("click")
        }

        val navView: NavigationView = binding.navView
        val drawerLayout: DrawerLayout = binding.drawerLayout

        val openMenu = binding.options
        openMenu.setOnClickListener{
            drawerLayout.openDrawer(Gravity.LEFT)
        }

        navView.setNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.nav_diff_easy -> {
                    Toast.makeText(applicationContext, "Easy difficulty selected", Toast.LENGTH_SHORT).show()
                    saveData("difficulty", 0)
                }
                R.id.nav_diff_hard -> {
                    Toast.makeText(applicationContext, "Hard difficulty selected", Toast.LENGTH_SHORT).show()
                    saveData("difficulty", 1)
                }
                R.id.nav_music_on -> {
                    Toast.makeText(applicationContext, "Music ON", Toast.LENGTH_SHORT).show()
                    saveData("music", 0)
                }
                R.id.nav_music_off -> {
                    Toast.makeText(applicationContext, "Music OFF", Toast.LENGTH_SHORT).show()
                    saveData("music", 1)
                }
                R.id.nav_resetProgress -> {
                    Toast.makeText(applicationContext, "Progress Reset", Toast.LENGTH_SHORT).show()
                    wordVM.clearDatbase()
                    saveData("gems", 0)
                }
            }
            true
        }

        playMusic()
    }

    //region SharedPrefs
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

    private fun loadData(prefsName: String, value: Int)
    {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)

        when (prefsName)
        {
            "gems" -> gems = sharedPreferences.getInt("Gem_KEY", 0)
            "difficulty" -> difficulty = sharedPreferences.getInt("Diff_KEY", 0)
            "music" -> music = sharedPreferences.getInt("Music_KEY", 0)
            else -> println("error has occurred")
        }

    }

    //endregion

    private fun playMusic() {
        val musicON = true
        var player: MediaPlayer? = null
        if (musicON && player == null)
        {
            player = MediaPlayer.create(this, R.raw.bg_music)
            player!!.isLooping = true
            player!!.start()
        }
    }
}