package com.victor_priesniakov.categoryquizapp.model

import androidx.room.ColumnInfo
import androidx.room.ColumnInfo.INTEGER
import androidx.room.Entity
import androidx.room.PrimaryKey


 @Entity
data class Question constructor (@PrimaryKey
                     public  var ID:Int,
                    public var QuestionText:String?,
                    public   var QuestionImage:String?,
                    public   var AnswerA:String?,
                    public  var AnswerB:String?,
                    public   var AnswerC:String?,
                    public   var AnswerD:String?,
                    public  var CorrectAnswer:String?,
                 //   @ColumnInfo(typeAffinity = INTEGER)
                    public   var IsImageQuestion:Int?,
                    public  var CategoryID:Int?

) {
     constructor(): this(0,
     null,
     null,
     null,
         null,
         null,
         null,
         null,
         0,
         0
     )
 }

