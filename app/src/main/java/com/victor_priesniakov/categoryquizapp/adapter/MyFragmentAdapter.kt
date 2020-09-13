package com.victor_priesniakov.categoryquizapp.adapter

import android.content.Context
import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.victor_priesniakov.categoryquizapp.QuestionFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.StringBuilder

class MyFragmentAdapter (fa:FragmentActivity, var context: Context,
                        // var fragmentList:List<QuestionFragment>):FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
                         var fragmentList:List<QuestionFragment>): FragmentStateAdapter(fa) {

    override fun createFragment (position: Int): Fragment {  //getCount

         return   fragmentList[position]

    }


    //return  fragmentList[position]  }

    override fun getItemCount(): Int { //getItem
        return fragmentList.size
    }

   /* override fun getPageTitle(position: Int): CharSequence? {
        return StringBuilder("Question ").append(position+1).toString()
    }*/

    internal var instance:MyFragmentAdapter?=null
}