package com.victor_priesniakov.categoryquizapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category (
    @PrimaryKey
@ColumnInfo(name = "ID")
    public  var ID:Int,

                     @ColumnInfo(name = "Name")
                     public  var name: String?,

                     @ColumnInfo(name = "Image")
                     public var Image:String?){
}