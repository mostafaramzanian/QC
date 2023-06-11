package com.project.test

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class FragmentReplacer(private val fragmentManager: FragmentManager) {
    fun replaceFragments(fragment1: Fragment, fragment2: Fragment,containerId: Int) {
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(containerId, fragment2)
        transaction.addToBackStack(null)
        transaction.commitAllowingStateLoss()
    }
}