package com.project.test.view.fragment


import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.project.test.R
import com.project.test.databinding.FinalRegistrationBinding
import com.project.test.dataclass.DataFinalRegister
import com.project.test.model.GetData
import com.project.test.model.SetData
import com.project.test.utils.Alert
import com.project.test.utils.CurrentTime
import com.project.test.utils.CustomToast
import com.project.test.utils.GoToOtherActivity
import com.project.test.utils.SharedPreferences
import com.project.test.utils.SharedViewModel
import com.project.test.utils.SpannableString


class FinalRegistrationFragment : Fragment() {
    private lateinit var binding: FinalRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FinalRegistrationBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val width1 = Size(requireContext()).calWidth(0.048f)
//        val height1 = Size(requireContext()).calHeight(0.032f)
//        val editTextList = mutableListOf<EditText>()
//        val buttonDeleteList = mutableListOf<Button>()
//        val textViewList = mutableListOf<TextView>()

//        val editTextList1 = mutableListOf<EditText>()
//        val buttonDeleteList1 = mutableListOf<Button>()
//        val textViewList1 = mutableListOf<TextView>()

//        val view1 = view.findViewById<View>(R.id.add_layout)
//        val layoutParams = view1.layoutParams
//        layoutParams.width = width1
//        layoutParams.height = height1
//        view1.layoutParams = layoutParams

//        val view2 = view.findViewById<View>(R.id.add_layout_1)
//        val layoutParams1 = view2.layoutParams
//        layoutParams1.width = width1
//        layoutParams1.height = height1
//        view2.layoutParams = layoutParams1

        val model = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        model.message.observe(viewLifecycleOwner, Observer {
            if (it == "0") {
                binding.scrollView.visibility = View.VISIBLE
            } else {
                binding.scrollView.visibility = View.INVISIBLE
            }

        })
        model.isDraft.observe(viewLifecycleOwner, Observer {
            if (it == 0) {
                val data = GetData(requireActivity()).finalRegister()
                if (data[0].product_tracking_code == "") {
                    binding.editText1Final.text =
                        Editable.Factory.getInstance().newEditable("کد ردیابی محصول ثبت نشده است!")
                } else {
                    binding.editText1Final.text = Editable.Factory.getInstance()
                        .newEditable(data[0].product_tracking_code.toString())
                }
                if (data[0].operator_name == "") {
                    binding.editText2Final.text =
                        Editable.Factory.getInstance().newEditable("نام اپراتور ثبت نشده است!")
                } else {
                    binding.editText2Final.text =
                        Editable.Factory.getInstance().newEditable(data[0].operator_name.toString())
                }
                if (data[0].production_count == 0) {
                    binding.editText3Final.text =
                        Editable.Factory.getInstance().newEditable("تعداد تولید ثبت نشده است!")
                } else {
                    binding.editText3Final.text = Editable.Factory.getInstance()
                        .newEditable(data[0].production_count.toString())
                }

//                val count =
//                    data.count { dataFinalRegister1 -> dataFinalRegister1.parameter_type == "inbound_tracking_code" }
//                val count1 =
//                    data.count { dataFinalRegister1 -> dataFinalRegister1.parameter_type == "non_conformity_code" }

                binding.inboundTrackingContainer.removeAllViews()
                binding.nonConformityContainer.removeAllViews()

                data.forEachIndexed { index, item ->
                    if (item.parameter_type == "inbound_tracking_code") {

                        val v = layoutInflater.inflate(
                            R.layout.form_code_layout,
                            binding.inboundTrackingContainer,
                            false
                        )
                        v.findViewById<AppCompatButton>(R.id.operator_button).visibility = View.GONE
                        v.findViewById<AppCompatEditText>(R.id.edit_text).run {
                            text =
                                Editable.Factory.getInstance().newEditable(item.parameter_value)
                            isEnabled = false
                        }

                        binding.inboundTrackingContainer.addView(v)

//                        entryTrackingCode(
//                            "add",
//                            requireContext(),
//                            binding,
//                            editTextList,
//                            buttonDeleteList,
//                            textViewList
//                        )
                    }
                    if (item.parameter_type == "non_conformity_code") {
                        val v = layoutInflater.inflate(
                            R.layout.form_code_layout,
                            binding.inboundTrackingContainer,
                            false
                        )
                        v.findViewById<AppCompatButton>(R.id.operator_button).visibility = View.GONE
                        v.findViewById<AppCompatEditText>(R.id.edit_text).run {
                            text =
                                Editable.Factory.getInstance().newEditable(item.parameter_value)
                            isEnabled = false
                        }

                        v.findViewById<TextView>(R.id.title).text = getString(R.string.Non_Code)
                        binding.inboundTrackingContainer.addView(v)

//                        nonComplianceCode(
//                            "add",
//                            requireContext(),
//                            binding,
//                            editTextList1,
//                            buttonDeleteList1,
//                            textViewList1
//                        )

                    }
//                    if (item.parameter_type == "inbound_tracking_code") {
//                        editTextList[sum].text =
//                            Editable.Factory.getInstance().newEditable(item.parameter_value)
//                        editTextList[sum].isEnabled = false
//                        buttonDeleteList[0].visibility = View.GONE
//                        buttonDeleteList[sum].visibility = View.GONE
//                        sum += 1
//                    } else if (item.parameter_type == "non_conformity_code") {
//                        buttonDeleteList1[0].visibility = View.GONE
//                        buttonDeleteList1[sum1].visibility = View.GONE
//                        editTextList1[sum1].text =
//                            Editable.Factory.getInstance().newEditable(item.parameter_value)
//                        editTextList1[sum1].isEnabled = false
//                        sum1 += 1
//                    }
                }


//                if (count == 0) {
//                    binding.editText4Final.text =
//                        Editable.Factory.getInstance().newEditable("کد ردیابی ورودی ثبت نشده است!")
////                    binding.editText4Final.layoutParams.width = 550
//
//                } else {
//                    binding.editText4Final.text = Editable.Factory.getInstance()
//                        .newEditable(data[0].parameter_value.toString())
//                }
//                if (count1 == 0) {
////                    binding.editText5Final.text =
//                    Editable.Factory.getInstance().newEditable("کد عدم انطباق ثبت نشده است!")
////                    binding.editText5Final.layoutParams.width = 550
//
//                } else {
////                    binding.editText5Final.text = Editable.Factory.getInstance()
////                        .newEditable(data[0].parameter_value.toString())
//                }
//                binding.editText4Final.visibility = View.INVISIBLE
//                binding.productionTrackingCode.visibility = View.INVISIBLE
//                binding.editText5Final.visibility = View.INVISIBLE
//                binding.nonComplianceCode.visibility = View.INVISIBLE
                when (data[0].shift) {
                    1 -> {
                        binding.radioShiftFirst.isChecked = true
                    }

                    2 -> {
                        binding.radioShiftSecond.isChecked = true
                    }

                    else -> {
                        binding.radioShiftThird.isChecked = true
                    }
                }
                var sum = 0
                var sum1 = 0



                binding.editText1Final.isFocusable = false
                binding.editText2Final.isFocusable = false
                binding.editText3Final.isFocusable = false
                binding.radioShiftFirst.isClickable = false
                binding.radioShiftSecond.isClickable = false
                binding.radioShiftThird.isClickable = false
                binding.editText4Final.isEnabled = false;
//                binding.editText5Final.isEnabled = false;

//                binding.addLayout.visibility = View.INVISIBLE
//                binding.addLayout1.visibility = View.INVISIBLE
                binding.btnFinal.visibility = View.INVISIBLE

                binding.btnFinal.setMargins(0, 0, 0, 0)
            } else {
                val model1 = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
                model1.message1.observe(viewLifecycleOwner, Observer {
                    binding.addLayoutInboundCode.setOnClickListener {

//                        entryTrackingCode(
//                            "add",
//                            requireContext(),
//                            binding,
//                            editTextList,
//                            buttonDeleteList,
//                            textViewList
//                        )
                        val v = layoutInflater.inflate(
                            R.layout.form_code_layout,
                            binding.inboundTrackingContainer,
                            false
                        )
                        v.findViewById<AppCompatButton>(R.id.operator_button).setOnClickListener {
                            binding.inboundTrackingContainer.removeView(v)
                        }
                        binding.inboundTrackingContainer.addView(v)
                    }
                    binding.addLayoutNonConformity.setOnClickListener {
//                        nonComplianceCode(
//                            "add",
//                            requireContext(),
//                            binding,
//                            editTextList1,
//                            buttonDeleteList1,
//                            textViewList1
//                        )
                        val v = layoutInflater.inflate(
                            R.layout.form_code_layout,
                            binding.nonConformityContainer,
                            false
                        )
                        v.findViewById<AppCompatButton>(R.id.operator_button).setOnClickListener {
                            binding.nonConformityContainer.removeView(v)
                        }
                        v.findViewById<TextView>(R.id.title).text = getString(R.string.Non_Code)
                        binding.nonConformityContainer.addView(v)
                    }

                    binding.btnFinal.setOnClickListener {
                        val listInboundTrackingCode1 = mutableListOf<String>()
                        val listNonConformityCode1 = mutableListOf<String>()
                        val productTrackingCode = binding.editText1Final.text.toString()
                        val operatorName = binding.editText2Final.text.toString()
                        var totalProduction = binding.editText3Final.text.toString()
//                        val inboundTrackingCode = binding.editText4Final.text.toString()
//                        val nonConformityCode = binding.editText5Final.text.toString()

                        val shift = when {
                            binding.radioShiftFirst.isChecked -> 1
                            binding.radioShiftSecond.isChecked -> 2
                            binding.radioShiftThird.isChecked -> 3
                            else -> 0
                        }
                        if (totalProduction == "") {
                            totalProduction = "0"
                        }
                        val currentTime = CurrentTime().time()
                        val dataInfo = DataFinalRegister(
                            productTrackingCode,
                            operatorName,
                            totalProduction.toInt(),
                            shift,
                            currentTime
                        )

                        binding.inboundTrackingContainer.children.forEach { view ->
                            Log.d("~~~~~~~~~~v", view.toString())
                            listInboundTrackingCode1.add(
                                view.findViewWithTag<AppCompatEditText>("input_code")?.text.toString()
                            )
                        }
                        binding.nonConformityContainer.children.forEach { view ->
                            Log.d("~~~~~~~~~~c", view.toString())
                            listNonConformityCode1.add(
                                view.findViewWithTag<AppCompatEditText>("input_code")?.text.toString()
                            )
                        }


//                        if (inboundTrackingCode != "") {
//                            listInboundTrackingCode1.add(0, inboundTrackingCode)
//                        }
//                        if (nonConformityCode != "") {
//                            listNonConformityCode1.add(0, nonConformityCode)
//                        }

//                        for (i in 0 until editTextList.size) {
//                            if (editTextList[i].text.toString() != "") {
//                                listInboundTrackingCode1.add(editTextList[i].text.toString())
//                            }
//                        }
//                        for (i in 0 until editTextList1.size) {
//                            if (editTextList1[i].text.toString() != "") {
//                                listNonConformityCode1.add(editTextList1[i].text.toString())
//                            }
//                        }
                        val sharedPreferences = SharedPreferences(requireContext())
                        val station = sharedPreferences.getString("csValueSelected", "")
                        val quality = sharedPreferences.getString("cpValueSelected", "")
                        val text =
                            "کاربر گرامی شما در حال ثبت نهایی گزارشات وارد شده برای ایستگاه کنترلی $station"
                        val text5 =
                            " و طرح کیفیت $quality می باشید. آیا از صحت گزارشات وارد شده اطمینان دارید؟ "
                        val text6 = "زیرا پس از تایید امکان ویرایش اطلاعات وجود ندارد!"

                        val color = ContextCompat.getColor(requireContext(), R.color.red)
                        val builder = SpannableStringBuilder()
                        val spannableString =
                            SpannableString().spannableString(text, station, color, null, null)
                        val spannableString1 =
                            SpannableString().spannableString(text5, quality, color, null, null)
                        val spannableString2 = SpannableString().spannableString(
                            text6, text6, color, 1.1f,
                            Typeface.DEFAULT_BOLD
                        )
                        builder.append(spannableString);
                        builder.append(spannableString1);
                        builder.append(spannableString2);
                        val alert =
                            Alert(requireActivity(), null, null, builder, "تایید", "لغو", "هشدار")

                        alert.setOnClick {
                            val check = SetData(requireActivity()).finalReport(dataInfo)
                            val check1 = SetData(requireActivity()).finalReport1(
                                listInboundTrackingCode1,
                                listNonConformityCode1,
                                sharedPreferences.getInt("idReports", 5)
                            )

                            if (check > 0 && check1 == 0) {
                                CustomToast(requireContext()).toastValid(
                                    null,
                                    "اطلاعات وارد گردیده با موفقیت ثبت شدند."
                                )
                                GoToOtherActivity(requireActivity()).mainActivity()
                            } else {
                                CustomToast(requireContext()).toastAlert(
                                    null,
                                    "عدم موفقیت در ثبت اطلاعات!"
                                )
                            }
                        }
                        alert.setOnClick1(View.OnClickListener {

                        })
                        alert.alert()
                    }
                })
            }
        })
//        val fontSizeBtn = Size(requireContext()).fontSize(0.034f)
//        val fontSizeTitle = Size(requireContext()).fontSize(0.034f)
//        val fontSizeTitle1 = Size(requireContext()).fontSize(0.032f)
//        val fontSizeContent = Size(requireContext()).fontSize(0.031f)

//        val title = view.findViewById<TextView>(R.id.title_final)
//        title.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle)
//        val codeProduct = view.findViewById<TextView>(R.id.code_product)
//        codeProduct.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle1)
//        val editTextFinal = view.findViewById<TextView>(R.id.edit_text_1_final)
//        editTextFinal.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeContent)
//        val operatorName = view.findViewById<TextView>(R.id.operator_name)
//        operatorName.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle1)
//        val editTextFinal1 = view.findViewById<TextView>(R.id.edit_text_2_final)
//        editTextFinal1.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeContent)
//        val totalProduction = view.findViewById<TextView>(R.id.total_production)
//        totalProduction.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle1)
//        val editTextFinal3 = view.findViewById<TextView>(R.id.edit_text_3_final)
//        editTextFinal3.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeContent)
//        val shift = view.findViewById<TextView>(R.id.shift)
//        shift.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle1)
//        val radioShiftFirst = view.findViewById<TextView>(R.id.radio_shift_first)
//        radioShiftFirst.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle1)
//        val radioShiftSecond = view.findViewById<TextView>(R.id.radio_shift_second)
//        radioShiftSecond.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle1)
//        val radioShiftThird = view.findViewById<TextView>(R.id.radio_shift_third)
//        radioShiftThird.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle1)
//        val productionTrackingCode =
//            view.findViewById<TextView>(R.id.production_tracking_code)
//        productionTrackingCode.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle1)
//        val editTextFinal4 = view.findViewById<TextView>(R.id.edit_text_4_final)
//        editTextFinal4.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeContent)
//        val nonComplianceCode = view.findViewById<TextView>(R.id.non_compliance_code)
//        nonComplianceCode.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle1)
//        val editTextFinal5 = view.findViewById<TextView>(R.id.edit_text_5_final)
//        editTextFinal5.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeContent)
//
//        val btn = view.findViewById<TextView>(R.id.btn_final)
//        btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeBtn)

    }

    override fun onResume() {
        super.onResume()
        //binding.root.requestLayout();
    }
}
//
//private fun entryTrackingCode(
//    status: String,
//    context: Context,
//    binding: FinalRegistrationBinding,
//    editTextList: MutableList<EditText>,
//    buttonDeleteList: MutableList<Button>,
//    textViewList: MutableList<TextView>
//) {
//
//    if (status == "add") {
//        val myEditText: EditText = EditText(context)
//        val myButtonDelete: Button = Button(context)
//        val myTextView: TextView = TextView(context)
//
//        myEditText.id = View.generateViewId()
//        myButtonDelete.id = View.generateViewId()
//
//        myTextView.id = View.generateViewId()
//
//        val typeface = ResourcesCompat.getFont(context, R.font.vazirmatn_bold)
//        myTextView.text = "کد ردیابی ورودی"
//        myTextView.typeface = typeface
//        val size = Size(context).fontSize(0.016f)
//        myTextView.textSize = size
//        myTextView.setTextColor(ContextCompat.getColor(context, R.color.black))
//        myButtonDelete.setBackgroundResource(R.drawable.remove_final_registr)
//
//        myEditText.setBackgroundResource(R.drawable.edit_text_info)
//
//        binding.constraintLayoutInnerFinal.addView(myEditText)
//        binding.constraintLayoutInnerFinal.addView(myButtonDelete)
//        binding.constraintLayoutInnerFinal.addView(myTextView)
//
//        editTextList.add(myEditText)
//        buttonDeleteList.add(myButtonDelete)
//        textViewList.add(myTextView)
//    }
//    val width1 = Size(context).calWidth(0.046f)
//    val height1 = Size(context).calHeight(0.031f)
//    val set = ConstraintSet()
//    set.constrainWidth(R.id.add_layout, width1)
//    set.constrainHeight(R.id.add_layout, height1)
//    if (editTextList.size == 0) {
//        set.connect(
//            R.id.add_layout,
//            ConstraintSet.TOP,
//            R.id.production_tracking_code,
//            ConstraintSet.TOP,
//            0
//        )
//        set.connect(
//            R.id.add_layout,
//            ConstraintSet.LEFT,
//            R.id.guidelineLeft9,
//            ConstraintSet.LEFT,
//            0
//        )
//        set.connect(
//            R.id.add_layout,
//            ConstraintSet.RIGHT,
//            R.id.edit_text_4_final,
//            ConstraintSet.LEFT,
//            16
//        )
//        set.connect(
//            R.id.add_layout,
//            ConstraintSet.BOTTOM,
//            R.id.production_tracking_code,
//            ConstraintSet.BOTTOM,
//            0
//        )
//        set.applyTo(binding.constraintLayoutInnerFinal)
//    }
//    val typeface1 = ResourcesCompat.getFont(context, R.font.vazirmatn_medium)
//    val filters = arrayOf<InputFilter>(InputFilter.LengthFilter(20)) // 10 is the maximum length
//    val width2 = Size(context).calWidth(0.049f)
//    val width3 = Size(context).calWidth(0.3f)
//    val height2 = Size(context).calHeight(0.037f)
//    val size1 = Size(context).fontSize(0.016f)
//    for (i in 0 until editTextList.size) {
//        buttonDeleteList[i].setOnClickListener {
//            (buttonDeleteList[i].parent as ViewGroup).removeView(buttonDeleteList[i])
//            (editTextList[i].parent as ViewGroup).removeView(editTextList[i])
//            (textViewList[i].parent as ViewGroup).removeView(textViewList[i])
//            editTextList.removeAt(i)
//            buttonDeleteList.removeAt(i)
//            textViewList.removeAt(i)
//            entryTrackingCode("", context, binding, editTextList, buttonDeleteList, textViewList)
//        }
//
//        set.constrainWidth(editTextList[i].id, width3)
//        set.constrainHeight(editTextList[i].id, ConstraintSet.WRAP_CONTENT)
//        editTextList[i].setTextColor(ContextCompat.getColor(context, R.color.black));
//        editTextList[i].inputType = InputType.TYPE_CLASS_NUMBER;
//
//        editTextList[i].textSize = size1
//        editTextList[i].typeface = typeface1
//        editTextList[i].filters = filters
//        editTextList[i].setPadding(10, 0, 0, 0)
//
//        set.constrainWidth(buttonDeleteList[i].id, width2)
//        set.constrainHeight(buttonDeleteList[i].id, height2)
//
//        set.constrainWidth(textViewList[i].id, ConstraintSet.WRAP_CONTENT)
//        set.constrainHeight(textViewList[i].id, ConstraintSet.WRAP_CONTENT)
//        if (i == 0) {
//            set.connect(
//                editTextList[i].id,
//                ConstraintSet.TOP,
//                R.id.edit_text_4_final,
//                ConstraintSet.BOTTOM,
//                50
//            )
//            set.connect(
//                buttonDeleteList[i].id,
//                ConstraintSet.TOP,
//                editTextList[i].id,
//                ConstraintSet.TOP,
//                0
//            )
//            set.connect(
//                buttonDeleteList[i].id,
//                ConstraintSet.BOTTOM,
//                editTextList[i].id,
//                ConstraintSet.BOTTOM,
//                0
//            )
//            set.connect(
//                R.id.add_layout,
//                ConstraintSet.TOP,
//                editTextList[i].id,
//                ConstraintSet.TOP,
//                0
//            )
//            set.connect(
//                R.id.add_layout,
//                ConstraintSet.BOTTOM,
//                editTextList[i].id,
//                ConstraintSet.BOTTOM,
//                0
//            )
//            set.connect(
//                buttonDeleteList[i].id,
//                ConstraintSet.LEFT,
//                R.id.guidelineLeft9,
//                ConstraintSet.LEFT,
//                0
//            )
//            set.connect(
//                buttonDeleteList[i].id,
//                ConstraintSet.RIGHT,
//                editTextList[i].id,
//                ConstraintSet.LEFT,
//                30
//            )
//        } else {
//            set.connect(
//                editTextList[0].id,
//                ConstraintSet.TOP,
//                R.id.edit_text_4_final,
//                ConstraintSet.BOTTOM,
//                40
//            )
//            set.connect(
//                editTextList[i].id,
//                ConstraintSet.TOP,
//                editTextList[i - 1].id,
//                ConstraintSet.BOTTOM,
//                50
//            )
//            set.connect(
//                buttonDeleteList[i].id,
//                ConstraintSet.TOP,
//                editTextList[i].id,
//                ConstraintSet.TOP,
//                0
//            )
//            set.connect(
//                buttonDeleteList[i].id,
//                ConstraintSet.BOTTOM,
//                editTextList[i].id,
//                ConstraintSet.BOTTOM,
//                0
//            )
//            set.connect(
//                buttonDeleteList[i].id,
//                ConstraintSet.LEFT,
//                R.id.guidelineLeft9,
//                ConstraintSet.LEFT,
//                0
//            )
//            set.connect(
//                buttonDeleteList[i].id,
//                ConstraintSet.RIGHT,
//                editTextList[i].id,
//                ConstraintSet.LEFT,
//                30
//            )
//        }
//        if (i == editTextList.size - 1) {
//            set.connect(
//                buttonDeleteList[i].id,
//                ConstraintSet.BOTTOM,
//                editTextList[i].id,
//                ConstraintSet.BOTTOM,
//                0
//            )
//            set.connect(
//                R.id.add_layout,
//                ConstraintSet.BOTTOM,
//                editTextList[i].id,
//                ConstraintSet.BOTTOM,
//                0
//            )
//            set.connect(
//                R.id.add_layout,
//                ConstraintSet.TOP,
//                editTextList[i].id,
//                ConstraintSet.TOP,
//                0
//            )
//
//            set.connect(
//                buttonDeleteList[i].id,
//                ConstraintSet.LEFT,
//                R.id.add_layout,
//                ConstraintSet.RIGHT,
//                0
//            )
//            set.connect(
//                buttonDeleteList[i].id,
//                ConstraintSet.RIGHT,
//                R.id.guidelineLeft5,
//                ConstraintSet.LEFT,
//                0
//            )
//            set.connect(
//                R.id.add_layout,
//                ConstraintSet.LEFT,
//                R.id.constraintLayout_inner_final,
//                ConstraintSet.LEFT,
//                0
//            )
//            set.connect(
//                R.id.add_layout,
//                ConstraintSet.RIGHT,
//                R.id.guidelineLeft4,
//                ConstraintSet.LEFT,
//                0
//            )
////            set.connect(
////                editTextList[i].id,
////                ConstraintSet.BOTTOM,
////                R.id.non_compliance_code,
////                ConstraintSet.TOP,
////                70
////            )
//        }
//        set.connect(
//            editTextList[i].id,
//            ConstraintSet.RIGHT,
//            R.id.guidelineLeft6,
//            ConstraintSet.LEFT,
//            0
//        )
//
//        set.connect(
//            textViewList[i].id,
//            ConstraintSet.RIGHT,
//            R.id.code_product,
//            ConstraintSet.RIGHT,
//            0
//        )
//        set.connect(
//            textViewList[i].id,
//            ConstraintSet.TOP,
//            editTextList[i].id,
//            ConstraintSet.TOP,
//            0
//        )
//        set.connect(
//            textViewList[i].id,
//            ConstraintSet.BOTTOM,
//            editTextList[i].id,
//            ConstraintSet.BOTTOM,
//            0
//        )
//    }
//    set.applyTo(binding.constraintLayoutInnerFinal)
//
//}

//
//private fun nonComplianceCode(
//    status: String,
//    context: Context,
//    binding: FinalRegistrationBinding,
//    editTextList1: MutableList<EditText>,
//    buttonDeleteList1: MutableList<Button>,
//    textViewList1: MutableList<TextView>
//) {
//
//    if (status == "add") {
//        val myEditText1: EditText = EditText(context)
//        val myButtonDelete1: Button = Button(context)
//        val myTextView1: TextView = TextView(context)
//
//        myEditText1.id = View.generateViewId()
//        myButtonDelete1.id = View.generateViewId()
//
//        myTextView1.id = View.generateViewId()
//        val size = Size(context).fontSize(0.016f)
//        val typeface = ResourcesCompat.getFont(context, R.font.vazirmatn_bold)
//        myTextView1.text = "کد عدم انطباق"
//        myTextView1.typeface = typeface
//        myTextView1.textSize = size
//        myTextView1.setTextColor(ContextCompat.getColor(context, R.color.black))
//        myButtonDelete1.setBackgroundResource(R.drawable.remove_final_registr)
//
//        myEditText1.setBackgroundResource(R.drawable.edit_text_info)
//
//        binding.constraintLayoutInnerFinal.addView(myEditText1)
//        binding.constraintLayoutInnerFinal.addView(myButtonDelete1)
//        binding.constraintLayoutInnerFinal.addView(myTextView1)
//
//        editTextList1.add(myEditText1)
//        buttonDeleteList1.add(myButtonDelete1)
//        textViewList1.add(myTextView1)
//    }
//    val width1 = Size(context).calWidth(0.046f)
//    val height1 = Size(context).calHeight(0.031f)
//    val set = ConstraintSet()
//    set.constrainWidth(R.id.add_layout_1, width1)
//    set.constrainHeight(R.id.add_layout_1, height1)
//    if (editTextList1.size == 0) {
//        set.connect(
//            R.id.add_layout_1,
//            ConstraintSet.TOP,
//            R.id.non_compliance_code,
//            ConstraintSet.TOP,
//            0
//        )
//        set.connect(
//            R.id.add_layout_1,
//            ConstraintSet.LEFT,
//            R.id.guidelineLeft9,
//            ConstraintSet.LEFT,
//            0
//        )
//        set.connect(
//            R.id.add_layout_1,
//            ConstraintSet.RIGHT,
//            R.id.edit_text_5_final,
//            ConstraintSet.LEFT,
//            16
//        )
//        set.connect(
//            R.id.add_layout_1,
//            ConstraintSet.BOTTOM,
//            R.id.non_compliance_code,
//            ConstraintSet.BOTTOM,
//            0
//        )
//        set.applyTo(binding.constraintLayoutInnerFinal)
//    }
//    val typeface1 = ResourcesCompat.getFont(context, R.font.vazirmatn_medium)
//    val filters = arrayOf<InputFilter>(InputFilter.LengthFilter(20)) // 10 is the maximum length
////    val width2 = Size(context).calWidth(0.049f)
////    val width3 = Size(context).calWidth(0.3f)
////    val height2 = Size(context).calHeight(0.037f)
////    val size1 = Size(context).fontSize(0.016f)
//
//    for (i in 0 until editTextList1.size) {
//        buttonDeleteList1[i].setOnClickListener {
//            (buttonDeleteList1[i].parent as ViewGroup).removeView(buttonDeleteList1[i])
//            (editTextList1[i].parent as ViewGroup).removeView(editTextList1[i])
//            (textViewList1[i].parent as ViewGroup).removeView(textViewList1[i])
//            editTextList1.removeAt(i)
//            buttonDeleteList1.removeAt(i)
//            textViewList1.removeAt(i)
//            nonComplianceCode("", context, binding, editTextList1, buttonDeleteList1, textViewList1)
//        }
//
////        set.constrainWidth(editTextList1[i].id, width3)
//        set.constrainHeight(editTextList1[i].id, ConstraintSet.WRAP_CONTENT)
//
//        editTextList1[i].setTextColor(ContextCompat.getColor(context, R.color.black));
//        editTextList1[i].inputType = InputType.TYPE_CLASS_NUMBER;
////        editTextList1[i].textSize = size1
//        editTextList1[i].typeface = typeface1
//        editTextList1[i].filters = filters
//        editTextList1[i].setPadding(10, 0, 0, 0)
//
////        set.constrainWidth(buttonDeleteList1[i].id, width2)
////        set.constrainHeight(buttonDeleteList1[i].id, height2)
//
//        set.constrainWidth(textViewList1[i].id, ConstraintSet.WRAP_CONTENT)
//        set.constrainHeight(textViewList1[i].id, ConstraintSet.WRAP_CONTENT)
//        if (i == 0) {
//            set.connect(
//                editTextList1[i].id,
//                ConstraintSet.TOP,
//                R.id.non_compliance_code,
//                ConstraintSet.BOTTOM,
//                -20
//            )
//
//            set.connect(
//                buttonDeleteList1[i].id,
//                ConstraintSet.TOP,
//                editTextList1[i].id,
//                ConstraintSet.TOP,
//                0
//            )
//            set.connect(
//                buttonDeleteList1[i].id,
//                ConstraintSet.BOTTOM,
//                editTextList1[i].id,
//                ConstraintSet.BOTTOM,
//                0
//            )
//            set.connect(
//                R.id.add_layout_1,
//                ConstraintSet.TOP,
//                editTextList1[i].id,
//                ConstraintSet.TOP,
//                0
//            )
//            set.connect(
//                R.id.add_layout_1,
//                ConstraintSet.BOTTOM,
//                editTextList1[i].id,
//                ConstraintSet.BOTTOM,
//                0
//            )
//            set.connect(
//                buttonDeleteList1[i].id,
//                ConstraintSet.LEFT,
//                R.id.guidelineLeft9,
//                ConstraintSet.LEFT,
//                0
//            )
//            set.connect(
//                buttonDeleteList1[i].id,
//                ConstraintSet.RIGHT,
//                editTextList1[i].id,
//                ConstraintSet.LEFT,
//                30
//            )
//
//        } else {
//            set.connect(
//                editTextList1[0].id,
//                ConstraintSet.TOP,
//                R.id.non_compliance_code,
//                ConstraintSet.BOTTOM,
//                45
//            )
//            set.connect(
//                editTextList1[i].id,
//                ConstraintSet.TOP,
//                editTextList1[i - 1].id,
//                ConstraintSet.BOTTOM,
//                32
//            )
//            set.connect(
//                buttonDeleteList1[i].id,
//                ConstraintSet.TOP,
//                editTextList1[i].id,
//                ConstraintSet.TOP,
//                0
//            )
//            set.connect(
//                buttonDeleteList1[i].id,
//                ConstraintSet.BOTTOM,
//                editTextList1[i].id,
//                ConstraintSet.BOTTOM,
//                0
//            )
//            set.connect(
//                buttonDeleteList1[i].id,
//                ConstraintSet.LEFT,
//                R.id.guidelineLeft9,
//                ConstraintSet.LEFT,
//                0
//            )
//            set.connect(
//                buttonDeleteList1[i].id,
//                ConstraintSet.RIGHT,
//                editTextList1[i].id,
//                ConstraintSet.LEFT,
//                30
//            )
//        }
//        if (i == editTextList1.size - 1) {
//
//            set.connect(
//                buttonDeleteList1[i].id,
//                ConstraintSet.BOTTOM,
//                editTextList1[i].id,
//                ConstraintSet.BOTTOM,
//                0
//            )
//            set.connect(
//                R.id.add_layout_1,
//                ConstraintSet.BOTTOM,
//                editTextList1[i].id,
//                ConstraintSet.BOTTOM,
//                0
//            )
//            set.connect(
//                R.id.add_layout_1,
//                ConstraintSet.TOP,
//                editTextList1[i].id,
//                ConstraintSet.TOP,
//                0
//            )
//
//            set.connect(
//                buttonDeleteList1[i].id,
//                ConstraintSet.LEFT,
//                R.id.add_layout_1,
//                ConstraintSet.RIGHT,
//                0
//            )
//            set.connect(
//                buttonDeleteList1[i].id,
//                ConstraintSet.RIGHT,
//                R.id.guidelineLeft5,
//                ConstraintSet.LEFT,
//                0
//            )
//            set.connect(
//                R.id.add_layout_1,
//                ConstraintSet.LEFT,
//                R.id.constraintLayout_inner_final,
//                ConstraintSet.LEFT,
//                0
//            )
//            set.connect(
//                R.id.add_layout_1,
//                ConstraintSet.RIGHT,
//                R.id.guidelineLeft4,
//                ConstraintSet.LEFT,
//                0
//            )
//            if (i == 0) {
//                set.connect(
//                    editTextList1[i].id,
//                    ConstraintSet.BOTTOM,
//                    R.id.btn_final,
//                    ConstraintSet.TOP,
//                    30
//                )
//            } else {
//                set.connect(
//                    editTextList1[i].id,
//                    ConstraintSet.BOTTOM,
//                    R.id.btn_final,
//                    ConstraintSet.TOP,
//                    90
//                )
//            }
//
//        }
//
//        set.connect(
//            editTextList1[i].id,
//            ConstraintSet.RIGHT,
//            R.id.guidelineLeft6,
//            ConstraintSet.LEFT,
//            0
//        )
//        set.connect(
//            textViewList1[i].id,
//            ConstraintSet.RIGHT,
//            R.id.non_compliance_code,
//            ConstraintSet.RIGHT,
//            0
//        )
//        set.connect(
//            textViewList1[i].id,
//            ConstraintSet.TOP,
//            editTextList1[i].id,
//            ConstraintSet.TOP,
//            0
//        )
//        set.connect(
//            textViewList1[i].id,
//            ConstraintSet.BOTTOM,
//            editTextList1[i].id,
//            ConstraintSet.BOTTOM,
//            0
//        )
//
//    }
//    set.applyTo(binding.constraintLayoutInnerFinal)
//
//}

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