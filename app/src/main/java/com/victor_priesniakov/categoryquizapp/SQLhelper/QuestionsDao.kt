package com.victor_priesniakov.categoryquizapp.SQLhelper

import androidx.room.*
import com.victor_priesniakov.categoryquizapp.model.Questions

@Dao
public interface QuestionsDao {

   @Query("SELECT * FROM Questions WHERE CategoryId == :categoryId ORDER BY RANDOM() LIMIT 30")
    fun getAllQuestionsByCategory(categoryId:Int):List<Questions>

    @Insert
    fun insert(questions: Questions?)

    @Update
    fun update(questions: Questions?)

    @Delete
    fun delete(questions: Questions?)
}