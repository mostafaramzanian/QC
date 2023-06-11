package com.project.test


import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import java.sql.Types.NULL


class Stack() {
    companion object {
        private val fragmentList = ArrayList<Fragment>()
    }


    fun reset() {
        fragmentList.clear()
    }

    fun push(activity: Activity, containerId: Int) {
        val currentFragment =
            (activity as AppCompatActivity).supportFragmentManager.findFragmentById(containerId)

        if (currentFragment != null && !fragmentList.contains(currentFragment)) {
            fragmentList.add(currentFragment)
        }
    }

    fun getData(index: Int): String {

        return fragmentList[index].javaClass.simpleName
    }

    fun pop(fragmentManager: FragmentManager, containerId: Int): Int {
        if (fragmentList.size >= 2) {
            FragmentReplacer(fragmentManager).replaceFragments(
                fragmentList[fragmentList.size - 1],
                fragmentList[fragmentList.size - 2],
                containerId
            )
            fragmentList.removeAt(fragmentList.size - 1)
        }
        return fragmentList.size
    }

    fun getLastFragment():Fragment{
        var fragment:Fragment=HomeFragment()
        if (fragmentList.size>0) {
            fragment= fragmentList[fragmentList.size - 1]
        }
        return fragment
    }
    fun size(): Int {
        return fragmentList.size
    }
}
