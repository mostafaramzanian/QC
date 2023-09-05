package com.project.test.view.activity


import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler

import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.provider.Settings
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.erkutaras.showcaseview.ShowcaseManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.project.test.R
import com.project.test.databinding.ActivityMainBinding
import com.project.test.dataclass.DataUser
import com.project.test.model.Database
import com.project.test.model.GetData
import com.project.test.utils.NavigationApp
import com.project.test.utils.SharedPreferences
import com.project.test.utils.SharedViewModel
import de.nycode.bcrypt.verify
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val PermissionsRequestId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val model1 = ViewModelProvider(this)[SharedViewModel::class.java]

        binding.customTitleLayout.showcase.setOnClickListener {

            val builder = ShowcaseManager.Builder()
            builder.context(this)
                .key("TEST")
                .developerMode(true)
                .buttonText("")
                .buttonVisibility(false)//To hide button
                .cancelButtonVisibility(true)//To hide cancel button
                .moveButtonsVisibility(true)
                .gradientFocusEnabled(false)
                .radiusFirst(150f)
                .radius(60f)
                .alphaBackground(210)

            model1.showcase.observe(this, Observer {

                if (it == "HomeFragment") {

                    builder.context(this)
                        .view(findViewById(R.id.nav_home))
                        .descriptionTitle("آیکن خانه")
                        .descriptionText("شما می توانید به وسیله ی این گزینه به صفحه خانه بروید.")
                        .roundedRectangle()
                        .add()

                        .view(findViewById(R.id.fab_options))
                        .descriptionTitle("آیکن اضافه کردن")
                        .descriptionText("")
                        .circle()
                        .add()

                        .view(findViewById(R.id.nav_more))
                        .descriptionTitle("آیکن بیشتر")
                        .descriptionText("")
                        .roundedRectangle()
                        .add()

                        .view(findViewById(R.id.showcase))
                        .descriptionTitle("آیکن راهنما")
                        .descriptionText("")
                        .circle()
                        .add()

                } else if (it == "InsertReportFragment") {
                    builder.context(this)
                        .view(findViewById(R.id.spinnerView))
                        .descriptionTitle("آیکن خانه")
                        .descriptionText("شما می توانید به وسیله ی این گزینه به صفحه خانه بروید.")
                        .roundedRectangle()
                        .add()
                }

            })
            builder.context(this)
                .build()
                .show()
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                NavigationApp(
                    this@MainActivity, supportFragmentManager, R.id.fragmentContainer
                ).navigationBackward()
            }
        })

        binding.customTitleLayout.backPage.setOnClickListener {
            NavigationApp(
                this@MainActivity, supportFragmentManager, R.id.fragmentContainer
            ).navigationBackward()
        }

        val model = ViewModelProvider(this)[SharedViewModel::class.java]
        model.show.observe(this, Observer {
            if (it == "Hide") {
                binding.customTitleLayout.backPage.visibility = View.INVISIBLE
            } else {
                binding.customTitleLayout.backPage.visibility = View.VISIBLE
            }
        })
        val navigationApp = NavigationApp(this, supportFragmentManager, R.id.fragmentContainer)


        binding.fabOptions.setOnClickListener {
            navigationApp.navigationForward("InsertFromFragment")
         //  AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            /*
                val bundle = Bundle()
                bundle.putBoolean("hideTextView", true)
                val myFragment = HomeFragment()
                myFragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, myFragment).commit()
 */
        }
        binding.navHome.setOnClickListener {
            navigationApp.navigationForward("HomeFragment")

            thread(true) {
                authentication()
            }
         // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        findViewById<TextView>(R.id.nav_more).setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this)
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout)
            bottomSheetDialog.findViewById<View>(R.id.exit)?.setOnClickListener {
                clearSharedPreferences()
            }
            bottomSheetDialog.show()
        }
    }
    private fun authentication() {
        val sharedPreferences = SharedPreferences(this)
        val userName = sharedPreferences.getString("username", "")
        val password = sharedPreferences.getString("password", "")
        val userId = sharedPreferences.getInt("userId", -1)
        val userType = sharedPreferences.getString("userType", "")
        val userTypeTitle = sharedPreferences.getString("userTypeTitle", "")
        val processId = sharedPreferences.getInt("process_id", -1)
        val processName = sharedPreferences.getString("process_name", "")
        val firstname = sharedPreferences.getString("firstname", "")
        val lastname = sharedPreferences.getString("lastname", "")
        val userData: DataUser? = GetData(this, this).getUser(userName)
        if (userData != null) {
            if (!verify(
                    password,
                    userData.passwd.toByteArray()
                ) || userId != userData.id || userType != userData.user_type || userTypeTitle!= userData.user_type_title ||processId != userData.process_id || processName != userData.process_name || firstname != userData.firstname || lastname != userData.lastname
            ) {
                clearSharedPreferences()
            }

        } else {
            clearSharedPreferences()
        }
    }
    private fun clearSharedPreferences() {
        SharedPreferences(this@MainActivity).clear()
        finish()
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        Handler(mainLooper).post {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@MainActivity,
                binding.customTitleLayout.imageView2,
                ViewCompat.getTransitionName(binding.customTitleLayout.imageView2)!!
            )
            startActivity(intent, options.toBundle())
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            // stopService(Intent(this, MyService::class.java))
        }
        if (event?.action == MotionEvent.ACTION_UP) {
            //   startService(Intent(this, MyService::class.java))
        }
        return super.onTouchEvent(event)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        Database(this.application).close()
        super.onDestroy()


//        val rememberMe = SharedPreferences(this).getBoolean("rememberMe", false)
//        if (rememberMe) {
//            startService(this)
//        } else {
//            stopService(Intent(this, MyService::class.java))
//        }

    }

    private fun requestFilePermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            // val uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID)
            val uri = Uri.parse("package:" + applicationContext.packageName)
            startActivity(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri))
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf<String>(
                    READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE
                ), PermissionsRequestId
            )
            val builder = VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())
        }

    }


}

