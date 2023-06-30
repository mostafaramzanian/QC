package com.project.test.utils

import android.app.Activity
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController

class NavigationApp(
    private val context: Activity,
    fragmentManager: FragmentManager,
    fragmentContainer: Int
) {
    private val navController =
        fragmentManager.findFragmentById(fragmentContainer)?.findNavController()

    fun navigationForward(label: String?) {
        val destination1 = navController?.graph?.find { it.label == label }
        if (destination1 != null) {
            navController?.navigate(destination1.id)
        }
    }
    fun navigationBackward() {
        navController?.navigateUp()
    }

    private fun emptyStack() {
        while (navController?.navigateUp() == true) {
        }
    }


}