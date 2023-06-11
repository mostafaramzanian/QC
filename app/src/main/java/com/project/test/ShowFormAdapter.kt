package com.project.test

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter


// این کلاس یک adapter شخصی سازی شده برای viewPager میباشد

class ShowFormAdapter(
    private val size:Int,
 fragmentManager: FragmentManager,
 lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

 override fun getItemCount(): Int = size

    override fun createFragment(position: Int): Fragment {

        return when(position){

            0 -> FinalRegistrationFragment()
            1 -> InfoRegisterFragment()
            2 -> InformationFragment()
            3 -> DocumentFragment()
            4 -> HelpFragment()
            else -> HelpFragment()
        }


    }

}

