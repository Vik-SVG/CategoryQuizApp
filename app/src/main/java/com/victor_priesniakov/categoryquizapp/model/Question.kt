package com.victor_priesniakov.categoryquizapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Question(@PrimaryKey
                     public  var ID:Int,
                    public var QuestionText:String?,
                    public   var QuestionImage:String?,
                    public   var AnswerA:String?,
                    public  var AnswerB:String?,
                    public   var AnswerC:String?,
                    public   var AnswerD:String?,
                    public  var CorrectAnswer:String?,
                    public   var IsImageQuestion:Boolean,
                    public  var CategoryId:Int

) {
}