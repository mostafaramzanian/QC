package com.project.test.view.activity



import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.core.widget.addTextChangedListener
import com.project.test.utils.CustomToast
import com.project.test.utils.GoToOtherActivity
import com.project.test.utils.MyService
import com.project.test.R
import com.project.test.utils.SharedPreferences
import com.project.test.databinding.LogingBinding
import com.project.test.model.Query
import java.util.Locale


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: LogingBinding

    override fun onBackPressed() {
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // Query(this).deleteAll("cp_reports")
       // Query(this).deleteAll("cp_reports_parameters")
       // Query(this).deleteAll("cp_reports_info")

       stopService(Intent(this, MyService::class.java))
        binding = LogingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val colorStateListAlertDisable =
            ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.alert))

        binding.edtInputUsername.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.usernameIcon.setMargins(0, 0, 280, 44)
                binding.hintUsername.setMargins(0, 0, 10, 44)
                binding.usernameIcon.size(30, 30)
                binding.hintUsername.textSize = 16f
            }
            if (!hasFocus && binding.edtInputUsername.text.toString().trim().isEmpty()) {
                binding.usernameIcon.setMargins(0, 0, 280, 0)
                binding.hintUsername.setMargins(0, 0, 10, 0)
                binding.usernameIcon.size(50, 50)
                binding.hintUsername.textSize = 20f
            }
        }
        binding.edtInputPassword.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.passwordIcon.setMargins(0, 0, 280, 44)
                binding.hintPassword.setMargins(0, 0, 10, 44)
                binding.showPassword.setMargins(0, 85, 240, 0)
                if(binding.alertPass.visibility == View.GONE) {
                    binding.rememberMe.setMargins(0, 87, 240, 0)
                }else{
                    binding.rememberMe.setMargins(0, 157, 240, 0)
                }
                binding.passwordIcon.size(30, 30)
                binding.hintPassword.textSize = 16f
            }
            if (!hasFocus && binding.edtInputPassword.text.toString().trim().isEmpty()) {
                binding.passwordIcon.setMargins(0, 0,280 , 0)
                binding.hintPassword.setMargins(0, 0, 10, 0)
                binding.passwordIcon.size(40, 40)
                if(binding.alertPass.visibility == View.GONE) {
                    binding.rememberMe.setMargins(0, 60, 240, 0)
                }else{
                    binding.rememberMe.setMargins(0, 130, 240, 0)
                }

                binding.hintPassword.textSize = 20f
            }
        }

        binding.edtInputUsername.addTextChangedListener {
            binding.edtInputUsername.setBackgroundResource(R.drawable.edit_text_bg)
            binding.username.defaultHintTextColor = colorStateListAlertDisable;
            binding.hintUsername.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.alert
                )
            );
            binding.usernameIcon.setImageResource(R.drawable.user_icon_blue);
            binding.alertUser.visibility = View.GONE
        }

        binding.edtInputPassword.addTextChangedListener {
            binding.edtInputPassword.setBackgroundResource(R.drawable.edit_text_bg)
            binding.password.defaultHintTextColor = colorStateListAlertDisable;
            binding.hintPassword.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.alert
                )
            )
            binding.passwordIcon.setImageResource(R.drawable.password_icon_blue);
            if (binding.edtInputPassword.text.toString().trim().isEmpty()) {
                binding.showPassword.visibility = View.GONE
                binding.showPassword.isChecked = false
                binding.rememberMe.setMargins(0, 86, 240, 0)
            } else {
                binding.showPassword.visibility = View.VISIBLE
                binding.rememberMe.setMargins(0, 30, 240, 0)
            }
            // binding.password.transformationMethod = PasswordTransformationMethod.getInstance()
            binding.alertPass.visibility = View.GONE
        }
        binding.showPassword.setOnClickListener {
            val start = binding.edtInputPassword.selectionStart;
            val end = binding.edtInputPassword.selectionEnd;
            if (binding.showPassword.isChecked) {
                binding.edtInputPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding.edtInputPassword.setSelection(start, end);
            } else {
                binding.edtInputPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.edtInputPassword.setSelection(start, end);

            }
        }
        val sharedPreferences = SharedPreferences(this)
        val rememberMe = sharedPreferences.getBoolean("rememberMe", false)
        if (rememberMe) {
          GoToOtherActivity(this).mainActivity()
        }
        binding.login.setOnClickListener {
            var user = binding.edtInputUsername.text.toString()
            val password = binding.password.editText?.text.toString()
             user = user.toCharArray().joinToString("") {
                if (it.isDigit()) {
                    it.toString().lowercase(Locale.ENGLISH)
                } else {
                    it.lowercaseChar().toString()
                }
            }
            val tableLogin= Query(this).login(user)
            if(validation(user, password)) {
                if (tableLogin.moveToFirst()) {
                    val userId = tableLogin.getInt(tableLogin.getColumnIndexOrThrow("id"))
                    val name = tableLogin.getString(tableLogin.getColumnIndexOrThrow("firstname"))
                    val lastName =
                        tableLogin.getString(tableLogin.getColumnIndexOrThrow("lastname"))
                    val userType =
                        tableLogin.getString(tableLogin.getColumnIndexOrThrow("user_type"))
                    if (userType == "QC_EXPERT" || userType == "QC_REVIEWER" || userType == "QUALITY_ASSURANCE_EXPERT") {
                        sharedPreferences.putString("username", user)
                        if (binding.rememberMe.isChecked) {
                            sharedPreferences.putBoolean("rememberMe", true)
                        } else {
                            sharedPreferences.putBoolean("rememberMe", false)
                        }
                        sharedPreferences.putInt("userId", userId)
                        sharedPreferences.putString("fullName", "$name $lastName")
                        sharedPreferences.putString("userType", userType)

                        val id = sharedPreferences.getInt("userId", 0)
                        val tableUserProcesses = Query(this).userProcesses(id)
                        if (tableUserProcesses.moveToFirst()) {
                            val processId =
                                tableUserProcesses.getInt(tableUserProcesses.getColumnIndexOrThrow("process_id"))
                            sharedPreferences.putInt("process_id", processId)
                        }
                        val fullName = sharedPreferences.getString("fullName", "")
                        val text = "کاربر "
                        val spannableString = SpannableString("$text$fullName خوش آمدید")
                        spannableString.setSpan(
                            ForegroundColorSpan(ContextCompat.getColor(this, R.color.red)),
                            text.length,
                            text.length + fullName.length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        CustomToast(this).toastValid(spannableString, null)

                        GoToOtherActivity(this).mainActivity()
                    } else {
                        val userType1= Query(this).userTypes(userType)
                        var userType2=""
                        if (userType1.moveToFirst()) {
                            userType2 =
                                userType1.getString(userType1.getColumnIndexOrThrow("title"))
                        }
                        val fullName = "$name $lastName"
                        val text1 ="کاربر گرامی $fullName از آن جا که کاربری شما در سیستم $userType2 تعریف شده است امکان ورود به برنامه را ندارید! فقط کاربرانی که کاربری آن ها بازرس می باشد اجازه ورود دارند."
                        val spannableString: SpannableString?
                        val spannableString1: SpannableString?
                        val spannableString2: SpannableString?
                        var spannableString3: SpannableString? = null
                        if (text1.length >= fullName.length) {
                             spannableString = SpannableString(text1)
                           val start= fullName.length+45
                            val end= userType2.length+start
                            spannableString.setSpan(ForegroundColorSpan(Color.BLACK), 12, fullName.length+12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                            spannableString1=spannableString
                            spannableString1.setSpan(ForegroundColorSpan(Color.BLACK), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                            spannableString2 = SpannableString(spannableString1)
                            val startIndex1 = spannableString2.indexOf("ندارید!")
                            val endIndex1 = startIndex1 + "ندارید!".length
                            spannableString2.setSpan(ForegroundColorSpan(Color.BLACK), startIndex1, endIndex1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

                             spannableString3 = SpannableString(spannableString2)
                            val startIndex = spannableString3.indexOf("بازرس")
                            val endIndex = startIndex + "بازرس".length
                            spannableString3.setSpan(ForegroundColorSpan(Color.BLACK), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        }
                        for (i in 1..8) {
                            CustomToast(this).toastAlert(spannableString3, null)
                        }
                    }
                }
            }

                tableLogin.close()



        }
    }
    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()
    }
    override fun onStop() {
        super.onStop()
    }
    override fun onDestroy() {
        super.onDestroy()
    }




/*
    private fun customToast(spannableString:SpannableString){
        val inflater = layoutInflater
        val layout: View = inflater.inflate(R.layout.toast, null)
        val textView = layout.findViewById<TextView>(R.id.textToast)
        textView.text = spannableString
        val toast = Toast(applicationContext)
        toast.view = layout
        toast.show()
    }

 */


    private fun View.setMargins(
        left: Int = this.marginLeft,
        top: Int = this.marginTop,
        right: Int = this.marginRight,
        bottom: Int = this.marginBottom
    ) {
        layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
            setMargins(left, top, right, bottom)
        }
    }

    private fun View.size(Width: Int, Height: Int) {
        layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
            width = Width
            height = Height
        }
    }

   // private val alert = Alert(this)
    // این متد مسئول اعتبار سنجی ورودی های کاربر است
    private fun validation(user: String, pass: String): Boolean {

       val tableLogin= Query(this).login(user)

        val colorStateListAlertEnable =
            ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.red))
       var validUserPass=false

        val isEmptyUser = when {
            user.isEmpty() -> {

                binding.edtInputUsername.setBackgroundResource(R.drawable.alert_edit_text)
                binding.username.defaultHintTextColor = colorStateListAlertEnable
                binding.hintUsername.setTextColor(Color.RED)
                binding.usernameIcon.setImageResource(R.drawable.user_ico_red);
                binding.alertUser.visibility = View.VISIBLE
                false
            }
            else -> {
                true
            }
        }
       val isEmptyPassword = when {
           pass.isEmpty() -> {
               binding.edtInputPassword.setBackgroundResource(R.drawable.alert_edit_text)
               binding.password.defaultHintTextColor = colorStateListAlertEnable
               binding.hintPassword.setTextColor(Color.RED)
               binding.passwordIcon.setImageResource(R.drawable.password_icon_red);
               binding.alertPass.visibility = View.VISIBLE
               false
           }
           else -> {
               true
           }
       }


       if(isEmptyUser&&isEmptyPassword) {
            validUserPass = when {
               tableLogin.count == 0 -> {
                   val text = "نام کاربری اشتباه است!"
                   val spannableString = SpannableString(text)
                   spannableString.setSpan(
                       ForegroundColorSpan(ContextCompat.getColor(this, R.color.white)),
                       0,
                       text.length,
                       Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                   )
                   CustomToast(this).toastAlert(spannableString, null)
                   false
               }

               pass != "1" -> {

                   val text = "رمز عبور اشتباه است!"
                   val spannableString = SpannableString(text)
                   spannableString.setSpan(
                       ForegroundColorSpan(ContextCompat.getColor(this, R.color.white)),
                       0,
                       text.length,
                       Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                   )
                   CustomToast(this).toastAlert(spannableString, null)
                   false
               }

               else -> {
                   true
               }

           }
       }
        return isEmptyUser && isEmptyPassword && validUserPass
    }

}




