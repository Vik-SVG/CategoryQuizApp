package com.victor_priesniakov.categoryquizapp.Common

import com.victor_priesniakov.categoryquizapp.QuestionFragment
import com.victor_priesniakov.categoryquizapp.model.Category
import com.victor_priesniakov.categoryquizapp.model.CurrentQuestion
import com.victor_priesniakov.categoryquizapp.model.Question
import java.lang.StringBuilder

object Common {
    val TOTAL_TIME = 20*60*1000 //20 min

    var myAnswerSheetList:MutableList<CurrentQuestion> = ArrayList()
    var questionList:MutableList<Question> = ArrayList()
    var selectedCategory:Category?=null

    var fragmentList:MutableList<QuestionFragment> = ArrayList()

    var selectedValues:MutableList<String> = ArrayList()

    var timer = 0
    var right_answer_count = 0
    var wrong_answer_count = 0
    var no_answer_count = 0
    var data_question = StringBuilder()


    enum class ANSWER_TYPE{
        NO_ANSWER,
        RIGHT_ANSWER,
        WRONG_ANSWER
    }
}