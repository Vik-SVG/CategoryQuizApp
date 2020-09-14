package com.victor_priesniakov.categoryquizapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.navigation.ui.AppBarConfiguration
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.victor_priesniakov.categoryquizapp.Common.Common
import com.victor_priesniakov.categoryquizapp.Common.SpacesItemDescription
import com.victor_priesniakov.categoryquizapp.SQLhelper.QuestionsDao
import com.victor_priesniakov.categoryquizapp.SQLhelper.RoomDBHelper
import com.victor_priesniakov.categoryquizapp.adapter.GridAnswerAdapter
import com.victor_priesniakov.categoryquizapp.adapter.MyFragmentAdapter
import com.victor_priesniakov.categoryquizapp.adapter.QuestionListHelperAdapter
import com.victor_priesniakov.categoryquizapp.model.CurrentQuestion
import com.victor_priesniakov.categoryquizapp.model.Question
import kotlinx.android.synthetic.main.activity_question.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

class QuestionActivity : AppCompatActivity(){

    private var mQuestionsDB: RoomDBHelper? = null
    private var mQuestions: QuestionsDao? = null
    lateinit var countDownTimer: CountDownTimer
    var timePlay = Common.TOTAL_TIME
    lateinit var gridAnswerAdapter: GridAnswerAdapter
    lateinit var questionHelperAdapter:QuestionListHelperAdapter
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var myViewPager: ViewPager
    private lateinit var mGridAnswer: RecyclerView
    private var isAnswerModeView = false

    private lateinit var mTxtWrongAnswer:TextView

    internal var goToQuestionNum:BroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent!!.action!!.toString()==Common.KEY_GO_TO_QUESTION){
                val question = intent.getIntExtra(Common.KEY_GO_TO_QUESTION, -1)

                if (question != -1)
                    view_pager.currentItem = question

                drawer_layout.closeDrawer(GravityCompat.START) //start to left
            }
        }
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(goToQuestionNum)
      /*  if (!countDownTimer.equals(null)) //TODO:timer
            countDownTimer.cancel()*/
        if (!Common.fragmentList.equals(null))
            Common.fragmentList.clear()
        if (!Common.myAnswerSheetList.equals(null))
            Common.myAnswerSheetList.clear()

        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

       // val toolbar: Toolbar = findViewById(R.id.toolbar2)
      //  toolbar2.title = "Now Quiz!"
        setSupportActionBar(toolbar2)

        LocalBroadcastManager.getInstance(this).registerReceiver(goToQuestionNum, IntentFilter(Common.KEY_GO_TO_QUESTION))

        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar2, // toolbar
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()


        val recyclerHelperAnswerSheetList = nav_view.getHeaderView(0).findViewById<RecyclerView>(R.id.answer_sheet)
        recyclerHelperAnswerSheetList.setHasFixedSize(true)
        recyclerHelperAnswerSheetList.layoutManager = GridLayoutManager(this, 3)
        recyclerHelperAnswerSheetList.addItemDecoration(SpacesItemDescription(2))

        val mBtnDone = nav_view.getHeaderView(0).findViewById<Button>(R.id.btn_done)
        mBtnDone.setOnClickListener{
            if(!isAnswerModeView){
              val dil =  MaterialStyledDialog.Builder(this@QuestionActivity)
                    .setTitle("Finish?")
                    .setDescription("Really finish?")
                    .setIcon(R.drawable.ic_mood_white_24)
                    .setNegativeText("No")
                    .setPositiveText("Yes")
                    .setCancelable(true)
                    .onPositive {  finishGame()
                        drawer_layout.closeDrawer(GravityCompat.START) }
                    .onNegative{   }
                  .show()
            } else{
                finishGame()
            }

            }



       // nav_view.setNavigationItemSelectedListener(this) // if extending ,NavigationView.OnNavigationItemSelectedListener

        /*val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.app_bar_scroling_view)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)*/


        genQuestion()



        if (Common.questionList.size > 0) {
            txt_timer.visibility = View.VISIBLE
            txt_right_answer.visibility = View.VISIBLE

            countTimer()

            genItems()

            mGridAnswer = findViewById(R.id.grid_answer)
            mGridAnswer.setHasFixedSize(true)


            if (Common.questionList.size > 0)
                mGridAnswer.layoutManager = GridLayoutManager(
                    this,
                    if (Common.questionList.size > 10)
                        Common.questionList.size / 2
                    else Common.questionList.size
                )

            gridAnswerAdapter = GridAnswerAdapter(this, Common.myAnswerSheetList)
           // questionHelperAdapter = QuestionListHelperAdapter(this, Common.myAnswerSheetList)
            mGridAnswer.adapter = gridAnswerAdapter



         //  runBlocking {
               genFragmentList()
         //  }


               val fragmentAdapter =
                   MyFragmentAdapter(this,this, Common.fragmentList)

               view_pager.adapter = fragmentAdapter

               TabLayoutMediator(sliding_tabs, view_pager) { tab, position ->
                   tab.text = "Question ${position + 1}"
                   view_pager.setCurrentItem(tab.position, true)
               }.attach()



           // sliding_tabs.setupWithViewPager(view_pager)

            view_pager.offscreenPageLimit = Common.questionList.size

            /* myViewPager = findViewById(R.id.view_pager
             myViewPager.offscreenPageLimit = Common.questionList.size
             myViewPager.adapter = fragmentAdapter
              sliding_tabs.setupWithViewPager(myViewPager)*/

            view_pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){

                val SCROLLING_RIGHT = 0
                val SCROLLING_LEFT = 1
                val SCROLLING_UNDETERMINED = 2

                var currentScrollDirection = SCROLLING_UNDETERMINED

                private val isScrollDirectionUndtermined:Boolean
                    get() = currentScrollDirection == SCROLLING_UNDETERMINED

                private val isScrollDirectionRight:Boolean
                    get() = currentScrollDirection == SCROLLING_RIGHT

                private val isScrollDirectionLeft:Boolean
                    get() = currentScrollDirection == SCROLLING_LEFT

                private fun setScrollDirection(positionOffSet:Float){

                    if (1-positionOffSet>=0.5)
                        this.currentScrollDirection = SCROLLING_RIGHT
                    else if (1-positionOffSet<=0.5)
                        this.currentScrollDirection = SCROLLING_LEFT

                }


                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    setScrollDirection(positionOffset)
                }

                override fun onPageSelected(p0: Int) {
                    val questionFragment:QuestionFragment
                    var position = 0
                    if (p0>0){
                        if (isScrollDirectionRight){
                            questionFragment = Common.fragmentList[p0-1]
                            position = p0-1
                        } else if (isScrollDirectionLeft){
                            questionFragment = Common.fragmentList[p0+1]
                            position = p0+1
                        } else{
                            questionFragment = Common.fragmentList[p0]
                        }
                    }else{
                        questionFragment = Common.fragmentList[0]
                        position = 0
                    }




                    if (Common.myAnswerSheetList[position].type == Common.ANSWER_TYPE.NO_ANSWER){
                        val question_state = questionFragment.selectedAnswer()

                        Common.myAnswerSheetList[position] = question_state
                        gridAnswerAdapter.notifyDataSetChanged()
                        questionHelperAdapter.notifyDataSetChanged()
                        countCorrectAnswer()

                        txt_right_answer.text = "${Common.right_answer_count} / ${Common.questionList.size}"


                        if (Common.wrong_answer_count>0) {
                            mTxtWrongAnswer.text = "${Common.wrong_answer_count}"
                        }

                        if (question_state.type != Common.ANSWER_TYPE.NO_ANSWER) {
                            questionFragment.showCorrectAnswer() //TODO: Ответы
                            questionFragment.disableAnswer()
                        }

                    }
                }

                override fun onPageScrollStateChanged(state: Int) {
                    if (state == ViewPager.SCROLL_STATE_IDLE)
                        this.currentScrollDirection == SCROLLING_UNDETERMINED
                }
            })

            txt_right_answer.text = (Common.right_answer_count/Common.questionList.size).toString()
            questionHelperAdapter = QuestionListHelperAdapter(this, Common.myAnswerSheetList)
            recyclerHelperAnswerSheetList.adapter = questionHelperAdapter

        }
    }

    private fun countCorrectAnswer() {
        Common.right_answer_count = 0
        Common.wrong_answer_count = 0

        for (item in Common.myAnswerSheetList)
            if (item.type == Common.ANSWER_TYPE.RIGHT_ANSWER)
                Common.right_answer_count++
        else if (item.type == Common.ANSWER_TYPE.WRONG_ANSWER)
                Common.wrong_answer_count++

    }

   private fun genFragmentList() {

        if (!Common.fragmentList.equals(null))
            Common.fragmentList.clear()

        for (i in Common.questionList.indices) {
            val bundle = Bundle()
            bundle.putInt("index", i)
            val fragment = QuestionFragment()
            fragment.arguments = bundle
            Common.fragmentList.add(fragment)
        }
    }

    private fun genItems() {

        if (!Common.myAnswerSheetList.equals(null))
            Common.myAnswerSheetList.clear()


        for (i in Common.questionList.indices)
            Common.myAnswerSheetList.add(CurrentQuestion(i, Common.ANSWER_TYPE.NO_ANSWER))
    }


    private fun countTimer() {
        countDownTimer = object : CountDownTimer(Common.TOTAL_TIME.toLong(), 1000) {
            override fun onFinish() {
                finishGame()
            }

            override fun onTick(interval: Long) {
                txt_timer.text = (java.lang.String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(interval),
                    TimeUnit.MILLISECONDS.toSeconds(interval) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(
                            interval
                        )
                    )
                ))
                timePlay -= 1000
            }
        }

    }

    private fun finishGame() {

        val position = view_pager.currentItem
        val questionFragment = Common.fragmentList[position]
        //  val answerFragment = Common.fragmentList[position-2]

        val questionState = questionFragment.selectedAnswer()
        Common.myAnswerSheetList[position] = questionState
        gridAnswerAdapter.notifyDataSetChanged()
        questionHelperAdapter.notifyDataSetChanged()
        countCorrectAnswer()

        txt_right_answer.text = (Common.right_answer_count / Common.questionList.size).toString() //might not work
        mTxtWrongAnswer.text = "${Common.wrong_answer_count}"

        if (questionState.type != Common.ANSWER_TYPE.NO_ANSWER){
            questionFragment.showCorrectAnswer()
            questionFragment.disableAnswer()
        }

    }

    private fun genQuestion() {

        var mQuestionsDB2 = RoomDBHelper.getAppDataBase(this)
        var mQuestions2 = mQuestionsDB2?.questionsDao()
        if (!Common.questionList.equals(null)){
            Common.questionList.clear()
        }

      //  Common.questionList = mQuestions2?.getAllQuestionsByCategory(Common.selectedCategory!!.ID) as MutableList<Question> //room implement

       /* Common.questionList = DBHelper.getInstance(this)
            .getQuestionsByCategory(Common.selectedCategory!!.ID) as MutableList<Question>*/


            Common.questionList = runBlocking {
            val job = async {
                mQuestions2?.getAllQuestionsByCategory(Common.selectedCategory!!.ID) as MutableList<Question>
            }
            job.await()
            }

         if (Common.questionList.size == 0) {

             MaterialStyledDialog.Builder(this)
                 .setTitle("Oops!")
                 .setIcon(R.drawable.ic_menu_camera)
                 .setDescription("Don't have any questions here in ${Common.selectedCategory!!.name} category")
                 .setPositiveText("Ok")
                 .onPositive { finish()}
                 .show()

        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.menu_done->{
                if(!isAnswerModeView){
                    val dil =  MaterialStyledDialog.Builder(this@QuestionActivity)
                        .setTitle("Finish?")
                        .setDescription("Really finish?")
                        .setIcon(R.drawable.ic_mood_white_24)
                        .setNegativeText("No")
                        .setPositiveText("Yes")
                        .setCancelable(true)
                        .onPositive {  finishGame()
                            drawer_layout.closeDrawer(GravityCompat.START) }
                        .onNegative{   }
                        .show()
                } else{
                    finishGame()
                }
            }
        }

        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.question, menu)
        val  mItem:MenuItem = menu!!.findItem(R.id.menu_wrong_answer)

        val layout = mItem.actionView as ConstraintLayout
        mTxtWrongAnswer = layout.findViewById(R.id.txt_wrong_answer) as TextView

        mTxtWrongAnswer.setOnClickListener{
            Toast.makeText(this, "${Common.wrong_answer_count} wrong answers", Toast.LENGTH_SHORT).show()
        }

        return true
    }

    /*override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // if using extening
    }*/

    override fun onBackPressed() {

        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else
            super.onBackPressed()
    }
}