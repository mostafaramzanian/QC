package com.project.test.view.activity


import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ForegroundColorSpan
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.addTextChangedListener
import com.github.ybq.android.spinkit.style.Wave
import com.project.test.R
import com.project.test.databinding.ActivityLoginBinding
import com.project.test.dataclass.DataUser
import com.project.test.model.Database
import com.project.test.model.GetData
import com.project.test.utils.CustomToast
import com.project.test.utils.GoToOtherActivity
import com.project.test.utils.SharedPreferences
import com.project.test.utils.Utils
import com.project.test.view.activity.usb_sync.SyncDataActivity
import de.nycode.bcrypt.verify
import java.util.Locale
import kotlin.concurrent.thread


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onBackPressed() {
        Utils.exitApp(this)
    }

    override fun onDestroy() {
        Database(this.application).close()
        CustomToast(this).cancelAllToasts(0)
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val colorStateListAlertDisable =
            ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.alert))

        binding.imageView2.setOnLongClickListener(View.OnLongClickListener {
            Intent(this@LoginActivity, SyncDataActivity::class.java).also {
                startActivity(it)
            }
            return@OnLongClickListener true
        })

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


        val view = binding.login
        view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.spinKit.layoutParams.height = view.height
                binding.spinKit.layoutParams.width = view.width
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
        var check = true
//        val mWaveDrawable = Wave()
//        mWaveDrawable.color = resources.getColor(R.color.white)
//        mWaveDrawable.setBounds(0, 0, 300, 70)

        binding.login.setOnClickListener {
           // binding.login.text=""
           // binding.login.setCompoundDrawables(mWaveDrawable, null, null, null)

            binding.login.visibility=View.GONE
            binding.spinKit.visibility=View.VISIBLE
            Utils.hideKeyboard(this@LoginActivity)

            var user = binding.edtInputUsername.text.toString()
            val password = binding.password.editText?.text.toString()

            val colorStateListAlertEnable =
                ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.red))

            var isValidUsernameAndPass = true;

            if (user == "") {
                binding.edtInputUsername.setBackgroundResource(R.drawable.alert_edit_text)
                binding.username.defaultHintTextColor = colorStateListAlertEnable
                binding.alertUser.visibility = View.VISIBLE
                isValidUsernameAndPass = false
            } else {
                binding.alertUser.visibility = View.GONE

            }

            if (password == "") {
                binding.edtInputPassword.setBackgroundResource(R.drawable.alert_edit_text)
                binding.password.defaultHintTextColor = colorStateListAlertEnable
                binding.alertPass.visibility = View.VISIBLE
                isValidUsernameAndPass = false
            } else {
                binding.alertPass.visibility = View.GONE
            }

            if (!isValidUsernameAndPass) return@setOnClickListener

            user = user.toCharArray().joinToString("") {
                if (it.isDigit()) {
                    it.toString().lowercase(Locale.ENGLISH)
                } else {
                    it.lowercaseChar().toString()
                }
            }
            if (check) {
             //   mWaveDrawable.start();
                check = false
                thread(true) {

                    val userData: DataUser? = GetData(this,this).getUser(user)

                    if (userData != null) {

                        if (verify(password, userData.passwd.toByteArray())) {

                            val userType2 = userData.user_type_title

                            if (userData.user_type == "QC_EXPERT" || userData.user_type == "QC_REVIEWER" || userData.user_type == "QUALITY_ASSURANCE_EXPERT") {
                                sharedPreferences.putString("username", user)
                                sharedPreferences.putString("password", password)
                                sharedPreferences.putInt("userId", userData.id)
                                sharedPreferences.putString(
                                    "fullName", "${userData.firstname} ${userData.lastname}"
                                )
                                sharedPreferences.putString(
                                    "firstname",userData.firstname
                                )
                                sharedPreferences.putString(
                                    "lastname", userData.lastname
                                )
                                sharedPreferences.putString("userType", userData.user_type)
                                sharedPreferences.putString(
                                    "userTypeTitle",
                                    userData.user_type_title
                                )
                                sharedPreferences.putInt("process_id", userData.process_id)
                                sharedPreferences.putString("process_name", userData.process_name)

                                runOnUiThread() {
                                    check=true
                               //     mWaveDrawable.stop()
                                    //binding.login.text="ورود"
                                  //  binding.login.setCompoundDrawables(null, null, null, null)
                                    binding.login.visibility=View.VISIBLE
                                    binding.spinKit.visibility=View.GONE
                                    CustomToast(this).toastValid(
                                        SpannableString(getString(R.string.success_login)),
                                        null,
                                        null,
                                        null
                                    )
                                    val intent =
                                        Intent(this@LoginActivity, MainActivity::class.java)
                                    val options =
                                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                                            this@LoginActivity,
                                            binding.imageView2,
                                            ViewCompat.getTransitionName(binding.imageView2)!!
                                        )
                                    startActivity(intent, options.toBundle())
                                    finish()
                                }
                            } else {
                                check=true
                                val color = ContextCompat.getColor(this, R.color.black)
                                val fullName = "${userData.firstname} ${userData.lastname}"
                                val type = "بازرس"
                                val text1 =
                                    "کاربر گرامی $fullName از آن جا که نوع کاربری شما در سیستم اجازه ورود دارند."
                                val text2 =
                                    "$userType2 تعریف شده است امکان ورود به برنامه را ندارید! "
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
                                Handler(mainLooper).post {
//                                    mWaveDrawable.stop()
//                                    binding.login.text="ورود"
//                                    binding.login.setCompoundDrawables(null, null, null, null)
                                    binding.login.visibility=View.VISIBLE
                                    binding.spinKit.visibility=View.GONE
                                    CustomToast(this).toastAlert(string, null, null, null)
                                }
                            }
                        } else {
                            check=true
                            val text = "نام کاربری یا گذرواژه اشتباه است"
                            val spannableString = SpannableString(text)
                            spannableString.setSpan(
                                ForegroundColorSpan(ContextCompat.getColor(this, R.color.white)),
                                0,
                                text.length,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                            Handler(mainLooper).post {
//                                mWaveDrawable.stop()
//                                binding.login.text="ورود"
//                                binding.login.setCompoundDrawables(null, null, null, null)
                                binding.login.visibility=View.VISIBLE
                                binding.spinKit.visibility=View.GONE
                                CustomToast(this).toastAlert(spannableString, null, null, null)
                            }
                        }
                    } else {
                        check=true
                        val text = "نام کاربری یا گذرواژه اشتباه است"
                        val spannableString = SpannableString(text)
                        spannableString.setSpan(
                            ForegroundColorSpan(ContextCompat.getColor(this, R.color.white)),
                            0,
                            text.length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        Handler(mainLooper).post {
//                            mWaveDrawable.stop()
//                            binding.login.text="ورود"
//                            binding.login.setCompoundDrawables(null, null, null, null)
                            binding.login.visibility=View.VISIBLE
                            binding.spinKit.visibility=View.GONE
                            CustomToast(this).toastAlert(spannableString, null, null, null)
                        }
                    }
                }
            }
            CustomToast(this).cancelAllToasts(1)
        }
    }


}




