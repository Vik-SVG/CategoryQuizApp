package com.victor_priesniakov.categoryquizapp

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.victor_priesniakov.categoryquizapp.Common.Common
import com.victor_priesniakov.categoryquizapp.`interface`.IAnswerSelect
import com.victor_priesniakov.categoryquizapp.model.CurrentQuestion
import com.victor_priesniakov.categoryquizapp.model.Questions
import kotlinx.android.synthetic.main.fragment_question.*
import java.lang.Exception
import java.lang.StringBuilder


class QuestionFragment : Fragment(), IAnswerSelect {

    lateinit var mtxtTextQuestion:TextView
    lateinit var mCheckBoxA:CheckBox
    lateinit var mCheckBoxB:CheckBox
    lateinit var mCheckBoxC:CheckBox
    lateinit var mCheckBoxD:CheckBox

    lateinit var mLayoutImage:FrameLayout
    lateinit var mProgresBar:ProgressBar
    var mQuestion: Questions?=null
    var mQuestionIndex = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val itemView = inflater.inflate(R.layout.fragment_question, container, false)

        mQuestionIndex = requireArguments().getInt("index",-1) //edited: requireArg Instead of arguments
        mLayoutImage = itemView.findViewById(R.id.layout_image) as FrameLayout

        mQuestion = Common.questionList[mQuestionIndex]

        if (mQuestion!=null){
            mProgresBar = itemView.findViewById(R.id.progress_bar) as ProgressBar

            if (mQuestion!!.IsImageQuestion){
                val imageVIew = itemView.findViewById<View>(R.id.img_question) as ImageView

                Picasso.get().load(mQuestion!!.QuestionImage).into(imageVIew, object:Callback{
                    override fun onSuccess() {
                        mProgresBar.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                    img_question.setImageResource(R.drawable.ic_menu_slideshow)
                    }


                })
            } else
                mLayoutImage.visibility = View.GONE

            mtxtTextQuestion = itemView.findViewById(R.id.txt_question_text) as TextView
            mtxtTextQuestion.text = mQuestion!!.QuestionText

            mCheckBoxA = itemView.findViewById(R.id.ckb_a) as CheckBox
            mCheckBoxA.text = mQuestion!!.AnswerA

            mCheckBoxB = itemView.findViewById(R.id.ckb_b) as CheckBox
            mCheckBoxB.text = mQuestion!!.AnswerB

            mCheckBoxC = itemView.findViewById(R.id.ckb_C) as CheckBox
            mCheckBoxC.text = mQuestion!!.AnswerC

            mCheckBoxD = itemView.findViewById(R.id.ckb_d) as CheckBox
            mCheckBoxD.text = mQuestion!!.AnswerD


            mCheckBoxA.setOnCheckedChangeListener{
                compoundButton, b ->
                if(b)
                    Common.selectedValues.add(mCheckBoxA.text.toString())
                else
                    Common.selectedValues.remove(mCheckBoxA.text.toString())
            }

            mCheckBoxB.setOnCheckedChangeListener{
                    compoundButton, b ->
                if(b)
                    Common.selectedValues.add(mCheckBoxB.text.toString())
                else
                    Common.selectedValues.remove(mCheckBoxB.text.toString())
            }

            mCheckBoxC.setOnCheckedChangeListener{
                    compoundButton, b ->
                if(b)
                    Common.selectedValues.add(mCheckBoxC.text.toString())
                else
                    Common.selectedValues.remove(mCheckBoxC.text.toString())
            }

            mCheckBoxD.setOnCheckedChangeListener{
                    compoundButton, b ->
                if(b)
                    Common.selectedValues.add(mCheckBoxD.text.toString())
                else
                    Common.selectedValues.remove(mCheckBoxD.text.toString())
            }

        }

        return itemView
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            QuestionFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun selectedAnswer(): CurrentQuestion {
        Common.selectedValues.distinct()

        if(Common.myAnswerSheetList[mQuestionIndex].type == Common.ANSWER_TYPE.NO_ANSWER){
            val currentQuestion = CurrentQuestion(mQuestionIndex, Common.ANSWER_TYPE.NO_ANSWER)
            val result = StringBuilder()
            if(Common.selectedValues.size>1){
                val arrayAnswer = Common.selectedValues.toTypedArray()

                for(i in arrayAnswer!!.indices)
                    if(i<arrayAnswer.size-1)
                        result.append(StringBuilder((arrayAnswer!![i] as String ).substring(0,1)).append(","))
                else
                        result.append((arrayAnswer!![i] as String).substring(0,1))

            } else if(Common.selectedValues.size==1){
                val arrayAnswer = Common.selectedValues.toTypedArray()
                result.append((arrayAnswer!![0] as String).substring(0,1))
            }
            if (mQuestion!=null){
                if(!TextUtils.isEmpty(result)){
                    if(result.toString()== mQuestion!!.CorrectAnswer)
                        currentQuestion.type= Common.ANSWER_TYPE.RIGHT_ANSWER
                    else
                        currentQuestion.type = Common.ANSWER_TYPE.WRONG_ANSWER
                } else
                    currentQuestion.type = Common.ANSWER_TYPE.NO_ANSWER

            } else{
                Toast.makeText(activity, "Cannot get question", Toast.LENGTH_SHORT).show()
                currentQuestion.type = Common.ANSWER_TYPE.NO_ANSWER
            }
            Common.selectedValues.clear()
            return currentQuestion
        }
        else
            return Common.myAnswerSheetList[mQuestionIndex]
    }

    override fun showCorrectAnswer() {
        val correctAnswers = mQuestion!!.CorrectAnswer!!.split(",".toRegex())
            .dropLastWhile { it.isEmpty() }
        for(answer in correctAnswers){

            if(answer.equals("A")){
                mCheckBoxA.setTypeface(null, Typeface.BOLD)
                mCheckBoxA.setTextColor(Color.RED)

            } else if(answer.equals("B")) {
                mCheckBoxB.setTypeface(null, Typeface.BOLD)
                mCheckBoxB.setTextColor(Color.RED)

            }else if(answer.equals("C")) {
                mCheckBoxC.setTypeface(null, Typeface.BOLD)
                mCheckBoxC.setTextColor(Color.RED)

            }else if(answer.equals("D")) {
                mCheckBoxD.setTypeface(null, Typeface.BOLD)
                mCheckBoxD.setTextColor(Color.RED)
            }
        }
    }

    override fun disableAnswer() {
        mCheckBoxA.isEnabled = false
        mCheckBoxB.isEnabled = false
        mCheckBoxC.isEnabled = false
        mCheckBoxD.isEnabled = false
    }

    override fun resetQuestion() {
        mCheckBoxA.isEnabled = true
        mCheckBoxB.isEnabled = true
        mCheckBoxC.isEnabled = true
        mCheckBoxD.isEnabled = true

        mCheckBoxA.isChecked = false
        mCheckBoxB.isChecked = false
        mCheckBoxC.isChecked = false
        mCheckBoxD.isChecked = false

        mCheckBoxA.setTypeface(null, Typeface.NORMAL)
        mCheckBoxA.setTextColor(Color.BLACK)
        mCheckBoxB.setTypeface(null, Typeface.NORMAL)
        mCheckBoxB.setTextColor(Color.BLACK)
        mCheckBoxC.setTypeface(null, Typeface.NORMAL)
        mCheckBoxC.setTextColor(Color.BLACK)
        mCheckBoxD.setTypeface(null, Typeface.NORMAL)
        mCheckBoxD.setTextColor(Color.BLACK)

        Common.selectedValues.clear()
    }
}