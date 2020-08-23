package com.victor_priesniakov.categoryquizapp.interface1

import com.victor_priesniakov.categoryquizapp.model.CurrentQuestion

public interface IAnswerSelect {
    fun selectedAnswer():CurrentQuestion
    fun showCorrectAnswer()
    fun disableAnswer()
    fun resetQuestion()
}