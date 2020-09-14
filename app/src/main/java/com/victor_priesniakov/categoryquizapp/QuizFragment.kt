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
import com.victor_priesniakov.categoryquizapp.SQLhelper.DBHelper
import com.victor_priesniakov.categoryquizapp.SQLhelper.RoomDBHelper
import com.victor_priesniakov.categoryquizapp.adapter.CategoryAdapter
import com.victor_priesniakov.categoryquizapp.model.Category
import kotlinx.android.synthetic.main.fragment_quiz.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class QuizFragment : Fragment() {

    private var mDataBase:RoomDBHelper?=null
    private var mCategories:CategoryDao?=null
   private lateinit var mCategoryList:List<Category>

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
       // v.toolbar1.title = toolbarTitle




        //Room implem
        mRecycleView = v.findViewById<RecyclerView>(R.id.recycler_category)
        mRecycleView.setHasFixedSize(true)

        mRecycleView.layoutManager = GridLayoutManager(context, 2)
        val mItemDecoration = SpacesItemDescription(10)
        mRecycleView.addItemDecoration(mItemDecoration)


          //  mDataBase = RoomDBHelper.getAppDataBase(context as Context)
          //  mCategories = mDataBase?.categoryDao()
//TODO: adding coro


              /*runBlocking {
                  mCategoryList = RoomDBHelper.getAppDataBase(context as Context)?.categoryDao()
                      ?.getAllCategory()!!
              }*/

        mCategoryList = getAllmCat()


        val myAdapter = CategoryAdapter(context as Context, activity, mCategoryList)
        mRecycleView.adapter = myAdapter

        return v





       /*//sqlite impl
       mRecycleView = v.findViewById<RecyclerView>(R.id.recycler_category)
        mRecycleView.setHasFixedSize(true)

        mRecycleView.layoutManager = GridLayoutManager(context, 2)

        val adapter = CategoryAdapter(context as Context, DBHelper.getInstance(context as Context).allCategory)

        val mItemDecoration = SpacesItemDescription(10)
        mRecycleView.addItemDecoration(mItemDecoration)

        mRecycleView.adapter = adapter

        return v*/




    }


    private fun getAllmCat():List<Category> = runBlocking {
       val jobA = async {  RoomDBHelper.getAppDataBase(context as Context)?.categoryDao()?.getAllCategory()!! }
        jobA.await()
    }




    companion object {
        fun newInstance(): QuizFragment {
            val fragment: QuizFragment = QuizFragment()
            return fragment
        }

        fun newInstance1(): QuizFragment = QuizFragment()

    }

}