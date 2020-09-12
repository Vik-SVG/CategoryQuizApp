package com.victor_priesniakov.categoryquizapp.SQLhelper

import androidx.lifecycle.LiveData
import androidx.room.*
import com.victor_priesniakov.categoryquizapp.model.Category

@Dao
public interface CategoryDao {

    @Query("SELECT * FROM Category")
    suspend fun getAllCategory():List<Category>

  /*  @Query("SELECT * FROM Category")
   suspend fun getAllCategory():LiveData<List<Category>>*/

    @Insert
    suspend fun insert(category: Category?)

    @Update
    suspend fun update(category: Category?)

    @Delete
    suspend fun delete(category: Category?)


}