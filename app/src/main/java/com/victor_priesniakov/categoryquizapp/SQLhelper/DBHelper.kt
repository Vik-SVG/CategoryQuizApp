package com.victor_priesniakov.categoryquizapp.SQLhelper

import android.content.Context
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper
import com.victor_priesniakov.categoryquizapp.model.Category
import com.victor_priesniakov.categoryquizapp.model.Questions

class DBHelper (context: Context):SQLiteAssetHelper(context, DB_NAME, null, DB_VER){

    companion object{
        private var instance:DBHelper? = null
        private val DB_NAME = "DBQuiz2020.db"
        private val DB_VER = 1

        @Synchronized
        fun getInstance(context: Context):DBHelper{
            if (instance==null)
                instance = DBHelper(context)
            return instance!!
        }
    }

    //get all category
    val allCategory:MutableList<Category>
    get() {
        val db = instance!!.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM Category", null)
        val categories = ArrayList<Category>()
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast){
                val category = Category(cursor.getInt(cursor.getColumnIndex("ID")),
                cursor.getString(cursor.getColumnIndex("Name")),
                cursor.getString(cursor.getColumnIndex("Image")))
                categories.add(category)
                cursor.moveToNext()
            }
        }
        cursor.close()
        db.close()
        return categories
    }

    //get all questions by category

    fun getQuestionsByCategory(categoryId:Int):MutableList<Questions>{
        val db = instance!!.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM Questions WHERE CategoryId=$categoryId ORDER BY RANDOM() LIMIT30", null)
        val questionList = ArrayList<Questions>()
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast){
                val question = Questions(cursor.getInt(cursor.getColumnIndex("ID")),
                    cursor.getString(cursor.getColumnIndex("QuestionText")),
                    cursor.getString(cursor.getColumnIndex("QuestionImage")),
                    cursor.getString(cursor.getColumnIndex("AnswerA")),
                    cursor.getString(cursor.getColumnIndex("AnswerB")),
                    cursor.getString(cursor.getColumnIndex("AnswerC")),
                    cursor.getString(cursor.getColumnIndex("AnswerD")),
                    cursor.getString(cursor.getColumnIndex("CorrectAnswer")),
                    if(cursor.getInt(cursor.getColumnIndex("IsImageQuestion")) == 0) java.lang.Boolean.FALSE else java.lang.Boolean.TRUE,
                    cursor.getInt(cursor.getColumnIndex("CategoryID")))
                questionList.add(question)
                cursor.moveToNext()
            }
        }
        cursor.close()
        db.close()

    return questionList
    }

}