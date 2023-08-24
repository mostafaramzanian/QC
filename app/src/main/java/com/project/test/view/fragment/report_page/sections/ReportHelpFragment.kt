package com.project.test.view.fragment.report_page.sections

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.project.test.R
import com.project.test.databinding.HelpBinding
import com.project.test.utils.SharedPreferences
import com.project.test.utils.SharedViewModel


class ReportHelpFragment : Fragment() {
    private lateinit var binding: HelpBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val model = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        model.message.observe(viewLifecycleOwner, Observer {
            if (it == "4") {
                binding.constraintHelp.visibility = View.VISIBLE
            } else {
                binding.constraintHelp.visibility = View.INVISIBLE
            }

        })
        binding = HelpBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val model = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        model.message1.observe(viewLifecycleOwner) {
            val station = model.cpValueSelectedName.value
            val quality = model.csIndexSelectedName.value
            val product = model.productName.value
            val text1 = "کاربر گرامی شما در حال تکمیل گزارش برای محصول $product"
            val text2 = " در ایستگاه کنترلی $station "
            val text3 = "و طرح کیفیت $quality می باشید."

            val color = ContextCompat.getColor(requireContext(), R.color.red)
            val builder = SpannableStringBuilder()
            val spannableString =
                com.project.test.utils.SpannableString()
                    .spannableString(text1, product, color, null, null)
            val spannableString1 =
                com.project.test.utils.SpannableString()
                    .spannableString(text2, station, color, null, null)
            val spannableString2 = com.project.test.utils.SpannableString()
                .spannableString(
                    text3, quality, color, null,
                    null
                )
            builder.append(spannableString);
            builder.append(spannableString1);
            builder.append(spannableString2);
            binding.textInfoReport.text = builder
        }
    }

    override fun onResume() {
        super.onResume()
    }
}