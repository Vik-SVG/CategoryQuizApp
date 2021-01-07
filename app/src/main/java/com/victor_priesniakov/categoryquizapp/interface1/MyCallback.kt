package com.victor_priesniakov.categoryquizapp.interface1

import com.victor_priesniakov.categoryquizapp.model.Question

interface MyCallback {
    fun setQuestionList(questionList:List<Question>)
}