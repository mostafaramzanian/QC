package com.project.test

import android.app.Activity
import android.content.ContentValues
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.project.test.databinding.RecyclerInfoBinding
import saman.zamani.persiandate.PersianDate
import saman.zamani.persiandate.PersianDateFormat
import java.text.SimpleDateFormat
import java.util.Calendar


class InformationRecyclerAdapter(
    private val context: Activity,
    private val context1: ViewModelStoreOwner,
    private val context2: LifecycleOwner,
    private val Info: ArrayList<DataInfo>
) : RecyclerView.Adapter<InformationRecyclerAdapter.InformationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InformationViewHolder {
        val binding = RecyclerInfoBinding.inflate(context.layoutInflater, parent, false)
        return InformationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InformationViewHolder, position: Int) {
        holder.setData(Info[position])
    }

    override fun getItemCount(): Int = Info.size

    inner class InformationViewHolder(
        private val binding: RecyclerInfoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun setData(data: DataInfo) {
            val sharedPreferences = SharedPreferences(context)
            val tools = Query(context).tools(
                data.toolType, sharedPreferences.getInt("process_id", 0)
            )
            val importanceLevel = "درجه اهمیت: (${data.importanceLevel})"
            val title = "عنوان مشخصه: ${data.name}"
            val time = "${data.samplingInterval} دقیقه"
            val range =
                "${data.acceptableRangeMin} < ${data.acceptableRangeTarget} < ${data.acceptableRangeMax} درجه"
            val idTools = mutableListOf<Int>()
            val listTools = mutableListOf<String>()
            val correctionFactor = mutableListOf<Float>()
            binding.titleInfo1.text = title
            binding.titleInfo2.text = importanceLevel
            binding.codeDoc1.text = time
            binding.txtTitleDoc1.text = range
            if (tools.moveToFirst()) {
                do {
                    idTools.add(tools.getInt(tools.getColumnIndexOrThrow("id")))
                    listTools.add(tools.getString(tools.getColumnIndexOrThrow("title")))
                    correctionFactor.add(tools.getFloat(tools.getColumnIndexOrThrow("correction_factor")))
                } while (tools.moveToNext())
            }

            binding.spinnerViewInfo.setItems(listTools)

            val animDown = AnimationUtils.loadAnimation(context, R.anim.spinner_dropdown_anim)
            val animUp = AnimationUtils.loadAnimation(context, R.anim.spinner_dropup_anim)
            binding.spinnerViewInfo.setOnClickListener {
                if (binding.spinnerViewInfo.isShowing) {
                    binding.spinnerViewInfo.dismiss()
                    binding.arrowSpinnerInfo.startAnimation(animUp)
                } else {
                    binding.spinnerViewInfo.show()
                    binding.arrowSpinnerInfo.startAnimation(animDown)
                }
            }
            var idTool: Int? = null
            var cF: Float? = null
            var check: String? = null
            val model1 = ViewModelProvider(context1)[SharedViewModel::class.java]
            binding.spinnerViewInfo.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
                binding.arrowSpinnerInfo.startAnimation(animUp)
                idTool = idTools[newIndex]
                cF = correctionFactor[newIndex]
                check = newItem
                model1.statusSpinnerInfo("show")
            }
            var reportValue=""
            binding.editText1.addTextChangedListener {
                val text = it.toString()
                if (cF != null && text != "") {
                    val user = Integer.parseInt(text);
                    val t = user.toFloat()
                    val sum = t + cF!!
                    binding.editText3Observed.text = sum.toString()
                    reportValue=sum.toString()
                } else {
                    binding.editText3Observed.text = "-"
                    reportValue=it.toString()
                }
            }
            binding.editText1.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
                if (check == null) {
                    CustomToast(context).toastAlert(
                        null,
                        "کاربری گرامی ابتدا از ابزار کنترلی موردی را انتخاب نمایید!"
                    )

                    binding.editText1.isEnabled = false
                    model1.statusSpinnerInfo("notShow")
                } else {
                    binding.editText1.isCursorVisible = true;
                }
            }
            binding.editText1.setOnClickListener {
                binding.editText1.isCursorVisible = true;
            }
            model1.spinnerInfo.observe(context2, Observer {

            })

            binding.btnInfo.setOnClickListener {
                var isChecked = 0
                var CheckName: String? = null
                var text4: String?
                if (binding.radioConfirmation.isChecked) {
                    isChecked = 1
                    CheckName = "تایید"
                }
                if (binding.radioRejection.isChecked) {
                    isChecked = 1
                    CheckName = "رد"
                }
                if (binding.editText1.text.toString() == "" && isChecked == 1) {
                    val textAlert =
                        "کاربر گرامی برای ثبت اطلاعات باید قسمت مقدار مشاهده شده تکمیل گردد!"
                    val alert = Alert(context, textAlert, null, null, "متوجه شدم",null,"خطا")
                    alert.setOnClick(View.OnClickListener {
                    })
                    alert.alert()
                }
                if (binding.editText1.text.toString() != "" && isChecked != 1) {
                    val textAlert =
                        "کاربر گرامی برای ثبت اطلاعات باید قسمت وضعیت تکمیل گردد!"
                    val alert = Alert(context, textAlert, null, null, "متوجه شدم",null,"خطا")
                    alert.setOnClick(View.OnClickListener {
                    })
                    alert.alert()
                }
                if (binding.editText1.text.toString() == "" && isChecked != 1) {
                    val textAlert =
                        "کاربر گرامی برای ثبت اطلاعات باید قسمت های مقدار مشاهده شده و وضعیت تکمیل گردد!"
                    val alert = Alert(context, textAlert, null, null, "متوجه شدم",null,"خطا")
                    alert.setOnClick(View.OnClickListener {
                    })
                    alert.alert()
                }
                if (binding.editText1.text.toString() != "" && isChecked == 1) {
                    val color = ContextCompat.getColor(context, R.color.red)
                    val observe = binding.editText1.text.toString()
                    val observe1 = binding.editText3Observed.text
                    val station = sharedPreferences.getString("csValueSelected", "")
                    val quality = sharedPreferences.getString("cpValueSelected", "")
                    val text = "کاربر گرامی شما در حال ثبت اطلاعات برای ایستگاه کنترلی $station"
                    val text5 =
                        " و طرح کیفیت $quality می باشید. لطفا اطلاعات وارد شده را با دقت بررسی نمایید زیرا پس از تایید امکان ویرایش اطلاعات وجود ندارد!"
                    val text2 = " \n\nمقدار مشاهده شده: $observe"
                    val text3 = "\nوضعیت: $CheckName"

                    val spannableString: SpannableString?
                    val spannableString1: SpannableString?
                    val spannableString2: SpannableString?
                    val spannableString3: SpannableString?
                    val spannableString4: SpannableString?


                    val builder = SpannableStringBuilder()

                    if (observe1 != null) {
                        text4 = "\nمقدار مشاهده شده پس از اعمال ضریب تصحیح: $observe1"
                        spannableString = spannableString(text, station, color)
                        spannableString1 = spannableString(text5, quality, color)
                        spannableString2 = spannableString(text2, observe, color)
                        spannableString3 = spannableString(
                            text4,
                            observe1.toString(), color
                        )
                        spannableString4 = spannableString(
                            text3,
                            CheckName.toString(), color
                        )
                        builder.append(spannableString);
                        builder.append(spannableString1);
                        builder.append(spannableString2);
                        builder.append(spannableString3);
                        builder.append(spannableString4);
                    } else {
                        spannableString = spannableString(text, station, color)
                        spannableString1 = spannableString(text5, quality, color)
                        spannableString2 = spannableString(text2, observe, color)
                        spannableString4 = spannableString(
                            text3,
                            CheckName.toString(), color
                        )
                        builder.append(spannableString);
                        builder.append(spannableString1);
                        builder.append(spannableString2);
                        builder.append(spannableString4);
                    }
                    val pdate = PersianDate()
                    val pdformater1 = PersianDateFormat("تاریخ: Y/m/d")
                    val formattedDate = pdformater1.format(pdate)
                    val currentTime = Calendar.getInstance().time
                    val sdf = SimpleDateFormat("ساعت: HH:mm:ss")
                    val formattedTime = sdf.format(currentTime)
                    val time="$formattedDate$formattedTime"

                    val statusSet1=Query(context).getLastRecord()
                    statusSet1.moveToFirst()
                    val idCpReport = statusSet1.getInt(statusSet1.getColumnIndexOrThrow("id"))

                    val values = ContentValues().apply {
                        put("cp_id", sharedPreferences.getInt("cpIdSelected",0))
                        put("station_id", sharedPreferences.getInt("csIdSelected",0))
                        put("created_by_user", sharedPreferences.getInt("process_id", 0))
                        put("created_datetime", time)
                        put("product_tracking_code", "null")
                        put("is_draft", 1)
                        put("completed_datetime", "null")
                        put("shift", "null")
                        put("operator_name", "null")
                        put("production_count", "null")
                    }
                    val values1 = ContentValues().apply {
                        put("report_id", idCpReport+1)
                        put("parameter_id", data.id)
                        put("tool_id", idTool)
                        put("report_value", reportValue)
                        put("attachment", "null")
                        put("description", binding.editText2.text.toString())
                        put("status", "1")
                        put("created_datetime", time)
                        put("report_order", "null")
                        put("lab_request_code", "null")
                    }

                    val alert = Alert(context, null, null, builder, "تایید","عدم تایید","هشدار")
                    alert.setOnClick(View.OnClickListener {
                        val statusSet=Query(context).insertCpReports(values)
                        if (statusSet == -1L) {
                            CustomToast(context).toastAlert(
                                null,
                                "اطلاعات وارد شده به درستی ثبت نشد!"
                            )
                        } else {
                            CustomToast(context).toastValid(
                                null,
                                "اطلاعات وارد شده با موفقیت ثبت گردید."
                            )
                        }
                    })
                    alert.setOnClick1(View.OnClickListener {

                    })
                    alert.alert()
                }
            }
            val fontSizeBtn = Size(context).fontSize(0.030f)
            val fontSizeTitle = Size(context).fontSize(0.032f)
            val fontSizeContent = Size(context).fontSize(0.032f)
            val fontSizeDegree = Size(context).fontSize(0.028f)
            val fontSizeTime = Size(context).fontSize(0.029f)

            binding.btnInfo.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeBtn)
            binding.txtTitleDoc.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle)
            binding.txtTitleDoc1.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeDegree)
            binding.codeDoc.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle)
            binding.codeDoc1.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTime)
            binding.controlTools.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle)
            binding.observedValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle)
            binding.editText1.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeContent)
            binding.observedValueChange.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle)
            binding.editText3Observed.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeContent)
            binding.description.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle)
            binding.editText2.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeContent)
            binding.statusInfo.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle)
            binding.radioConfirmation.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeContent)
            binding.radioRejection.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeContent)
            binding.titleInfo1.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle)
            binding.titleInfo2.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle)
        }

    }
}

private fun spannableString(text: String?, text3: String?, color: Int): SpannableString {
    val spannableString = SpannableString(text)
    val startIndex = spannableString.indexOf(text3!!)
    val endIndex = startIndex + text3.length
    spannableString.setSpan(
        ForegroundColorSpan(color),
        startIndex,
        endIndex,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return spannableString
}