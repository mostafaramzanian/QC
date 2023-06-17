package com.project.test.utils

import android.app.Activity
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.project.test.R

class NavigationApp(
    private val context: Activity,
    fragmentManager: FragmentManager,
    fragmentContainer: Int
) {
    private val navController =
        fragmentManager.findFragmentById(fragmentContainer)?.findNavController()

    fun navigationForward(destination: Int, label: String?) {
        val label1 = navController?.currentDestination?.label
        if (label1 == label && label == "HomeFragment") {
            navController?.navigate(R.id.action_home_menu_self)
        } else if (label1 == label && label == "InsertFromFragment") {
            navController?.navigate(R.id.action_insertFromFragment_self2)
        } else if (label1 == "InsertFromFragment" && label == "HomeFragment") {
            navController?.navigate(R.id.action_insertFromFragment_to_home_menu)
        } else {
                navController?.navigate(destination)
        }
    }
    fun navigationBackward() {
        if (navController?.currentDestination?.label != "InsertFromFragment" && navController?.currentDestination?.label != "HomeFragment") {
            navController?.navigateUp()
        }
    }

    fun emptyStack() {
        while (navController?.navigateUp() == true) {
        }
    }


}