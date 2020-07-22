package com.victor_priesniakov.categoryquizapp

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog
import com.victor_priesniakov.categoryquizapp.Common.Common
import com.victor_priesniakov.categoryquizapp.SQLhelper.DBHelper
import com.victor_priesniakov.categoryquizapp.SQLhelper.QuestionsDao
import com.victor_priesniakov.categoryquizapp.SQLhelper.RoomDBHelper
import com.victor_priesniakov.categoryquizapp.adapter.GridAnswerAdapter
import com.victor_priesniakov.categoryquizapp.adapter.MyFragmentAdapter
import com.victor_priesniakov.categoryquizapp.model.CurrentQuestion
import com.victor_priesniakov.categoryquizapp.model.Questions
import kotlinx.android.synthetic.main.activity_question.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.concurrent.TimeUnit

class QuestionActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

   private var mQuestionsDB:RoomDBHelper? = null
    private var mQuestions: QuestionsDao? =null
    lateinit var countDownTimer:CountDownTimer
    var time_play = Common.TOTAL_TIME
    lateinit var adapter: GridAnswerAdapter
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var myViewPager:ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)
        val toolbar: Toolbar = findViewById(R.id.toolbar2)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)



        /*val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.app_bar_scroling_view)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)*/





        genQuestion()

        if (Common.questionList.size>0){
            txt_timer.visibility = View.VISIBLE
            txt_right_answer.visibility = View.VISIBLE
            countTimer()
            genItems()
            grid_answer.setHasFixedSize(true)
            if (Common.questionList.size>0)
                grid_answer.layoutManager = GridLayoutManager(this,
                    if (Common.questionList.size>10)
                        Common.questionList.size/2
                    else Common.questionList.size)
            adapter = GridAnswerAdapter(this, Common.myAnswerSheetList)
            grid_answer.adapter = adapter
            genFragmentList()

            val fragmentAdapter = MyFragmentAdapter(supportFragmentManager, this, Common.fragmentList)

            view_pager.offscreenPageLimit = Common.questionList.size
            view_pager.adapter = fragmentAdapter

           /* myViewPager = findViewById(R.id.view_pager)
            myViewPager.offscreenPageLimit = Common.questionList.size
            myViewPager.adapter = fragmentAdapter
             sliding_tabs.setupWithViewPager(myViewPager)*/

            sliding_tabs.setupWithViewPager(view_pager)

        }
    }

    private fun genFragmentList() {
        for (i in Common.questionList.indices){
            val bundle = Bundle()
            bundle.putInt("index",i)
            val fragment = QuestionFragment()
            fragment.arguments = bundle
            Common.fragmentList.add(fragment)
        }
    }

    private fun genItems() {
        for(i in Common.questionList.indices)
            Common.myAnswerSheetList.add(CurrentQuestion(i, Common.ANSWER_TYPE.NO_ANSWER))
    }

    private fun countTimer() {
        countDownTimer = object:CountDownTimer(Common.TOTAL_TIME.toLong(), 1000){
            override fun onFinish() {
                finishGame()
            }

            override fun onTick(interval: Long) {
                txt_timer.text = (java.lang.String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(interval),
                    TimeUnit.MILLISECONDS.toSeconds(interval) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(interval))))
           time_play-=1000 }
        }

    }

    private fun finishGame() {
        TODO("Not yet implemented")
    }

    private fun genQuestion() {

        /* mQuestionsDB = RoomDBHelper.getAppDataBase(this)
        mQuestions = mQuestionsDB?.questionsDao()

        Common.questionList = mQuestions?.getAllQuestionsByCategory(Common.selectedCategory!!.ID) as MutableList<Questions> room implement*/

        Common.questionList = DBHelper.getInstance(this).getQuestionsByCategory(Common.selectedCategory!!.ID) as MutableList<Questions>

        if (Common.questionList.size==0){



            MaterialStyledDialog.Builder(this)
                .setTitle("Oops!")
                .setIcon(R.drawable.ic_menu_camera)
                .setDescription("Don't have any questions here ${Common.selectedCategory!!.name} category")
                .setPositiveText("Ok")
                .onPositive { finish()  }
                .show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.question, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("Not yet implemented")
    }

    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        } else
            super.onBackPressed()
    }
}