package com.project.test.view.recyclerview

import android.app.Activity
import android.content.Context
import android.text.InputType.TYPE_CLASS_NUMBER
import android.text.InputType.TYPE_CLASS_TEXT
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.project.test.R
import com.project.test.databinding.RecyclerInfoBinding
import com.project.test.databinding.ReyclerLabBinding
import com.project.test.dataclass.DataCpReports
import com.project.test.dataclass.DataInfo
import com.project.test.dataclass.DataLab
import com.project.test.dataclass.SetDataInfo
import com.project.test.model.GetData
import com.project.test.model.Query
import com.project.test.model.SetData
import com.project.test.utils.Alert
import com.project.test.utils.CurrentTime
import com.project.test.utils.CustomToast
import com.project.test.utils.SharedPreferences
import com.project.test.utils.SharedViewModel


class InformationRecyclerViewAdapter(
    private val context: Activity,
    private val context1: ViewModelStoreOwner,
    private val context2: LifecycleOwner,
    private val Info: ArrayList<DataInfo>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            val binding = ReyclerLabBinding.inflate(context.layoutInflater, parent, false)
            LabInformationViewHolder(binding)
        } else {
            val binding = RecyclerInfoBinding.inflate(context.layoutInflater, parent, false)
            InformationViewHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return Info[position].isLab
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is InformationViewHolder) {
            holder.setData(Info[position])
        } else if (holder is LabInformationViewHolder) {
            holder.setData(Info[position])
        }
//
//        if(Info[position].isLab == 0){
//            (holder as InformationViewHolder).setData(Info[position])
//
//        }else{
//            (holder as LabInformationViewHolder).setData(Info[position])
////            holder.setData(Info[position])
//        }
    }

    override fun getItemCount(): Int = Info.size

    inner class LabInformationViewHolder(
        private val binding: ReyclerLabBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun setData(data: DataInfo) {
            val sharedPreferences = SharedPreferences(context)

            val importanceLevel = "درجه اهمیت: (${data.importanceLevel})"
            val title = "عنوان مشخصه: ${data.name}"
            val time = "${data.samplingInterval} دقیقه"
            val range =
                "${data.acceptableRangeMin} < ${data.acceptableRangeTarget} < ${data.acceptableRangeMax} درجه"

            binding.titleInfo1.text = title
            binding.titleInfo2.text = importanceLevel
            binding.codeDoc1.text = time
            binding.txtTitleDoc1.text = range

            val model1 = ViewModelProvider(context1)[SharedViewModel::class.java]

            binding.btnInfo.setOnClickListener {

                var text4: String?
                var finalText: SpannableString?
                var finalText1: String?
                if (binding.editText1.text.toString() == "") {
                    val textAlert =
                        "کاربر گرامی برای ثبت اطلاعات باید قسمت کد درخواست آزمایشگاه تکمیل گردد!"
                    val alert = Alert(context, textAlert, null, null, "متوجه شدم", null, "خطا")
                    alert.setOnClick(View.OnClickListener {
                    })
                    alert.alert()
                }
                if (binding.editText1.text.toString() != "") {
                    val color = ContextCompat.getColor(context, R.color.red)
                    val observe = binding.editText1.text.toString()
                    val station = sharedPreferences.getString("csValueSelected", "")
                    val quality = sharedPreferences.getString("cpValueSelected", "")
                    val text = "کاربر گرامی شما در حال ثبت اطلاعات برای ایستگاه کنترلی $station"
                    val text5 =
                        " و طرح کیفیت $quality می باشید. لطفا اطلاعات وارد شده را با دقت بررسی نمایید زیرا پس از تایید امکان ویرایش اطلاعات وجود ندارد!"
                    val text2 = " \n\nکد درخواست آزمایشگاه: $observe"

                    val spannableString: SpannableString?
                    val spannableString1: SpannableString?
                    val spannableString2: SpannableString?

                    val builder = SpannableStringBuilder()

                    spannableString = spannableString(text, station, color)
                    spannableString1 = spannableString(text5, quality, color)
                    spannableString2 = spannableString(text2, observe, color)

                    builder.append(spannableString);
                    builder.append(spannableString1);
                    builder.append(spannableString2);

                    val currentTime = CurrentTime().time()
                    val dataCpReports: DataCpReports? = GetData(context).information().first
                    val idCpReport = GetData(context).information().second
                    val reportOrder = GetData(context).information().third
                    val dataInfo = DataLab(
                        idCpReport!!,
                        data.id,
                        0,
                        binding.editText2.text.toString(),
                        currentTime,
                        reportOrder!!,
                        binding.editText1.text.toString()
                    )
                    val alert = Alert(context, null, null, builder, "تایید", "لغو", "هشدار")
                    alert.setOnClick(View.OnClickListener {
                        if (dataCpReports != null) {
                            val statusSet = SetData(context).information1(dataCpReports)
                            if (statusSet == -1L) {
                                CustomToast(context).toastAlert(
                                    null,
                                    "خطایی در هنگام ثبت اطلاعات رخ داده است!"
                                )
                            }
                        }
                        val statusSet3 = SetData(context).information(null, dataInfo)
                        if (statusSet3 == -1L) {
                            CustomToast(context).toastAlert(
                                null,
                                "عدم موفقیت در ثبت اطلاعات!"
                            )
                        } else {
                            CustomToast(context).toastValid(
                                null,
                                "اطلاعات وارد گردیده با موفقیت ثبت شدند."
                            )
                            emptyLab(binding)
                            GetData(context).count(
                                context1,
                                context2,
                                context,
                                sharedPreferences.getInt("idReports", 5)
                            )
                        }


                    })
                    alert.setOnClick1(View.OnClickListener {

                    })
                    alert.alert()
                }
            }
//            val fontSizeBtn = Size(context).fontSize(0.030f)
//            val fontSizeTitle = Size(context).fontSize(0.032f)
//            val fontSizeContent = Size(context).fontSize(0.032f)
//            val fontSizeDegree = Size(context).fontSize(0.028f)
//            val fontSizeTime = Size(context).fontSize(0.029f)
//
//            binding.btnInfo.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeBtn)
//            binding.txtTitleDoc.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle)
//            binding.txtTitleDoc1.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeDegree)
//            binding.codeDoc.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle)
//            binding.codeDoc1.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTime)
//            binding.observedValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle)
//            binding.editText1.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeContent)
//            binding.description.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle)
//            binding.editText2.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeContent)
//            binding.titleInfo1.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle)
//            binding.titleInfo2.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle)
        }

    }


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
            idTools.add(0, 0)
            listTools.add(0, "مقدار کیفی")
            correctionFactor.add(0, 0f)
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
            val model1 = ViewModelProvider(context1)[SharedViewModel::class.java]
            binding.spinnerViewInfo.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
                binding.arrowSpinnerInfo.startAnimation(animUp)
                idTool = idTools[newIndex]
                cF = correctionFactor[newIndex]
                if (oldIndex == 0 || newIndex == 0) {
                    binding.editText1.inputType = TYPE_CLASS_TEXT
                    binding.editText1.text = null
                    binding.radioConfirmation.isChecked = false
                    binding.radioRejection.isChecked = false
                }
                if (newIndex != 0) {
                    binding.editText1.inputType = TYPE_CLASS_NUMBER
                    if (binding.editText1.text!!.isNotEmpty()) {
                        val value = binding.editText1.text.toString()
                        change(
                            data.acceptableRangeMin,
                            data.acceptableRangeMax,
                            binding,
                            cF!!,
                            value.toFloat()
                        )
                    }
                }
                model1.statusSpinnerInfo("show")
            }
            var reportValue = ""
            binding.editText1.addTextChangedListener {
                val text = it.toString()
                if (cF != null && text != "" && idTool!! > 0) {
                    try {
                        val user = Integer.parseInt(text)
                        val value = user.toFloat()
                        reportValue = change(
                            data.acceptableRangeMin,
                            data.acceptableRangeMax,
                            binding,
                            cF!!,
                            value
                        )
                    } catch (e: NumberFormatException) {
                        // Handle the exception here
                    }
                } else {
                    binding.editText3Observed.text = "-"
                    reportValue = it.toString()
                    binding.editText1.setBackgroundResource(R.drawable.edit_text_info)
                    reportValue =
                        change(data.acceptableRangeMin, data.acceptableRangeMax, binding, 0f, 0f)
                }
            }
            binding.editText1.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
                if (idTool == null) {
                    CustomToast(context).toastAlert(
                        null,
                        "کاربر گرامی ابتدا از قسمت ابزار کنترلی آیتمی را انتخاب نمایید!"
                    )
                    model1.statusSpinnerInfo("notShow")
                    binding.editText5.layoutParams.height = binding.editText1.height
                    binding.editText1.visibility = View.GONE
                    binding.editText5.visibility = View.VISIBLE
                }
            }
            binding.editText5.setOnClickListener {
                if (idTool == null) {
                    CustomToast(context).toastAlert(
                        null,
                        "کاربر گرامی ابتدا از قسمت ابزار کنترلی آیتمی را انتخاب نمایید!"
                    )
                }
            }

            model1.spinnerInfo.observe(context2, Observer {
                if (it == "show" && idTool != null) {
                    binding.editText1.visibility = View.VISIBLE
                    binding.editText5.visibility = View.GONE
                    binding.editText1.isEnabled = true
                    binding.editText1.requestFocus()
                    val imm =
                        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                    imm?.showSoftInput(binding.editText1, InputMethodManager.SHOW_IMPLICIT)
                }
            })

            binding.btnInfo.setOnClickListener {
                var isChecked = 0
                var checkName: String? = null
                var state: Int? = null
                var text4: String?
                if (binding.radioConfirmation.isChecked) {
                    isChecked = 1
                    checkName = "تایید"
                    state = 1
                }
                if (binding.radioRejection.isChecked) {
                    isChecked = 1
                    checkName = "رد"
                    state = 0
                }
                if (binding.editText1.text.toString() == "" && isChecked == 1) {
                    val textAlert =
                        "کاربر گرامی برای ثبت اطلاعات باید قسمت مقدار مشاهده شده تکمیل گردد!"
                    val alert = Alert(context, textAlert, null, null, "متوجه شدم", null, "خطا")
                    alert.setOnClick(View.OnClickListener {
                    })
                    alert.alert()
                }
                if (binding.editText1.text.toString() != "" && isChecked != 1) {
                    val textAlert =
                        "کاربر گرامی برای ثبت اطلاعات باید قسمت وضعیت تکمیل گردد!"
                    val alert = Alert(context, textAlert, null, null, "متوجه شدم", null, "خطا")
                    alert.setOnClick(View.OnClickListener {
                    })
                    alert.alert()
                }
                if (binding.editText1.text.toString() == "" && isChecked != 1) {
                    val textAlert =
                        "کاربر گرامی برای ثبت اطلاعات باید قسمت های مقدار مشاهده شده و وضعیت تکمیل گردد!"
                    val alert = Alert(context, textAlert, null, null, "متوجه شدم", null, "خطا")
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
                    val text3 = "\nوضعیت: $checkName"

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
                            checkName.toString(), color
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
                            checkName.toString(), color
                        )
                        builder.append(spannableString);
                        builder.append(spannableString1);
                        builder.append(spannableString2);
                        builder.append(spannableString4);
                    }
                    var statusSet: Long? = null
                    val dataCpReports: DataCpReports? = GetData(context).information().first
                    val idCpReport = GetData(context).information().second
                    val reportOrder = GetData(context).information().third
                    val currentTime = CurrentTime().time()
                    val dataInfo = SetDataInfo(
                        idCpReport!!,
                        data.id,
                        idTool!!,
                        reportValue,
                        binding.editText2.text.toString(),
                        state!!,
                        currentTime,
                        reportOrder!!
                    )
                    val alert = Alert(context, null, null, builder, "تایید", "لغو", "هشدار")
                    alert.setOnClick(View.OnClickListener {
                        if (dataCpReports != null) {
                            statusSet = SetData(context).information1(dataCpReports)
                            if (statusSet == -1L) {
                                CustomToast(context).toastAlert(
                                    null,
                                    "خطایی در هنگام ثبت اطلاعات رخ داده است!"
                                )
                            }
                        }
                        val statusSet3 = SetData(context).information(dataInfo, null)
                        if (statusSet3 == -1L) {
                            CustomToast(context).toastAlert(
                                null,
                                "عدم موفقیت در ثبت اطلاعات!"
                            )
                        } else {
                            CustomToast(context).toastValid(
                                null,
                                "اطلاعات وارد گردیده با موفقیت ثبت شدند."
                            )
                            empty(binding)
                             idTool= null
                             cF= null
                            GetData(context).count(
                                context1,
                                context2,
                                context,
                                sharedPreferences.getInt("idReports", 5)
                            )
                        }
                    })


                    alert.setOnClick1(View.OnClickListener {

                    })
                    alert.alert()
                }
            }
//            val fontSizeBtn = Size(context).fontSize(0.030f)
//            val fontSizeTitle = Size(context).fontSize(0.032f)
//            val fontSizeContent = Size(context).fontSize(0.032f)
//            val fontSizeDegree = Size(context).fontSize(0.028f)
//            val fontSizeTime = Size(context).fontSize(0.029f)
//
//            binding.btnInfo.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeBtn)
//            binding.txtTitleDoc.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle)
//            binding.txtTitleDoc1.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeDegree)
//            binding.codeDoc.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle)
//            binding.codeDoc1.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTime)
//            binding.controlTools.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle)
//            binding.observedValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle)
//            binding.editText1.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeContent)
//            binding.observedValueChange.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle)
//            binding.editText3Observed.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeContent)
//            binding.description.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle)
//            binding.editText2.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeContent)
//            binding.statusInfo.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle)
//            binding.radioConfirmation.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeContent)
//            binding.radioRejection.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeContent)
//            binding.titleInfo1.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle)
//            binding.titleInfo2.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle)
        }

    }

    fun change(
        min: Float,
        max: Float,
        binding: RecyclerInfoBinding,
        cf: Float,
        value: Float
    ): String {
        val sum = value + cf
        if (value > 0) {
            if ((sum >= min) && (max <= sum)) {
                binding.editText1.background = getDrawable(context, R.drawable.bac_valid)
                binding.radioConfirmation.isChecked = true
            } else {
                binding.editText1.background = getDrawable(context, R.drawable.back_wrong)
                binding.radioRejection.isChecked = true
            }
            binding.editText3Observed.text = sum.toString()
        } else {
            binding.radioConfirmation.isChecked = false
            binding.radioRejection.isChecked = false
        }
        return sum.toString()
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

private fun emptyLab(binding: ReyclerLabBinding){
    binding.editText1.text= null
    binding.editText2.text= null
}

private fun empty(binding: RecyclerInfoBinding){
    binding.editText1.text= null
    binding.editText2.text= null
    binding.spinnerViewInfo.text = "انتخاب کنید"
    binding.editText3Observed.text="-"
    binding.radioConfirmation.isChecked = false;
    binding.radioRejection.isChecked = false

    binding.editText5.layoutParams.height = binding.editText1.height
    binding.editText1.isEnabled = false
    binding.editText1.visibility = View.GONE
    binding.editText5.visibility = View.VISIBLE
}