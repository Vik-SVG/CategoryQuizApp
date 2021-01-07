package com.victor_priesniakov.categoryquizapp

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog
import com.victor_priesniakov.categoryquizapp.Common.Common
import com.victor_priesniakov.categoryquizapp.Common.SpaceItemDecoration
import com.victor_priesniakov.categoryquizapp.SQLhelper.CategoryDao
import com.victor_priesniakov.categoryquizapp.SQLhelper.RoomDBHelper
import com.victor_priesniakov.categoryquizapp.adapter.CategoryAdapter
import com.victor_priesniakov.categoryquizapp.model.Category
import io.paperdb.Paper
import kotlinx.android.synthetic.main.fragment_quiz.view.*
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking


class QuizFragment : Fragment() {

    private var mDataBase:RoomDBHelper?=null
    private var mCategories:CategoryDao?=null
    private lateinit var mCategoryList:List<Category>
    lateinit var mRecycleView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fragment_quiz, container, false)

        v.toolbar1.title = ""

        //TODO 12:00

        Paper.init(activity as Context)
        Common.isOnlineMode = Paper.book().read(Common.KEY_SAVE_ONLINE_MODE, false)

        val mToolbar = v.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar1)
        (activity as AppCompatActivity).setSupportActionBar(mToolbar)

        setHasOptionsMenu(true)


        mRecycleView = v.findViewById<RecyclerView>(R.id.recycler_category)
        mRecycleView.setHasFixedSize(true)

        mRecycleView.layoutManager = GridLayoutManager(context, 2)
        val mItemDecoration = SpaceItemDecoration(10)
        mRecycleView.addItemDecoration(mItemDecoration)

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.category_menu, menu);
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.menu_settings){
            showSettings()
        }
   //     return super.onOptionsItemSelected(item)
    return true
    }

    private fun showSettings() {
        var settingsLayout = LayoutInflater.from(activity as Context).inflate(R.layout.settings_layout, null)
        var ckbOnlineMode = settingsLayout.findViewById<CheckBox>(R.id.ckb_onlline_mode)

        ckbOnlineMode.setChecked(Paper.book().read(Common.KEY_SAVE_ONLINE_MODE, false))

        MaterialStyledDialog.Builder(activity as Context)
            .setIcon(R.drawable.ic_baseline_settings_24)
            .setTitle("Settings")
            .setDescription("Please choose action")
            .setCustomView(settingsLayout)
            .setNegativeText("Dismiss")
            .setCancelable(true)
            .setPositiveText("Save")
            .onPositive {
                Common.isOnlineMode = ckbOnlineMode.isChecked
                Paper.book().write(Common.KEY_SAVE_ONLINE_MODE, ckbOnlineMode.isChecked)


            }.show()
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