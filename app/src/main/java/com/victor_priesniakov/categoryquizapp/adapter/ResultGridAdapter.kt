package com.victor_priesniakov.categoryquizapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.victor_priesniakov.categoryquizapp.Common.Common
import com.victor_priesniakov.categoryquizapp.R
import com.victor_priesniakov.categoryquizapp.model.CurrentQuestion
import java.lang.StringBuilder

class ResultGridAdapter (internal var context:Context,
                         internal var answerSheetList:List<CurrentQuestion>):
    RecyclerView.Adapter<ResultGridAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.layout_result_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mButtonQuestionNumber.text = StringBuilder("Question ").append(answerSheetList[position].questionIndex+1)

        if(answerSheetList[position].type == Common.ANSWER_TYPE.RIGHT_ANSWER) {
            holder.mButtonQuestionNumber.setBackgroundResource(R.drawable.grid_item_right_answer)
            val img = context.resources.getDrawable(R.drawable.ic_check_white_24dp)
            holder.mButtonQuestionNumber.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, img)

        } else if(answerSheetList[position].type == Common.ANSWER_TYPE.WRONG_ANSWER) {
            holder.mButtonQuestionNumber.setBackgroundResource(R.drawable.grid_item_wrong_answer)
            val img = context.resources.getDrawable(R.drawable.ic_baseline_clear_white__24)
            holder.mButtonQuestionNumber.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, img)
        } else{
                holder.mButtonQuestionNumber.setBackgroundResource(R.drawable.grid_item_no_answer)
                val img = context.resources.getDrawable(R.drawable.ic_baseline_error_outline_white__24)
                holder.mButtonQuestionNumber.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, img)

        }
    }

    override fun getItemCount(): Int {
        return answerSheetList.size
    }

    inner class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        internal var mButtonQuestionNumber : Button
        init {
            mButtonQuestionNumber = itemView.findViewById(R.id.btn_question) as Button
            mButtonQuestionNumber.setOnClickListener{
                LocalBroadcastManager.getInstance(context)
                    .sendBroadcast(Intent(Common.KEY_BACK_FROM_RESULT)
                        .putExtra(Common.KEY_BACK_FROM_RESULT, answerSheetList[adapterPosition].questionIndex))
            }
        }

    }

}