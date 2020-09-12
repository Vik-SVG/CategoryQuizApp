package com.victor_priesniakov.categoryquizapp.SQLhelper

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.victor_priesniakov.categoryquizapp.model.Category
import com.victor_priesniakov.categoryquizapp.model.Question
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Category::class , Question::class] , version = 1, exportSchema = false)
 abstract class RoomDBHelper: RoomDatabase() {

    abstract fun categoryDao():CategoryDao
    abstract fun questionsDao():QuestionsDao

    companion object {
        private var INSTANCE: RoomDBHelper? = null
        private const val DB_NAME = "DBQuiz2020.db"


        fun getAppDataBase(context: Context /*,scope:CoroutineScope*/): RoomDBHelper? {

            if (INSTANCE == null){
               // synchronized(RoomDBHelper::class){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        RoomDBHelper::class.java, DB_NAME)
                        .createFromAsset("databases/$DB_NAME")
                     //   .addCallback(QuizDatabaseCallback(scope))
                        //.allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase(){
            INSTANCE = null
        }
    }

    /*class QuizDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let {database ->
               // scope.launch {  }
            }

        }

    }*/

}
