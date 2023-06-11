package com.project.test


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.shape.MaterialShapeDrawable
import com.project.test.databinding.ActivityMainBinding
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper
import java.util.Timer
import java.util.TimerTask


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onBackPressed() {

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = SQLiteAssetHelper(this, "QC.db", null, 1).writableDatabase
        val cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null)
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                Log.i("Table Name", cursor.getString(0))
                cursor.moveToNext()
            }
        }
        cursor.close()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.groupNav.visibility = View.VISIBLE
        binding.groupNav1.visibility = View.GONE

        binding.groupMore.visibility = View.GONE
        binding.groupMore1.visibility = View.VISIBLE

        val model = ViewModelProvider(this)[SharedViewModel::class.java]
        model.show.observe(this, Observer {
            if (it == "Hide") {
                binding.customTitleLayout.backPage.visibility = View.INVISIBLE
            } else {
                binding.customTitleLayout.backPage.visibility = View.VISIBLE
            }
        })
        val fragmentList = Stack()

        binding.customTitleLayout.backPage.setOnClickListener {
            val size = fragmentList.pop(supportFragmentManager, R.id.fragmentContainer)
        }

        val count= GetData(this).homePage()
        if(count>0){
            binding.arrow.clearAnimation()
            binding.arrow.visibility = View.GONE
        }else{
            val anim = AnimationUtils.loadAnimation(this, R.anim.arrow_anim)
            binding.arrow.startAnimation(anim)
        }


        val bottomAppBar = findViewById<BottomAppBar>(R.id.bottom_app_bar)
        val bottomBarBackground = bottomAppBar.background as MaterialShapeDrawable
        bottomBarBackground.shapeAppearanceModel = bottomBarBackground.shapeAppearanceModel
            .toBuilder()
            //.setBottomLeftCorner(ROUNDED,50f)
            //.setBottomRightCorner(ROUNDED,50f)
            //.setAllCorners(RoundedCornerTreatment()).setAllCornerSizes(RelativeCornerSize(0.5f))
            .build()

        binding.fabOptions.setOnClickListener {
            binding.arrow.clearAnimation()
            binding.arrow.visibility = View.GONE
            binding.groupNav.visibility = View.GONE
            binding.groupNav1.visibility = View.VISIBLE
            binding.groupMore.visibility = View.GONE
            binding.groupMore1.visibility = View.VISIBLE
            binding.showMore.slideDown()
            /*
                val bundle = Bundle()
                bundle.putBoolean("hideTextView", true)
                val myFragment = HomeFragment()
                myFragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, myFragment).commit()
        */
            FragmentReplacer(supportFragmentManager).replaceFragments(
                HomeFragment(),
                InsertReportFragment(),
                R.id.fragmentContainer
            )
        }

        val fontSize = Size(this).fontSize(0.029f)

        val titleApp = Size(this).fontSize(0.045f)
        //Size(this).changeSize(0.065f,0.03f,findViewById<ImageButton>(R.id.home_menu))
        val textView = findViewById<TextView>(R.id.title_home)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
        val textView1 = findViewById<TextView>(R.id.title_home1)
        textView1.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
        val textView2 = findViewById<TextView>(R.id.title_more)
        textView2.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
        val textView3 = findViewById<TextView>(R.id.title_more1)
        textView3.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
        val textView4 = findViewById<TextView>(R.id.text_exit)
        textView4.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)

        binding.customTitleLayout.textTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleApp)


        val view = findViewById<ConstraintLayout>(R.id.constraint)
        view.viewTreeObserver.addOnGlobalLayoutListener {
            val height = binding.bottomAppBar.height
            binding.innerViewGroup.layoutParams.height = height
            binding.innerViewGroup.visibility = View.GONE;
            binding.innerViewGroup.visibility = View.VISIBLE;
        }

        click(binding.background1, this, binding, supportFragmentManager, fragmentList)
        click(binding.homeMenu1, this, binding, supportFragmentManager, fragmentList)
        click(binding.titleHome1, this, binding, supportFragmentManager, fragmentList)

        home(binding.background, binding,this)
        home(binding.homeMenu, binding,this)
        home(binding.titleHome, binding,this)

        clickMore(binding.backgroundMore, binding, this, this,this)
        clickMore(binding.more, binding, this, this,this)
        clickMore(binding.titleMore, binding, this, this,this)

        clickMore1(binding.backgroundMore1, binding,this,this)
        clickMore1(binding.more1, binding,this,this)
        clickMore1(binding.titleMore1, binding,this,this)
        binding.image1.setOnClickListener {
            binding.groupMore.visibility = View.GONE
            binding.groupMore1.visibility = View.VISIBLE
            binding.showMore.slideDown()
            SharedPreferences(this).putBoolean("menuExit",false)
        }
        /*
     binding.background1.setOnClickListener {
         binding.groupNav.visibility = View.VISIBLE
         binding.groupNav1.visibility = View.GONE
         FragmentReplacer(supportFragmentManager).replaceFragments(
             fragmentList.getLastFragment(),
             HomeFragment(),
             R.id.fragmentContainer
         )
     }
*/
        exit(binding.exitIcon, this)
        exit(binding.textExit, this)

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
          stopService(Intent(this, MyService::class.java))
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
        }else{
            stopService(Intent(this, MyService::class.java))
        }

    }


}
fun startService(context: Activity){
    context.startService(Intent(context, MyService::class.java))
}

fun exit(
    view: View,
    context: Activity
) {
    view.setOnClickListener {
       SharedPreferences(context).clear()
        context.stopService(Intent(context, MyService::class.java))
        GoToOtherActivity(context).login()
    }
}

fun home(
    view: View,
    binding: ActivityMainBinding,
context:Context
) {
    view.setOnClickListener {
        binding.showMore.slideDown()
        SharedPreferences(context).putBoolean("menuExit",false)
        binding.groupMore.visibility = View.GONE
        binding.groupMore1.visibility = View.VISIBLE
    }
}


fun click(
    view: View,
    context:Context,
    binding: ActivityMainBinding,
    fragmentManager: FragmentManager,
    fragmentList: Stack
) {
    view.setOnClickListener {
        binding.showMore.slideDown()
        SharedPreferences(context).putBoolean("menuExit",false)
        binding.groupNav.visibility = View.VISIBLE
        binding.groupNav1.visibility = View.GONE
        binding.groupMore.visibility = View.GONE
        binding.groupMore1.visibility = View.VISIBLE
        FragmentReplacer(fragmentManager).replaceFragments(
            fragmentList.getLastFragment(),
            HomeFragment(),
            R.id.fragmentContainer
        )
        val anim = AnimationUtils.loadAnimation(context, R.anim.arrow_anim)
        binding.arrow.startAnimation(anim)
    }
}

fun clickMore(
    view: View,
    binding: ActivityMainBinding,
    context: ViewModelStoreOwner,
    context1: LifecycleOwner,
    context2: Context,
) {
    view.setOnClickListener {
        val model1 = ViewModelProvider(context)[SharedViewModel::class.java]
        model1.statusViewSpinner("show")
        SharedPreferences(context2).putBoolean("menuExit",false)
            binding.groupMore.visibility = View.GONE
            binding.groupMore1.visibility = View.VISIBLE
        binding.showMore.slideDown()
        model1.fragment.observe(context1, Observer {
            if(it=="HomeFragment"){
                binding.groupNav.visibility = View.VISIBLE
                binding.groupNav1.visibility = View.GONE
            }
        })

    }
}

fun clickMore1(
    view: View,
    binding: ActivityMainBinding,
    context: ViewModelStoreOwner,
    context1: Context,
) {
    view.setOnClickListener {
        val model1 = ViewModelProvider(context)[SharedViewModel::class.java]
        model1.statusViewSpinner("hidden")
        SharedPreferences(context1).putBoolean("menuExit",true)
        binding.groupMore.visibility = View.VISIBLE
        binding.groupMore1.visibility = View.GONE
        binding.groupNav.visibility = View.GONE
        binding.groupNav1.visibility = View.VISIBLE
        binding.showMore.slideUp()
    }
}
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

