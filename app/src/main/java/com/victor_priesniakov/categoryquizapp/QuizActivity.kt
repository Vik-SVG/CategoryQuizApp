package com.victor_priesniakov.categoryquizapp

import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_question.*

class QuizActivity : SingleFragmentActivity() {

    private lateinit var mDrawerLayout:DrawerLayout

    override fun createFragment(): Fragment? {

        return QuizFragment.newInstance1()
    }

    /* override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

        val mItem = menu!!.findItem(R.id.txt_wrong_answer)
        val layout = mItem.actionView as ConstraintLayout
        val mTxt_wrong_answer = layout.findViewById(R.id.txt_wrong_answer) as TextView
        mTxt_wrong_answer.text = 0.toString()
        return true
    }*/



}