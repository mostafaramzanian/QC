package com.project.test.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.project.test.view.fragment.OtherFormActiveFragment
import com.project.test.view.fragment.ShowFormReportFragment

class ShowMoreFormAdapter(
    private val size:Int,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = size

    override fun createFragment(position: Int): Fragment {

        return when(position){

            0 -> OtherFormActiveFragment()
            1 -> ShowFormReportFragment()
            else -> ShowFormReportFragment()
        }


    }

}