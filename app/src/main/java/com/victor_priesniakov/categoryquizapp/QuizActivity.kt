package com.victor_priesniakov.categoryquizapp

import androidx.fragment.app.Fragment

class QuizActivity : SingleFragmentActivity() {

    override fun createFragment(): Fragment? {

        return QuizFragment.newInstance()
    }
}