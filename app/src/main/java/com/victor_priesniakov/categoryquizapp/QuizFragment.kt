package com.victor_priesniakov.categoryquizapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.victor_priesniakov.categoryquizapp.Common.SpacesItemDescription
import com.victor_priesniakov.categoryquizapp.SQLhelper.DBHelper
import com.victor_priesniakov.categoryquizapp.adapter.CategoryAdapter
import kotlinx.android.synthetic.main.fragment_quiz.*
import kotlinx.android.synthetic.main.fragment_quiz.view.*




class QuizFragment : Fragment() {

    val toolbarTitle = "QUIZ APP 2020"
    lateinit var mRecycleView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        val v = inflater.inflate(R.layout.fragment_quiz, container, false)
        v.toolbar.title = toolbarTitle

        mRecycleView = v.findViewById<RecyclerView>(R.id.recycler_category)
        mRecycleView.setHasFixedSize(true)

        mRecycleView.layoutManager = GridLayoutManager(context, 2)

        val adapter = CategoryAdapter(context as Context, DBHelper.getInstance(context as Context).allCategory)

        val mItemDecoration = SpacesItemDescription(10)
        mRecycleView.addItemDecoration(mItemDecoration)

        mRecycleView.adapter = adapter

        return v
    }

    companion object {
        fun newInstance(): QuizFragment {
            val fragment: QuizFragment = QuizFragment()
            return fragment
        }
    }

}