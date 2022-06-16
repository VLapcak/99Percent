package com.example.lapcak_99game.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lapcak_99game.databinding.ActivityHowToPlayBinding

/**
 * @author Viliam Lapčák
 * Trieda [HowToPlay] má informačný charakter, kde má hráča oboznámiť s hrou.
 * Hráč sa tu dozvie:
 * - ako fungujú dané obtiažnosti
 * - ako funguje herná mena Gemy
 * - ako funguje poloha.
 */
class HowToPlay : AppCompatActivity() {

    lateinit var binding: ActivityHowToPlayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHowToPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttons()
    }

    /**
     * Metóda [buttons] inicializuje talčidlá v triede
     */
    private fun buttons()
    {
        binding.goBackMenu.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}