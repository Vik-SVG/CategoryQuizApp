package com.victor_priesniakov.categoryquizapp

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog
import com.google.android.material.tabs.TabLayout
import com.victor_priesniakov.categoryquizapp.Common.Common
import com.victor_priesniakov.categoryquizapp.SQLhelper.QuestionsDao
import com.victor_priesniakov.categoryquizapp.SQLhelper.RoomDBHelper
import com.victor_priesniakov.categoryquizapp.adapter.GridAnswerAdapter
import com.victor_priesniakov.categoryquizapp.adapter.MyFragmentAdapter
import com.victor_priesniakov.categoryquizapp.model.CurrentQuestion
import com.victor_priesniakov.categoryquizapp.model.Question
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit


class QuestionMainFragment : Fragment() {

    private var mQuestionsDB: RoomDBHelper? = null
    private var mQuestions: QuestionsDao? = null
    lateinit var countDownTimer: CountDownTimer
    var time_play = Common.TOTAL_TIME
    lateinit var mGridAdapter: GridAnswerAdapter
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var mGridAnswerRecyclerView: RecyclerView
    private lateinit var txtTimer:TextView
    private lateinit var mRight_answer_txt:TextView
    private lateinit var mViewPager:ViewPager
    private lateinit var mSlidingTabs:TabLayout
    private lateinit var mToolbar2: Toolbar
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var mTxt_wrong_answer:TextView

    private var mQuestionsDB2: RoomDBHelper? = null
    private var mQuestions2:QuestionsDao? = null
    lateinit var questionFragment:QuestionFragment
    lateinit var answerFragment: QuestionFragment
    lateinit var fragmentAdapter:MyFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var mainView = inflater.inflate(R.layout.activity_question, container, false)
        /*txtTimer = mainView.findViewById<TextView>(R.id.txt_timer)
        mRight_answer_txt = mainView.findViewById<TextView>(R.id.txt_right_answer)
        mViewPager = mainView.findViewById<ViewPager>(R.id.view_pager)
        mSlidingTabs = mainView.findViewById<TabLayout>(R.id.sliding_tabs)
        mDrawerLayout = mainView.findViewById<DrawerLayout>(R.id.drawer_layout)
        mToolbar2 = mainView.findViewById<Toolbar>(R.id.toolbar2)*/



        /*mToolbar2.title = "Now Quiz!"
        //mainView.setSupportActionBar(mToolbar2)
        (activity as AppCompatActivity?)!!.setSupportActionBar(mToolbar2)


        val toggle = ActionBarDrawerToggle(
            activity,
            mDrawerLayout,
            mToolbar2, // toolbar
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        mDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()*/



        setHasOptionsMenu(true)


        genQuestion()

        if (Common.questionList.size > 0) {

            countTimer()

            genItems()


            genFragmentList()
        }


        /*genQuestion()

        if (Common.questionList.size > 0) {
            txtTimer.visibility = View.VISIBLE
            mRight_answer_txt.visibility = View.VISIBLE

            countTimer()

            genItems()*/





        /* mGridAnswerRecyclerView = mainView.findViewById(R.id.grid_answer)
         mGridAnswerRecyclerView.setHasFixedSize(true)
         //grid_answer.setHasFixedSize(true)

         if (Common.questionList.size > 0)
         // grid_answer.layoutManager = GridLayoutManager(
             mGridAnswerRecyclerView.layoutManager = GridLayoutManager(
                 context as Context,
                 if (Common.questionList.size > 10)
                     Common.questionList.size / 2
                 else Common.questionList.size
             )

         mGridAdapter = GridAnswerAdapter(context as Context, Common.myAnswerSheetList)
         //grid_answer.adapter = adapter
         mGridAnswerRecyclerView.adapter = mGridAdapter*/







        /*genFragmentList()

         fragmentAdapter =
            MyFragmentAdapter(
                activity?.supportFragmentManager as FragmentManager,
                context as Context,
                Common.fragmentList
            )

        //  view_pager.offscreenPageLimit = Common.questionList.size
        mViewPager.adapter = fragmentAdapter
        mSlidingTabs.setupWithViewPager(mViewPager)
        mViewPager.offscreenPageLimit = 30  //TODO: лимит страниц

        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{ //TODO: review

            val SCROLLING_RIGHT = 0
            val SCROLLING_LEFT = 1
            val SCROLLING_UNDETERMINED = 2

            var currentScrollDirection = SCROLLING_UNDETERMINED

            private val isScrollDirectionUndetermined:Boolean
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

                 var position=p0

                if (p0>0){
                    if (isScrollDirectionRight){
                             questionFragment = Common.fragmentList[p0]
                            position = p0
                        answerFragment = Common.fragmentList[p0-1]


                    } else if (isScrollDirectionLeft){
                        questionFragment = Common.fragmentList[p0]// deleted
                        position = p0
                        answerFragment = Common.fragmentList[p0+1]


                    } else{
                        questionFragment = Common.fragmentList[p0]
                        answerFragment = Common.fragmentList[p0]
                    }

                }else{
                    questionFragment = Common.fragmentList[0]
                    position = 0
                    answerFragment = Common.fragmentList[0]

                }

                if (position==Common.myAnswerSheetList.size){
                    position == Common.myAnswerSheetList.size -1
                }

                if (Common.myAnswerSheetList[position].type == Common.ANSWER_TYPE.NO_ANSWER){

                    val questionState = questionFragment.selectedAnswer() //TODO: проблема с ответами не сохр

                    if (position!=0){
                    Common.myAnswerSheetList[position-1] = questionState //TODO;сетка с ответами
                    } else{
                        Common.myAnswerSheetList[position] = questionState //
                    }

                    mGridAdapter.notifyDataSetChanged()
                    fragmentAdapter.notifyDataSetChanged()//added
                    countCorrectAnswer()

                    mRight_answer_txt.text = "${Common.right_answer_count} / ${Common.questionList.size}" //might not work
                    mTxt_wrong_answer.text = "${Common.wrong_answer_count}"

                    if (questionState.type != Common.ANSWER_TYPE.NO_ANSWER){
                        answerFragment.showCorrectAnswer() //TODO: Ответы
                        answerFragment.disableAnswer()
                    }

                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager.SCROLL_STATE_IDLE)
                    this.currentScrollDirection == SCROLLING_UNDETERMINED
            }
        })*/

        //  }





        return mainView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewPager = view.findViewById<ViewPager>(R.id.view_pager)

        txtTimer = view.findViewById<TextView>(R.id.txt_timer)
        mRight_answer_txt = view.findViewById<TextView>(R.id.txt_right_answer)
        mViewPager = view.findViewById<ViewPager>(R.id.view_pager)
        mSlidingTabs = view.findViewById<TabLayout>(R.id.sliding_tabs)
        mDrawerLayout = view.findViewById<DrawerLayout>(R.id.drawer_layout)
        mToolbar2 = view.findViewById<Toolbar>(R.id.toolbar2)

        mGridAnswerRecyclerView = view.findViewById(R.id.grid_answer)
        mGridAnswerRecyclerView.setHasFixedSize(true)
        //grid_answer.setHasFixedSize(true)



        mToolbar2.title = "Now Quiz!"
        //mainView.setSupportActionBar(mToolbar2)
        (activity as AppCompatActivity?)!!.setSupportActionBar(mToolbar2)


        val toggle = ActionBarDrawerToggle(
            activity,
            mDrawerLayout,
            mToolbar2, // toolbar
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        mDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()



        if (Common.questionList.size > 0)
        // grid_answer.layoutManager = GridLayoutManager(
            mGridAnswerRecyclerView.layoutManager = GridLayoutManager(
                context as Context,
                if (Common.questionList.size > 10)
                    Common.questionList.size / 2
                else Common.questionList.size
            )

        mGridAdapter = GridAnswerAdapter(context as Context, Common.myAnswerSheetList)
        //grid_answer.adapter = adapter
        mGridAnswerRecyclerView.adapter = mGridAdapter

        // genQuestion()

        if (Common.questionList.size > 0) {
            txtTimer.visibility = View.VISIBLE
            mRight_answer_txt.visibility = View.VISIBLE

            /* countTimer()

             genItems()


             genFragmentList()*/

            /*  fragmentAdapter =
                  MyFragmentAdapter(
                      activity?.supportFragmentManager as FragmentManager,
                      context as Context,
                      Common.fragmentList
                  )*/

            //  view_pager.offscreenPageLimit = Common.questionList.size
            //   mViewPager.adapter = fragmentAdapter
            mSlidingTabs.setupWithViewPager(mViewPager)
//            mViewPager.offscreenPageLimit = 30  //TODO: лимит страниц

            mViewPager.addOnPageChangeListener(object :
                ViewPager.OnPageChangeListener { //TODO: review

                val SCROLLING_RIGHT = 0
                val SCROLLING_LEFT = 1
                val SCROLLING_UNDETERMINED = 2

                var currentScrollDirection = SCROLLING_UNDETERMINED

                private val isScrollDirectionUndetermined: Boolean
                    get() = currentScrollDirection == SCROLLING_UNDETERMINED

                private val isScrollDirectionRight: Boolean
                    get() = currentScrollDirection == SCROLLING_RIGHT

                private val isScrollDirectionLeft: Boolean
                    get() = currentScrollDirection == SCROLLING_LEFT

                private fun setScrollDirection(positionOffSet: Float) {

                    if (1 - positionOffSet >= 0.5)
                        this.currentScrollDirection = SCROLLING_RIGHT
                    else if (1 - positionOffSet <= 0.5)
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

                    var position = p0

                    if (p0 > 0) {
                        if (isScrollDirectionRight) {
                            questionFragment = Common.fragmentList[p0]
                            position = p0
                            answerFragment = Common.fragmentList[p0 - 1]


                        } else if (isScrollDirectionLeft) {
                            questionFragment = Common.fragmentList[p0]// deleted
                            position = p0
                            answerFragment = Common.fragmentList[p0 + 1]
                        } else {
                            questionFragment = Common.fragmentList[p0]
                            answerFragment = Common.fragmentList[p0]
                        }

                    } else {
                        questionFragment = Common.fragmentList[0]
                        position = 0
                        answerFragment = Common.fragmentList[0]

                    }

                    if (position == Common.myAnswerSheetList.size) {
                        position == Common.myAnswerSheetList.size - 1
                    }

                    if (Common.myAnswerSheetList[position].type == Common.ANSWER_TYPE.NO_ANSWER) {

                        val questionState = questionFragment.selectedAnswer() //TODO: проблема с ответами не сохр

                        if (position != 0) {
                            Common.myAnswerSheetList[position - 1] =
                                questionState //TODO;сетка с ответами
                        } else {
                            Common.myAnswerSheetList[position] = questionState //
                        }

                        mGridAdapter.notifyDataSetChanged()
                        fragmentAdapter.notifyDataSetChanged()//added
                        countCorrectAnswer()

                        mRight_answer_txt.text = "${Common.right_answer_count} / ${Common.questionList.size}" //might not work
                        mTxt_wrong_answer.text = "${Common.wrong_answer_count}"

                        if (questionState.type != Common.ANSWER_TYPE.NO_ANSWER) {
                            answerFragment.showCorrectAnswer() //TODO: Ответы
                            answerFragment.disableAnswer()
                        }

                    }
                }

                override fun onPageScrollStateChanged(state: Int) {
                    if (state == ViewPager.SCROLL_STATE_IDLE)
                        this.currentScrollDirection == SCROLLING_UNDETERMINED
                }
            })

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
                txtTimer.text = (java.lang.String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(interval),
                    TimeUnit.MILLISECONDS.toSeconds(interval) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(
                            interval
                        )
                    )
                ))
                time_play -= 1000
            }
        }

    }

    private fun finishGame() {
        val position = mViewPager.currentItem
        val questionFragment = Common.fragmentList[position]
        //  val answerFragment = Common.fragmentList[position-2]

        val questionState = questionFragment.selectedAnswer()
        Common.myAnswerSheetList[position] = questionState
        mGridAdapter.notifyDataSetChanged()
        countCorrectAnswer()

        mRight_answer_txt.text = "${Common.right_answer_count} / ${Common.questionList.size}" //might not work
        mTxt_wrong_answer.text = "${Common.wrong_answer_count}"

        if (questionState.type != Common.ANSWER_TYPE.NO_ANSWER){
            questionFragment.showCorrectAnswer()
            questionFragment.disableAnswer()
        }    }



    private fun genQuestion() {


        if (mQuestionsDB2==null)
            mQuestionsDB2 = RoomDBHelper.getAppDataBase(context as Context)

        if (mQuestions2==null)
            mQuestions2 = mQuestionsDB2?.questionsDao()

        if (!Common.questionList.equals(null)){
            Common.questionList.clear()
            Common.questionList = ArrayList()
        }


        Common.questionList = runBlocking {
            mQuestions2?.getAllQuestionsByCategory(Common.selectedCategory!!.ID) as MutableList<Question> }

        /* val job = async {
             mQuestions2?.getAllQuestionsByCategory(Common.selectedCategory!!.ID) as MutableList<Question> }
         job.await()*/
        //}  //room implement



        /* Common.questionList = DBHelper.getInstance(this)
             .getQuestionsByCategory(Common.selectedCategory!!.ID) as MutableList<Question>*/

        if (Common.questionList.size == 0) {

            MaterialStyledDialog.Builder(context as Context)
                .setTitle("Oops!")
                .setIcon(R.drawable.ic_menu_camera)
                .setDescription("Don't have any questions here in ${Common.selectedCategory!!.name} category")
                .setPositiveText("Ok")
                .onPositive { activity?.finish() }
                .show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        //super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.question, menu)


        /* val mItem = menu.findItem(R.id.menu_wrong_answer)
         val layout  = mItem.actionView*/
        //        val layout:ConstraintLayout = mItem.actionView as ConstraintLayout

        return  super.onCreateOptionsMenu(menu, inflater)


        //  return super.onCreateOptionsMenu(menu, inflater)
        // return inflater.inflate(R.menu.question, menu)
        //val mItem = menu.findItem(R.id.txt_right_answer)
        //  mItem = menu.findItem(R.id.menu_wrong_answer)

    }

//TODO:onbackpressed implementation to close drawer layout

    override fun onPrepareOptionsMenu(menu: Menu) {

        val  mItem = menu.findItem(R.id.menu_wrong_answer)
        //     mItem.isVisible = false

        val layout = mItem.actionView as ConstraintLayout

        mTxt_wrong_answer = layout.findViewById(R.id.txt_wrong_answer) as TextView
        mTxt_wrong_answer.text = 0.toString()
        return super.onPrepareOptionsMenu(menu)

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            QuestionMainFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}