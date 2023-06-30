package com.project.test.view.activity


import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.core.widget.addTextChangedListener
import com.project.test.R
import com.project.test.databinding.ActivityLoginBinding
import com.project.test.model.Query
import com.project.test.utils.CustomToast
import com.project.test.utils.GoToOtherActivity
import com.project.test.utils.MyService
import com.project.test.utils.SharedPreferences
import com.toxicbakery.bcrypt.Bcrypt
import java.util.Locale


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onBackPressed() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Query(this).deleteAll("cp_reports")
        // Query(this).deleteAll("cp_reports_parameters")
        // Query(this).deleteAll("cp_reports_info")

        stopService(Intent(this, MyService::class.java))
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val colorStateListAlertDisable =
            ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.alert))

        binding.edtInputUsername.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                //   binding.usernameIcon.setMargins(0, 0, 280, 44)
                //  binding.hintUsername.setMargins(0, 0, 10, 44)
                //  binding.usernameIcon.size(30, 30)
                // binding.hintUsername.textSize = 16f
            }
            if (!hasFocus && binding.edtInputUsername.text.toString().trim().isEmpty()) {
                //   binding.usernameIcon.setMargins(0, 0, 280, 0)
                //   binding.hintUsername.setMargins(0, 0, 10, 0)
                //    binding.usernameIcon.size(50, 50)
                //    binding.hintUsername.textSize = 20f
            }
        }
//        binding.edtInputPassword.setOnFocusChangeListener { _, hasFocus ->
//            if (hasFocus) {
        //   binding.passwordIcon.setMargins(0, 0, 280, 44)
        //   binding.hintPassword.setMargins(0, 0, 10, 44)
//                binding.showPassword.setMargins(0, 85, 240, 0)
//                if (binding.alertPass.visibility == View.GONE) {
//                    binding.rememberMe.setMargins(0, 87, 240, 0)
//                } else {
//                    binding.rememberMe.setMargins(0, 157, 240, 0)
//                }
        //   binding.passwordIcon.size(30, 30)
        //    binding.hintPassword.textSize = 16f
//            }
//            if (!hasFocus && binding.edtInputPassword.text.toString().trim().isEmpty()) {
        //   binding.passwordIcon.setMargins(0, 0, 280, 0)
        //   binding.hintPassword.setMargins(0, 0, 10, 0)
        //    binding.passwordIcon.size(40, 40)
//                if (binding.alertPass.visibility == View.GONE) {
//                    binding.rememberMe.setMargins(0, 60, 240, 0)
//                } else {
//                    binding.rememberMe.setMargins(0, 130, 240, 0)
//                }

        // binding.hintPassword.textSize = 20f
//            }
//        }

        binding.edtInputUsername.addTextChangedListener {
            binding.edtInputUsername.setBackgroundResource(R.drawable.edit_text_bg)
            binding.username.defaultHintTextColor = colorStateListAlertDisable
//            binding.hintUsername.setTextColor(
//                ContextCompat.getColor(
//                    this,
//                    R.color.alert
//                )
//            );
//            binding.usernameIcon.setImageResource(R.drawable.user_icon_blue);
            binding.alertUser.visibility = View.GONE
        }

        binding.edtInputPassword.addTextChangedListener {
            binding.edtInputPassword.setBackgroundResource(R.drawable.edit_text_bg)
            binding.password.defaultHintTextColor = colorStateListAlertDisable
//            binding.hintPassword.setTextColor(
//                ContextCompat.getColor(
//                    this,
//                    R.color.alert
//                )
//            )
//            binding.passwordIcon.setImageResource(R.drawable.password_icon_blue);
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
            val start = binding.edtInputPassword.selectionStart
            val end = binding.edtInputPassword.selectionEnd
            if (binding.showPassword.isChecked) {
                binding.edtInputPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding.edtInputPassword.setSelection(start, end)
            } else {
                binding.edtInputPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.edtInputPassword.setSelection(start, end)

            }
        }
        val sharedPreferences = SharedPreferences(this)
        val userID = sharedPreferences.getInt("userId", -1)
        if (userID != -1) {
            GoToOtherActivity(this).mainActivity()
            return
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
            val tableLogin = Query(this).login(user)
            if (tableLogin.moveToFirst()) {
                Log.d("~~~~~~~p", tableLogin.getString(tableLogin.getColumnIndexOrThrow("passwd")))
                if (validation(
                        user,
                        password,
                        tableLogin.getString(tableLogin.getColumnIndexOrThrow("passwd"))
                    )
                ) {

                    val userId = tableLogin.getInt(tableLogin.getColumnIndexOrThrow("id"))
                    val name = tableLogin.getString(tableLogin.getColumnIndexOrThrow("firstname"))
                    val lastName =
                        tableLogin.getString(tableLogin.getColumnIndexOrThrow("lastname"))
                    val userType =
                        tableLogin.getString(tableLogin.getColumnIndexOrThrow("user_type"))

                    val userType1 = Query(this).userTypes(userType)
                    var userType2 = ""
                    if (userType1.moveToFirst()) {
                        userType2 = userType1.getString(userType1.getColumnIndexOrThrow("title"))
                    }

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
                        sharedPreferences.putString("userTypeTitle", userType2)

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
                        CustomToast(this).toastValid(spannableString, null, null, null)

                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            this@LoginActivity,
                            binding.imageView2,
                            ViewCompat.getTransitionName(binding.imageView2)!!
                        )
                        startActivity(intent, options.toBundle())

//                        GoToOtherActivity(this).mainActivity()
                    } else {
                        val color = ContextCompat.getColor(this, R.color.black)
                        val fullName = "$name $lastName"
                        val type = "بازرس"
                        val text1 =
                            "کاربر گرامی $fullName از آن جا که نوع کاربری شما در سیستم اجازه ورود دارند."
                        val text2 = "$userType2 تعریف شده است امکان ورود به برنامه را ندارید! "
                        val text3 =
                            "فقط کاربرانی که نوع کاربری آن ها $type می باشد اجازه ورود دارند."

                        val spannableString: SpannableString?
                        val spannableString1: SpannableString?
                        val spannableString2: SpannableString?

                        val builder = SpannableStringBuilder()

                        spannableString = com.project.test.utils.SpannableString()
                            .spannableString(text1, fullName, color, null, null)
                        spannableString1 = com.project.test.utils.SpannableString()
                            .spannableString(text2, userType2, color, null, null)
                        spannableString2 = com.project.test.utils.SpannableString()
                            .spannableString(text3, type, color, null, null)

                        builder.append(spannableString);
                        builder.append(spannableString1);
                        builder.append(spannableString2);
                        val string = SpannableString.valueOf(builder)

                        CustomToast(this).toastAlert(string, null, null, null)

                    }
                }


                tableLogin.close()

            } else {
                val text = "نام کاربری یا گذرواژه اشتباه است"
                val spannableString = SpannableString(text)
                spannableString.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(this, R.color.white)),
                    0,
                    text.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                CustomToast(this).toastAlert(spannableString, null, null, null)
            }
        }
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
    private fun validation(user: String, pass: String, hash_passwd: String): Boolean {

        val tableLogin = Query(this).login(user)

        val colorStateListAlertEnable =
            ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.red))
        var validUserPass = false

        val isEmptyUser = when {
            user.isEmpty() -> {

                binding.edtInputUsername.setBackgroundResource(R.drawable.alert_edit_text)
                binding.username.defaultHintTextColor = colorStateListAlertEnable
//                binding.hintUsername.setTextColor(Color.RED)
//                binding.usernameIcon.setImageResource(R.drawable.user_ico_red);
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
//                binding.hintPassword.setTextColor(Color.RED)
//                binding.passwordIcon.setImageResource(R.drawable.password_icon_red);
                binding.alertPass.visibility = View.VISIBLE
                false
            }

            else -> {
                true
            }
        }

        if (isEmptyUser && isEmptyPassword) {


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
                    CustomToast(this).toastAlert(spannableString, null, null, null)
                    false
                }

                !Bcrypt.verify(pass, hash_passwd.toByteArray()) -> {

                    val text = "رمز عبور اشتباه است!"
                    val spannableString = SpannableString(text)
                    spannableString.setSpan(
                        ForegroundColorSpan(ContextCompat.getColor(this, R.color.white)),
                        0,
                        text.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    CustomToast(this).toastAlert(spannableString, null, null, null)
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




