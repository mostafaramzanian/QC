package com.project.test.view.activity


import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.Spannable
import android.text.SpannableString
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.addTextChangedListener
import com.kusu.loadingbutton.LoadingButton
import com.project.test.R
import com.project.test.databinding.ActivityLoginBinding
import com.project.test.dataclass.DataUser
import com.project.test.model.GetData
import com.project.test.model.Query
import com.project.test.utils.CustomToast
import com.project.test.utils.GoToOtherActivity
import com.project.test.utils.MyService
import com.project.test.utils.SharedPreferences
import com.project.test.utils.Utils
import com.toxicbakery.bcrypt.Bcrypt
import java.util.Locale


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onBackPressed() {
        Utils.exitApp(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        stopService(Intent(this, MyService::class.java))
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val colorStateListAlertDisable =
            ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.alert))

        binding.edtInputUsername.addTextChangedListener {
            binding.edtInputUsername.setBackgroundResource(R.drawable.edit_text_bg)
            binding.username.defaultHintTextColor = colorStateListAlertDisable
            binding.alertUser.visibility = View.GONE
        }

        binding.edtInputPassword.addTextChangedListener {
            binding.edtInputPassword.setBackgroundResource(R.drawable.edit_text_bg)
            binding.password.defaultHintTextColor = colorStateListAlertDisable
            if (binding.edtInputPassword.text.toString().trim().isEmpty()) {
                binding.showPassword.visibility = View.GONE
                binding.showPassword.isChecked = false
            } else {
                binding.showPassword.visibility = View.VISIBLE
            }
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

        val loginButton = findViewById<LoadingButton>(R.id.login)

        binding.login.setOnClickListener {

            Utils.hideKeyboard(this@LoginActivity)

            loginButton.showLoading()

            var user = binding.edtInputUsername.text.toString()
            val password = binding.password.editText?.text.toString()
            user = user.toCharArray().joinToString("") {
                if (it.isDigit()) {
                    it.toString().lowercase(Locale.ENGLISH)
                } else {
                    it.lowercaseChar().toString()
                }
            }
            Thread {

                val userData: DataUser? = GetData(this).getUser(user)

                if (userData != null) {

                    if (validation(
                            user, password, userData
                        )
                    ) {

                        val userType1 = Query(this).userTypes(userData.user_type)
                        var userType2 = ""
                        if (userType1.moveToFirst()) {
                            userType2 =
                                userType1.getString(userType1.getColumnIndexOrThrow("title"))
                        }

                        if (userData.user_type == "QC_EXPERT" || userData.user_type == "QC_REVIEWER" || userData.user_type == "QUALITY_ASSURANCE_EXPERT") {
                            sharedPreferences.putString("username", user)
                            sharedPreferences.putInt("userId", userData.id)
                            sharedPreferences.putString(
                                "fullName", "${userData.firstname} ${userData.lastname}"
                            )
                            sharedPreferences.putString("userType", userData.user_type)
                            sharedPreferences.putString("userTypeTitle", userType2)

                            val tableUserProcesses = Query(this).userProcesses(userData.id)
                            if (tableUserProcesses.moveToFirst()) {
                                val processId = tableUserProcesses.getInt(
                                    tableUserProcesses.getColumnIndexOrThrow(
                                        "process_id"
                                    )
                                )
                                sharedPreferences.putInt("process_id", processId)
                            }
                            Handler(mainLooper).post {
                                loginButton.hideLoading()

                                CustomToast(this).toastValid(
                                    SpannableString(getString(R.string.success_login)), null
                                )

                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                    this@LoginActivity,
                                    binding.imageView2,
                                    ViewCompat.getTransitionName(binding.imageView2)!!
                                )

                                startActivity(intent, options.toBundle())
                                finish()
                            }


//                        GoToOtherActivity(this).mainActivity()
                        } else {

                            val fullName = "${userData.firstname} ${userData.lastname}"
                            val text1 =
                                "کاربر گرامی $fullName از آن جا که نوع کاربری شما در سیستم $userType2 تعریف شده است امکان ورود به برنامه را ندارید! فقط کاربرانی که نوع کاربری آن ها بازرس می باشد اجازه ورود دارند."
                            val spannableString: SpannableString?
                            val spannableString1: SpannableString?
                            val spannableString2: SpannableString?
                            var spannableString3: SpannableString? = null
                            if (text1.length >= fullName.length) {
                                spannableString = SpannableString(text1)
                                val start = fullName.length + 45
                                val end = userType2.length + start
                                spannableString.setSpan(
                                    ForegroundColorSpan(Color.BLACK),
                                    12,
                                    fullName.length + 12,
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                                spannableString1 = spannableString
                                spannableString1.setSpan(
                                    ForegroundColorSpan(Color.BLACK),
                                    start,
                                    end,
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                                spannableString2 = SpannableString(spannableString1)
                                val startIndex1 = spannableString2.indexOf("ندارید!")
                                val endIndex1 = startIndex1 + "ندارید!".length
                                spannableString2.setSpan(
                                    ForegroundColorSpan(Color.BLACK),
                                    startIndex1,
                                    endIndex1,
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                )

                                spannableString3 = SpannableString(spannableString2)
                                val startIndex = spannableString3.indexOf("بازرس")
                                val endIndex = startIndex + "بازرس".length
                                spannableString3.setSpan(
                                    ForegroundColorSpan(Color.BLACK),
                                    startIndex,
                                    endIndex,
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                            }
                            Handler(mainLooper).post {
                                loginButton.hideLoading()
                                CustomToast(this).toastAlert(spannableString3, null)

                            }

//                        for (i in 1..8) {
//                            CustomToast(this).toastAlert(spannableString3, null)
//                        }


                        }
                    } else {
                        val text = "نام کاربری یا گذرواژه اشتباه است"
                        val spannableString = SpannableString(text)
                        spannableString.setSpan(
                            ForegroundColorSpan(ContextCompat.getColor(this, R.color.white)),
                            0,
                            text.length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        Handler(mainLooper).post {
                            loginButton.hideLoading()
                            CustomToast(this).toastAlert(spannableString, null)
                        }
                    }


                } else {
                    val text = "نام کاربری یا گذرواژه اشتباه است"
                    val spannableString = SpannableString(text)
                    spannableString.setSpan(
                        ForegroundColorSpan(ContextCompat.getColor(this, R.color.white)),
                        0,
                        text.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    Handler(mainLooper).post {
                        loginButton.hideLoading()
                        CustomToast(this).toastAlert(spannableString, null)
                    }

                }

            }.start()


        }
    }

    // private val alert = Alert(this)
    // این متد مسئول اعتبار سنجی ورودی های کاربر است
    private fun validation(user: String, pass: String, userData: DataUser): Boolean {


        val colorStateListAlertEnable =
            ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.red))
        var validUserPass = false

        val isEmptyUser = when {
            user.isEmpty() -> {
                Handler(mainLooper).post {
                    binding.edtInputUsername.setBackgroundResource(R.drawable.alert_edit_text)
                    binding.username.defaultHintTextColor = colorStateListAlertEnable
                    binding.alertUser.visibility = View.VISIBLE
                }

                false
            }

            else -> {
                true
            }
        }
        val isEmptyPassword = when {
            pass.isEmpty() -> {
                Handler(mainLooper).post {
                    binding.edtInputPassword.setBackgroundResource(R.drawable.alert_edit_text)
                    binding.password.defaultHintTextColor = colorStateListAlertEnable
                    binding.alertPass.visibility = View.VISIBLE
                }

                false
            }

            else -> {
                true
            }
        }

        if (isEmptyUser && isEmptyPassword) {

            validUserPass = Bcrypt.verify(pass, userData.passwd.toByteArray())

//            validUserPass = when {
//
//                !Bcrypt.verify(pass, userData.passwd.toByteArray()) -> {
//                    val text = "رمز عبور اشتباه است!"
//                    val spannableString = SpannableString(text)
//                    spannableString.setSpan(
//                        ForegroundColorSpan(ContextCompat.getColor(this, R.color.white)),
//                        0,
//                        text.length,
//                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//                    )
//                    CustomToast(this).toastAlert(spannableString, null)
//                    false
//                }
//
//                else -> {
//                    true
//                }
//
//            }
        }
        return isEmptyUser && isEmptyPassword && validUserPass
    }

}




