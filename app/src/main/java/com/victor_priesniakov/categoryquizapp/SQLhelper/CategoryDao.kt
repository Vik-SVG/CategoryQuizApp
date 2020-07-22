package com.victor_priesniakov.categoryquizapp.SQLhelper

import androidx.room.*
import com.victor_priesniakov.categoryquizapp.model.Category

@Dao
public interface CategoryDao {

    @Query("SELECT * FROM Category")
    fun getAllCategory():List<Category>

    @Insert
    fun insert(category: Category?)

    @Update
    fun update(category: Category?)

    @Delete
    fun delete(category: Category?)


}