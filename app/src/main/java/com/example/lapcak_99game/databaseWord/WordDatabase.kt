package com.example.lapcak_99game.databaseWord

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Abstraktná trieda [WordDatabase] sa rozširuje pomocou [RoomDatabase].
 */
@Database(entities = [Word::class], version = 1, exportSchema = false)
abstract class WordDatabase: RoomDatabase() {

    abstract fun wordDao(): WordDao

    companion object
    {
        @Volatile
        private  var INSTANCE: WordDatabase? = null

        /**
         * Metóda [getDatabase] vracia inštanciu databázy slovíčok.
         */
        fun getDatabase(context: Context): WordDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null)
            {
                return tempInstance
            }
            synchronized(this)
            {
                val instance = Room.databaseBuilder(context.applicationContext, WordDatabase::class.java, "word_database").build()
                INSTANCE = instance
                return instance
            }
        }
    }
}