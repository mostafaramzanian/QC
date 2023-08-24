package com.project.test.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.project.test.view.fragment.report_page.sections.ReportInstructionsFragment
import com.project.test.view.fragment.report_page.sections.ReportFinalStepFragment
import com.project.test.view.fragment.report_page.sections.ReportHelpFragment
import com.project.test.view.fragment.report_page.sections.ReportSavedItemsFragment
import com.project.test.view.fragment.report_page.sections.ReportParametersFragment


// این کلاس یک adapter شخصی سازی شده برای viewPager میباشد

class ShowFormAdapter(
    private val size:Int,
 fragmentManager: FragmentManager,
 lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

 override fun getItemCount(): Int = size

    override fun createFragment(position: Int): Fragment {

        return when(position){

            0 -> ReportFinalStepFragment()
            1 -> ReportSavedItemsFragment()
            2 -> ReportParametersFragment()
            3 -> ReportInstructionsFragment()
            4 -> ReportHelpFragment()
            else -> ReportHelpFragment()
        }


    }

}

