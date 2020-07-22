package com.victor_priesniakov.categoryquizapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.victor_priesniakov.categoryquizapp.Common.Common
import com.victor_priesniakov.categoryquizapp.R
import com.victor_priesniakov.categoryquizapp.model.CurrentQuestion
import kotlinx.android.synthetic.main.layout_grid_answer.view.*

class GridAnswerAdapter(internal var context:Context, internal var answerSheetList:List<CurrentQuestion>) :
    RecyclerView.Adapter<GridAnswerAdapter.MyViewHolder>() {




    inner class MyViewHolder(itemViev: View): RecyclerView.ViewHolder(itemViev){
        internal var question_item:View
        init {
            question_item = itemViev.findViewById(R.id.question_item) as View
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemViev = LayoutInflater.from(context).inflate(R.layout.layout_grid_answer, parent, false)
        return MyViewHolder(itemViev)
    }

    override fun getItemCount(): Int {
        return answerSheetList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (answerSheetList[position].type == Common.ANSWER_TYPE.RIGHT_ANSWER)
            holder.question_item.setBackgroundResource(R.drawable.grid_item_right_answer)
        else if (answerSheetList[position].type == Common.ANSWER_TYPE.WRONG_ANSWER)
            holder.question_item.setBackgroundResource(R.drawable.grid_item_wrong_answer)
        else
            holder.question_item.setBackgroundResource(R.drawable.grid_item_no_answer)
    }
}