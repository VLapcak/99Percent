package com.example.lapcak_99game.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lapcak_99game.R
import com.example.lapcak_99game.RecyclerView.Picture
import com.example.lapcak_99game.RecyclerView.PictureAdapter
import com.example.lapcak_99game.databaseWord.WordViewModel
import com.example.lapcak_99game.databinding.ActivityLevelSelectorBinding

class LevelSelector : AppCompatActivity() {
    lateinit var binding: ActivityLevelSelectorBinding
    lateinit var recyclerView: RecyclerView
    lateinit var pictureList: ArrayList<Picture>
    lateinit var pictureAdapter: PictureAdapter
    lateinit var wordVM: WordViewModel
    private var sum: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevelSelectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        wordVM = ViewModelProvider(this)[WordViewModel::class.java]

        //add cads to recyclerView
        pictureList = ArrayList()
        pictureList.add(Picture(R.drawable.church, "Level 1", 0, 1))
        pictureList.add(Picture(R.drawable.uniza_heart, "Level 2", 0, 2))
        for (i in 1..pictureList.size) {getPercentagesByLevel(i)}


        recyclerView = binding.levelRecycler
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        pictureAdapter = PictureAdapter(pictureList)
        recyclerView.adapter = pictureAdapter

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
    }
    private fun getPercentagesByLevel(level: Int)
    {
        wordVM.getAllWByLvl(level).observe(this) { word ->
            for (element in word) {
                pictureList[level -1].percentage += element.percentage
                pictureAdapter.notifyDataSetChanged()
            }
        }
    }

}