package com.project.test.view.activity


import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.provider.Settings
import android.view.MotionEvent
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.project.test.BuildConfig
import com.project.test.R
import com.project.test.databinding.ActivityMainBinding
import com.project.test.utils.MyService
import com.project.test.utils.NavigationApp
import com.project.test.utils.SharedPreferences
import com.project.test.utils.SharedViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val PermissionsRequestId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        requestFilePermissions()
//        val json = DatabaseReportsAsJson(this).getJson()
        /*
                val json = Json(this).getJson()
                val table1 = json.getJSONArray("cp_reports")

                for (i in 0 until table1.length()) {
                    val row = table1.getJSONObject(i)
                    val column1 = row?.getInt("id")
                    val column2 = row?.getInt("cp_id")
                    val column3 = row?.getInt("station_id")
                    val column4 = row?.getInt("created_by_user")
                    val text= "$column1 $column2 $column3 $column4"
                    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
                }


         */
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

//        binding.groupNav.visibility = View.VISIBLE
//        binding.groupNav1.visibility = View.GONE
//        binding.groupMore.visibility = View.GONE
//        binding.groupMore1.visibility = View.VISIBLE

        val model = ViewModelProvider(this)[SharedViewModel::class.java]
        model.show.observe(this, Observer {
            if (it == "Hide") {
                binding.customTitleLayout.backPage.visibility = View.INVISIBLE
            } else {
                binding.customTitleLayout.backPage.visibility = View.VISIBLE
            }
        })
        val navigationApp = NavigationApp(this, supportFragmentManager, R.id.fragmentContainer)
        // setupActionBarWithNavController(findNavController(R.id.fragmentContainer))

//        val count = GetData(this).homePage()
//        if (count > 0) {
//            binding.arrow.clearAnimation()
//            binding.arrow.visibility = View.GONE
//        } else {
//            val anim = AnimationUtils.loadAnimation(this, R.anim.arrow_anim)
//            binding.arrow.startAnimation(anim)
//        }


//        val bottomAppBar = findViewById<BottomAppBar>(R.id.bottom_app_bar)
//        val bottomBarBackground = bottomAppBar.background as MaterialShapeDrawable
//        bottomBarBackground.shapeAppearanceModel = bottomBarBackground.shapeAppearanceModel
//            .toBuilder()
//            //.setBottomLeftCorner(ROUNDED,50f)
//            //.setBottomRightCorner(ROUNDED,50f)
//            //.setAllCorners(RoundedCornerTreatment()).setAllCornerSizes(RelativeCornerSize(0.5f))
//            .build()

        binding.fabOptions.setOnClickListener {
            navigationApp.emptyStack()
            navigationApp.navigationForward(
                R.id.action_home_menu_to_insertFromFragment, "InsertFromFragment"
            )
//            binding.arrow.clearAnimation()
//            binding.arrow.visibility = View.GONE
//            binding.groupNav.visibility = View.GONE
//            binding.groupNav1.visibility = View.VISIBLE
//            binding.groupMore.visibility = View.GONE
//            binding.groupMore1.visibility = View.VISIBLE
            // binding.showMore.slideDown()
            /*
                val bundle = Bundle()
                bundle.putBoolean("hideTextView", true)
                val myFragment = HomeFragment()
                myFragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, myFragment).commit()
 */

        }
        binding.navHome.setOnClickListener {
            navigationApp.emptyStack()
            navigationApp.navigationForward(R.id.action_home_menu_self, "HomeFragment")
        }

        findViewById<TextView>(R.id.nav_more).setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this)
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout)
            bottomSheetDialog.findViewById<View>(R.id.exit)?.setOnClickListener {
                SharedPreferences(this@MainActivity).clear()

                finish()
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
            }
            bottomSheetDialog.show()
        }


//        val fontSize = Size(this).fontSize(0.029f)

//        val titleApp = Size(this).fontSize(0.045f)
        //Size(this).changeSize(0.065f,0.03f,findViewById<ImageButton>(R.id.home_menu))
//        val textView = findViewById<TextView>(R.id.title_home)
//        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
//        val textView1 = findViewById<TextView>(R.id.title_home1)
//        textView1.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
//        val textView2 = findViewById<TextView>(R.id.title_more)
//        textView2.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
//        val textView3 = findViewById<TextView>(R.id.title_more1)
//        textView3.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
//        val textView4 = findViewById<TextView>(R.id.text_exit)
//        textView4.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)

//        binding.customTitleLayout.textTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleApp)


//        val view = findViewById<LinearLayout>(R.id.constraint)
//        view.viewTreeObserver.addOnGlobalLayoutListener {
//            val height = binding.bottomAppBar.height
//            binding.innerViewGroup.layoutParams.height = height
//            binding.innerViewGroup.visibility = View.GONE;
//            binding.innerViewGroup.visibility = View.VISIBLE;
//        }

        //click(binding.navHome, this, binding, supportFragmentManager, fragmentList)
//        click(binding.background1, this, binding, supportFragmentManager, fragmentList)
//        click(binding.homeMenu1, this, binding, supportFragmentManager, fragmentList)
//        click(binding.titleHome1, this, binding, supportFragmentManager, fragmentList)
//
//        home(binding.navHome, binding, this)
//        home(binding.homeMenu, binding, this)
//        home(binding.titleHome, binding, this)
//
//        clickMore(binding.navMore, binding, this, this, this)
//        clickMore(binding.more, binding, this, this, this)
//        clickMore(binding.titleMore, binding, this, this, this)
//
//        clickMore1(binding.backgroundMore1, binding, this, this)
//        clickMore1(binding.more1, binding, this, this)
//        clickMore1(binding.titleMore1, binding, this, this)
        /*
        binding.image1.setOnClickListener {
            binding.groupMore.visibility = View.GONE
            binding.groupMore1.visibility = View.VISIBLE
            binding.showMore.slideDown()
            SharedPreferences(this).putBoolean("menuExit", false)
        }
        binding.image2.setOnClickListener {
        }
*/
//        binding.background1.setOnClickListener {
//            binding.groupNav.visibility = View.VISIBLE
//            binding.groupNav1.visibility = View.GONE
//            FragmentReplacer(supportFragmentManager).replaceFragments(
//                fragmentList.getLastFragment(),
//                HomeFragment(),
//                R.id.fragmentsContainer
//            )
//        }

//        exit(binding.exitIcon, this)
//        exit(binding.textExit, this)
        //defaultFragment(supportFragmentManager)
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
        stopService(Intent(this, MyService::class.java))
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        startService(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        val rememberMe = SharedPreferences(this).getBoolean("rememberMe", false)
        if (rememberMe) {
            startService(this)
        } else {
            stopService(Intent(this, MyService::class.java))
        }

    }

    private fun requestFilePermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            val uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID)
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

fun startService(context: Activity) {
    context.startService(Intent(context, MyService::class.java))
}
//
//fun exit(
//    view: View, context: Activity
//) {
//    view.setOnClickListener {
//        SharedPreferences(context).clear()
//        context.stopService(Intent(context, MyService::class.java))
//        GoToOtherActivity(context).login()
//    }
//}
//
//fun home(
//    view: View, binding: ActivityMainBinding, context: Context
//) {
//    view.setOnClickListener {
////        binding.showMore.slideDown()
//        SharedPreferences(context).putBoolean("menuExit", false)
////        binding.groupMore.visibility = View.GONE
////        binding.groupMore1.visibility = View.VISIBLE
//    }
//}

//
//fun click(
//    view: View,
//    context: Context,
//    binding: ActivityMainBinding,
//    fragmentManager: FragmentManager,
//    fragmentList: Stack
//) {
//    view.setOnClickListener {
////        binding.showMore.slideDown()
//        SharedPreferences(context).putBoolean("menuExit", false)
////        binding.groupNav.visibility = View.VISIBLE
////        binding.groupNav1.visibility = View.GONE
////        binding.groupMore.visibility = View.GONE
////        binding.groupMore1.visibility = View.VISIBLE
//
//        FragmentReplacer(fragmentManager).replaceFragments(
//            fragmentList.getLastFragment(), HomeFragment(), R.id.fragmentsContainer
//        )
//
//
//        val anim = AnimationUtils.loadAnimation(context, R.anim.arrow_anim)
////        binding.arrow.startAnimation(anim)
//    }
//}
//
//fun clickMore(
//    view: View,
//    binding: ActivityMainBinding,
//    context: ViewModelStoreOwner,
//    context1: LifecycleOwner,
//    context2: Context,
//) {
//    view.setOnClickListener {
//        val model1 = ViewModelProvider(context)[SharedViewModel::class.java]
//        model1.statusViewSpinner("show")
//        SharedPreferences(context2).putBoolean("menuExit", false)
////        binding.groupMore.visibility = View.GONE
////        binding.groupMore1.visibility = View.VISIBLE
////        binding.showMore.slideDown()
//        model1.fragment.observe(context1, Observer {
//            if (it == "HomeFragment") {
////                binding.groupNav.visibility = View.VISIBLE
////                binding.groupNav1.visibility = View.GONE
//            }
//        })
//
//    }
//}
//
//fun clickMore1(
//    view: View,
//    binding: ActivityMainBinding,
//    context: ViewModelStoreOwner,
//    context1: Context,
//) {
//    view.setOnClickListener {
//        val model1 = ViewModelProvider(context)[SharedViewModel::class.java]
//        model1.statusViewSpinner("hidden")
//        SharedPreferences(context1).putBoolean("menuExit", true)
////        binding.groupMore.visibility = View.VISIBLE
////        binding.groupMore1.visibility = View.GONE
////        binding.groupNav.visibility = View.GONE
////        binding.groupNav1.visibility = View.VISIBLE
////        binding.showMore.slideUp()
//    }
//}

fun View.slideUp(duration: Int = 500) {
    if (visibility == View.INVISIBLE) {
        visibility = View.VISIBLE
        val animate = TranslateAnimation(0f, 0f, this.height.toFloat(), 0f)
        animate.duration = duration.toLong()
        animate.fillAfter = true
        this.startAnimation(animate)
    }
}

fun View.slideDown(duration: Int = 500) {
    if (visibility == View.VISIBLE) {
        visibility = View.INVISIBLE
        val animate = TranslateAnimation(0f, 0f, 0f, this.height.toFloat())
        animate.duration = duration.toLong()
        animate.fillAfter = false
        this.startAnimation(animate)
    }
}

//fun homeFragment(fragmentManager: FragmentManager) {
//    val transaction = fragmentManager.beginTransaction()
//    transaction.add(R.id.fragmentContainer, HomeFragment())
//    transaction.commit()
//}
//
//fun insertReportFragment(fragmentManager: FragmentManager) {
//    val transaction = fragmentManager.beginTransaction()
//    transaction.add(R.id.fragmentsContainer, InsertReportFragment())
//    transaction.commit()
//}
