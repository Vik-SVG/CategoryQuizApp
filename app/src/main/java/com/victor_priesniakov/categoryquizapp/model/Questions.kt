package com.victor_priesniakov.categoryquizapp.model

class Questions(var id:Int,
                var questionText:String?,
                var questionImage:String?,
                var answerA:String?,
                var answerB:String?,
                var answerC:String?,
                var answerD:String?,
                var correctAnswer:String?,
                var isImageQuestion:Boolean,
                var categoryId:Int

) {
}