package com.victor_priesniakov.categoryquizapp

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog
import com.victor_priesniakov.categoryquizapp.Common.Common
import com.victor_priesniakov.categoryquizapp.Common.SpaceItemDecoration
import com.victor_priesniakov.categoryquizapp.adapter.ResultGridAdapter
import com.victor_priesniakov.categoryquizapp.databinding.ActivityResultBinding

import java.util.concurrent.TimeUnit

class ResultActivity : AppCompatActivity() {

    internal var backToQuestion:BroadcastReceiver = object:BroadcastReceiver(){

        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent!!.action!!.toString() == Common.KEY_BACK_FROM_RESULT){
                val questionIndex = intent.getIntExtra(Common.KEY_BACK_FROM_RESULT, -1)
                goBackActivityWithQuestionIndex(questionIndex)
            }
        }

    }

    private fun goBackActivityWithQuestionIndex(questionIndex: Int) {
        val returnIntent = Intent()
        returnIntent.putExtra(Common.KEY_BACK_FROM_RESULT, questionIndex)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this@ResultActivity)
            .unregisterReceiver(backToQuestion)
        super.onDestroy()
    }

    private lateinit var bdn: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bdn = ActivityResultBinding.inflate(layoutInflater)
        val view = bdn.root
        setContentView(view)

        LocalBroadcastManager.getInstance(this@ResultActivity)
            .registerReceiver(backToQuestion, IntentFilter(Common.KEY_BACK_FROM_RESULT))

       // LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(Common.KEY_GO_TO_QUESTION).putExtra(Common.KEY_GO_TO_QUESTION,7 ))


        setSupportActionBar(bdn.toolbar3)
      //  bdn.toolbar3.setNavigationIcon(R.drawable.ic_mood_white_24)
      //  bdn.toolbar3.navigationIcon?.setColorFilter(resources.getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP)

        //supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_clear_white__24)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)


        bdn.txtTime.text = (String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(Common.timer.toLong()),
            TimeUnit.MILLISECONDS.toSeconds(Common.timer.toLong()) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(
                    Common.timer.toLong()
                )
            )
        ))

        bdn.txtRightAnswerResult.text = Common.right_answer_count.toString() + "/" + Common.questionList.size.toString()

        bdn.btnFilterTotal.text = Common.questionList.size.toString()
        bdn.btnFilterRight.text = Common.right_answer_count.toString()
        bdn.btnFilterWrong.text = Common.wrong_answer_count.toString()
        bdn.btnFilterNoAnswer.text = Common.no_answer_count.toString()

        val percent = Common.right_answer_count * 100 / Common.questionList.size

        if(percent > 80 )
            bdn.txtResult.text = "Exelent"
        else if(percent>70)
            bdn.txtResult.text = "Good"
        else if(percent>60)
            bdn.txtResult.text = "Fair"
        else if(percent>50)
            bdn.txtResult.text = "Bad"
        else
            bdn.txtResult.text = "Fail"


        var mQuestionResultAdapter = ResultGridAdapter(this, Common.myAnswerSheetList)
        bdn.recyclerResult.adapter = mQuestionResultAdapter


        bdn.btnFilterTotal.setOnClickListener{
             mQuestionResultAdapter = ResultGridAdapter(this, Common.myAnswerSheetList)
            bdn.recyclerResult.adapter = mQuestionResultAdapter
        }

        bdn.btnFilterNoAnswer.setOnClickListener{
            Common.myAnswerSheetListFiltered.clear()

            for(currentQuestion in Common.myAnswerSheetList) {
                if (currentQuestion.type == Common.ANSWER_TYPE.NO_ANSWER)
                    Common.myAnswerSheetListFiltered.add(currentQuestion)
            }
                 mQuestionResultAdapter = ResultGridAdapter(this, Common.myAnswerSheetListFiltered)
                bdn.recyclerResult.adapter = mQuestionResultAdapter

        }

        bdn.btnFilterWrong.setOnClickListener{
            Common.myAnswerSheetListFiltered.clear()

            for(currentQuestion in Common.myAnswerSheetList) {
                if (currentQuestion.type == Common.ANSWER_TYPE.WRONG_ANSWER)
                    Common.myAnswerSheetListFiltered.add(currentQuestion)
            }

                mQuestionResultAdapter = ResultGridAdapter(this, Common.myAnswerSheetListFiltered)
                bdn.recyclerResult.adapter = mQuestionResultAdapter
        }

        bdn.btnFilterRight.setOnClickListener{
            Common.myAnswerSheetListFiltered.clear()

            for(currentQuestion in Common.myAnswerSheetList) {
                if (currentQuestion.type == Common.ANSWER_TYPE.RIGHT_ANSWER)
                    Common.myAnswerSheetListFiltered.add(currentQuestion)
            }

             mQuestionResultAdapter = ResultGridAdapter(this, Common.myAnswerSheetListFiltered)
            bdn.recyclerResult.adapter = mQuestionResultAdapter
        }


        bdn.recyclerResult.setHasFixedSize(true)
        bdn.recyclerResult.layoutManager = GridLayoutManager(this, 4)
        bdn.recyclerResult.addItemDecoration(SpaceItemDecoration(4))



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_result, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.menu_do_quiz_again -> doQuizAgain()
            R.id.menu_view_answer -> viewAnswer()
            android.R.id.home -> {
                val intent = Intent(applicationContext, QuizActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }

        }
        return true
    }

    private fun viewAnswer() {
        val returnIntent = Intent()
        returnIntent.putExtra("action", "view_answer")
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    private fun doQuizAgain() {
        val dil =  MaterialStyledDialog.Builder(this@ResultActivity)
            .setTitle("Do quiz again?")
            .setDescription("Do you really want to delete this run?")
            .setIcon(R.drawable.ic_mood_white_24)
            .setNegativeText("No")
            .setPositiveText("Yes")
            .setCancelable(true)
            .onPositive {
                val returnIntent = Intent()
                returnIntent.putExtra("action", "do_quiz_again")
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
            .onNegative{   }
            .show()
    }
}