package com.victor_priesniakov.categoryquizapp

import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_quiz.*

class QuizActivity : SingleFragmentActivity() {

    override fun createFragment(): Fragment? {

        return QuizFragment.newInstance()
    }
}