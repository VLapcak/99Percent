package com.example.lapcak_99game.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lapcak_99game.R
import com.example.lapcak_99game.recyclerView.Picture
import com.example.lapcak_99game.recyclerView.PictureAdapter
import com.example.lapcak_99game.databaseWord.WordViewModel
import com.example.lapcak_99game.databinding.ActivityLevelSelectorBinding
/**
 * Trieda [LevelSelector] (výber levelov).
 * Umožnuje užívateľovi vybrať si level, ktorý chce hrať.
 * Má na starosti zobraziť karty s názvom levelu,
 * percentami a obrázkom, ktoré pridáva do recycler view.
 */

class LevelSelector : AppCompatActivity() {
    private lateinit var binding: ActivityLevelSelectorBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var pictureList: ArrayList<Picture>
    private lateinit var pictureAdapter: PictureAdapter
    private lateinit var wordVM: WordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevelSelectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        wordVM = ViewModelProvider(this)[WordViewModel::class.java]

        addCards()

        recyclerView = binding.levelRecycler
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        pictureAdapter = PictureAdapter(pictureList)
        recyclerView.adapter = pictureAdapter

        buttons()
    }

    /**
     * Metóda [addCards] pridáva karty do ArrayListu [pictureList] neskôr použité v Recycler View.
     * Pre každý element v [pictureList] obnoví percentá pomocou metódy [updatePercentagesByLevel].
     */
    private fun addCards()
    {
        pictureList = ArrayList()
        pictureList.add(Picture(R.drawable.church, "Level 1", 0, 1))
        pictureList.add(Picture(R.drawable.uniza_heart, "Level 2", 0, 2))
        pictureList.add(Picture(R.drawable.rectorate, "Level 3", 0, 3))
        pictureList.add(Picture(R.drawable.opatia, "Level 4", 0, 4))
        for (i in 1..pictureList.size) {updatePercentagesByLevel(i)}
    }

    /**
     * Metóda [buttons] inicializuje talčidlá v triede.
     * [PictureAdapter.onItemClick] po kliknutí pošle dodatočné informácie do triedy [GameActivity] a spustí ju.
     * Tlačidlo GoBackLevel, reprezenujúce návrat do menu zapne aktivitu [MainActivity].
     * Tlačidlo Filter zabezpečí preorganizovanie kariet v recycler view podľa percent zostupne alebo podľa názvu.
     */
    private fun buttons()
    {
        pictureAdapter.onItemClick = {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("level", it.level)
            intent.putExtra("pic", it.image.toString())
            startActivity(intent)
        }
        binding.goBackLevel.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        var clicked = false
        binding.filter.setOnClickListener {
            if (!clicked) {
                pictureList.sortByDescending {
                    it.percentage
                }
                pictureAdapter.notifyDataSetChanged()
                clicked = true
            }
            else
            {
                pictureList.sortBy{
                    it.name
                }
                pictureAdapter.notifyDataSetChanged()
                clicked = false
            }
        }
    }

    /**
     * Metóda [updatePercentagesByLevel] načita percentá slovíčok z
     * View Modelu [wordVM] prepojená na databázu.
     * @param level určuje pre ktorý level má obnoviť percentá.
     */
    private fun updatePercentagesByLevel(level: Int)
    {
        wordVM.getAllWByLvl(level).observe(this) { word ->
            for (element in word) {
                pictureList[level -1].percentage += element.percentage
                pictureAdapter.notifyDataSetChanged()
            }
        }
    }

}