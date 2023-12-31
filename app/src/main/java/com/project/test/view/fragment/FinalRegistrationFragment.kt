package com.project.test.view.fragment


import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.view.children
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
import com.vicmikhailau.maskededittext.MaskedFormatter
import com.vicmikhailau.maskededittext.MaskedWatcher


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
                        Editable.Factory.getInstance().newEditable("-")
                } else {
                    binding.editText1Final.text = Editable.Factory.getInstance()
                        .newEditable(data[0].product_tracking_code.toString())
                }
                if (data[0].operator_name == "") {
                    binding.editText2Final.text =
                        Editable.Factory.getInstance().newEditable("-")
                } else {
                    binding.editText2Final.text =
                        Editable.Factory.getInstance().newEditable(data[0].operator_name.toString())
                }
                if (data[0].production_count == 0) {
                    binding.editText3Final.text =
                        Editable.Factory.getInstance().newEditable("-")
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
                            R.layout.layout_form_code,
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
                            R.layout.layout_form_code,
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

                    }

                }

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

            } else {
                val model1 = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
                model1.message1.observe(viewLifecycleOwner, Observer {
                    binding.addLayoutInboundCode.setOnClickListener {

                        val v = layoutInflater.inflate(
                            R.layout.layout_form_code,
                            binding.inboundTrackingContainer,
                            false
                        )
                        v.findViewById<AppCompatButton>(R.id.operator_button).setOnClickListener {
                            binding.inboundTrackingContainer.removeView(v)
                        }
                        binding.inboundTrackingContainer.addView(v)
                    }
                    binding.addLayoutNonConformity.setOnClickListener {

                        val v = layoutInflater.inflate(
                            R.layout.layout_form_code,
                            binding.nonConformityContainer,
                            false
                        )
                        v.findViewById<AppCompatButton>(R.id.operator_button).setOnClickListener {
                            binding.nonConformityContainer.removeView(v)
                        }
                        v.findViewById<AppCompatEditText>(R.id.edit_text).run {
                            text =
                                Editable.Factory.getInstance().newEditable("F")
                            val formatter = MaskedFormatter("F##-###")
                            addTextChangedListener(MaskedWatcher(formatter, this))
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
                            val tmp =
                                view.findViewWithTag<AppCompatEditText>("input_code")?.text.toString()
                            if (tmp != "" || tmp.isNotEmpty()) {
                                listInboundTrackingCode1.add(tmp)
                            }
                        }
                        binding.nonConformityContainer.children.forEach { view ->
                            val tmp =
                                view.findViewWithTag<AppCompatEditText>("input_code")?.text.toString()
                            if (tmp != "" || tmp.isNotEmpty()) {
                                if (tmp != "F") {
                                    listNonConformityCode1.add(tmp)
                                }
                            }
                        }


                        val sharedPreferences = SharedPreferences(requireContext())
                        val station = sharedPreferences.getString("csValueSelected", "")
                        val quality = sharedPreferences.getString("cpValueSelected", "")
                        val text =
                            "کاربر گرامی شما در حال ثبت نهایی گزارشات وارد شده برای ایستگاه کنترلی $station"
                        val text5 =
                            " و طرح کیفیت $quality می باشید. آیا از صحت گزارشات وارد شده اطمینان دارید؟ "
                        val text6 = "زیرا پس از تایید امکان ویرایش اطلاعات وجود ندارد."

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
                            Alert(
                                requireActivity(),
                                null,
                                null,
                                builder,
                                "تایید",
                                "لغو",
                                "هشدار"
                            )

                        alert.setOnClick {
                            val check = SetData(requireActivity()).finalReport(dataInfo)
                            val check1 = SetData(requireActivity()).finalReport1(
                                listInboundTrackingCode1,
                                listNonConformityCode1,
                                sharedPreferences.getInt("idReports", 0)
                            )

                            if (check > 0 && check1 == 0) {
                                CustomToast(requireContext()).toastValid(
                                    null,
                                    "اطلاعات وارد گردیده با موفقیت ثبت شدند.", 15f, Gravity.CENTER
                                )
                                GoToOtherActivity(requireActivity()).mainActivity()
                            } else {
                                CustomToast(requireContext()).toastAlert(
                                    null,
                                    "عدم موفقیت در ثبت اطلاعات!", 15f, Gravity.CENTER
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

    }

    override fun onResume() {
        super.onResume()
        //binding.root.requestLayout();
    }
}
