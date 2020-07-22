package com.victor_priesniakov.categoryquizapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.victor_priesniakov.categoryquizapp.Common.SpacesItemDescription
import com.victor_priesniakov.categoryquizapp.SQLhelper.CategoryDao
import com.victor_priesniakov.categoryquizapp.SQLhelper.RoomDBHelper
import com.victor_priesniakov.categoryquizapp.adapter.CategoryAdapter
import com.victor_priesniakov.categoryquizapp.model.Category
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_quiz.view.*


class QuizFragment : Fragment() {

    private var mDataBase:RoomDBHelper?=null
    private var mCategories:CategoryDao?=null

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
        val mItemDecoration = SpacesItemDescription(10)
        mRecycleView.addItemDecoration(mItemDecoration)


            mDataBase = RoomDBHelper.getAppDataBase(context as Context)
            mCategories = mDataBase?.categoryDao()
        val mCategoryList:List<Category>? = mCategories?.getAllCategory()

        val myAdapter = CategoryAdapter(context as Context, mCategoryList as List<Category>)
        mRecycleView.adapter = myAdapter

        return v
    }

    companion object {
        fun newInstance(): QuizFragment {
            val fragment: QuizFragment = QuizFragment()
            return fragment
        }
    }

}