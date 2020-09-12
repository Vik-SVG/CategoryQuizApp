package com.victor_priesniakov.categoryquizapp.SQLhelper

import androidx.room.*
import com.victor_priesniakov.categoryquizapp.model.Question

@Dao
public interface QuestionsDao {

   @Query("SELECT * FROM Question WHERE CategoryId == :categoryId ORDER BY RANDOM() LIMIT 30")
    suspend fun getAllQuestionsByCategory(categoryId:Int):List<Question>

    @Insert
    suspend fun insert(question: Question?)

    @Update
    suspend fun update(question: Question?)

    @Delete
    suspend fun delete(question: Question?)
}