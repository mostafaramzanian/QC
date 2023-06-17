package com.project.test.utils

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.project.test.R
import com.project.test.view.fragment.HomeFragment

class NavigationApp(private val context:Activity,private val fragmentManager: FragmentManager,private val fragmentContainer: Int) {
   fun navigationForward(destination: Int,label:String?){
       val navController = fragmentManager.findFragmentById(fragmentContainer)?.findNavController()
       val label1= navController?.currentDestination?.label
       if(label1==label && label=="HomeFragment"){
          navController?.navigate(R.id.action_home_menu_self)
       }else if(label1==label && label=="InsertFromFragment"){
           navController?.navigate(R.id.action_insertFromFragment_self2)
       }else if(label1=="InsertFromFragment" && label=="HomeFragment"){
           navController.navigate(R.id.action_insertFromFragment_to_home_menu)
       }
       else{
           navController?.navigate(destination)
       }
   }
fun navigationBackward(){
    val navHostFragment = fragmentManager.findFragmentById(fragmentContainer) as NavHostFragment
    val navController = navHostFragment.navController
    if(navController.currentDestination?.label!="InsertFromFragment" && navController.currentDestination?.label!="HomeFragment"){
        navController.navigateUp()
        navController.findDestination("hom")
    }
}

//    val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
//    val navController = navHostFragment.navController
//    navController.navigateUp()
}