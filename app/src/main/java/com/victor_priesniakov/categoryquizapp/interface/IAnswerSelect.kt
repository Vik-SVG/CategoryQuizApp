package com.victor_priesniakov.categoryquizapp.`interface`

import com.victor_priesniakov.categoryquizapp.model.CurrentQuestion

interface IAnswerSelect {
    fun selectedAnswer():CurrentQuestion
    fun showCorrectAnswer()
    fun disableAnswer()
    fun resetQuestion()
}