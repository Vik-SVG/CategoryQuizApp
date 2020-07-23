package com.victor_priesniakov.categoryquizapp.SQLhelper

import androidx.room.*
import com.victor_priesniakov.categoryquizapp.model.Question

@Dao
public interface QuestionsDao {

   @Query("SELECT * FROM Question WHERE CategoryId == :categoryId ORDER BY RANDOM() LIMIT 30")
    fun getAllQuestionsByCategory(categoryId:Int):List<Question>

    @Insert
    fun insert(question: Question?)

    @Update
    fun update(question: Question?)

    @Delete
    fun delete(question: Question?)
}