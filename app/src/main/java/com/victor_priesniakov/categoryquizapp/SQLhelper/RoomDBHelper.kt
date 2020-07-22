package com.victor_priesniakov.categoryquizapp.SQLhelper

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.victor_priesniakov.categoryquizapp.model.Category
import com.victor_priesniakov.categoryquizapp.model.Questions

@Database(entities = [Category::class , Questions::class] , version = 1, exportSchema = false)
 abstract class RoomDBHelper: RoomDatabase() {

    abstract fun categoryDao():CategoryDao
    abstract fun questionsDao():QuestionsDao

    companion object {
        private var INSTANCE: RoomDBHelper? = null
        private const val DB_NAME = "DBQuiz2020.db"


        fun getAppDataBase(context: Context): RoomDBHelper? {

            if (INSTANCE == null){
                synchronized(RoomDBHelper::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, RoomDBHelper::class.java, DB_NAME)
                        .createFromAsset("databases/$DB_NAME")
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase(){
            INSTANCE = null
        }
    }

}
