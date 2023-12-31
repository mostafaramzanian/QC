package com.project.test.view.fragment

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.project.test.R
import com.project.test.databinding.HelpBinding
import com.project.test.utils.SharedPreferences
import com.project.test.utils.SharedViewModel
import kotlin.math.pow
import kotlin.math.roundToLong
import kotlin.math.sqrt


class HelpFragment : Fragment() {
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
//        val fontSize = Size(requireContext()).fontSize(0.033f)

//        val textView = view.findViewById<TextView>(R.id.title_help1)
//        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
//        val textView1 = view.findViewById<TextView>(R.id.text_info_report)
//        textView1.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
//        val textView2 = view.findViewById<TextView>(R.id.help_title)
//        textView2.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
//        val textView3 = view.findViewById<TextView>(R.id.text_help_report)
//        textView3.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)

        val sharedPreferences = SharedPreferences(requireContext())

        val model = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        model.message1.observe(viewLifecycleOwner, Observer {
            val station = sharedPreferences.getString("csValueSelected", "")
            val quality = sharedPreferences.getString("cpValueSelected", "")
            val product = sharedPreferences.getString("productName", "")
            val text1= "کاربر گرامی شما در حال تکمیل گزارش برای محصول $product"
            val text2= " در ایستگاه کنترلی $station "
            val text3= "و طرح کیفیت $quality می باشید."

            val color = ContextCompat.getColor(requireContext(), R.color.red)
            val builder = SpannableStringBuilder()
            val spannableString =
                com.project.test.utils.SpannableString()
                    .spannableString(text1, product, color, null, null)
            val spannableString1 =
                com.project.test.utils.SpannableString()
                    .spannableString(text2, station, color, null, null)
            val spannableString2 = com.project.test.utils.SpannableString().spannableString(
                text3, quality, color, null,
               null
            )
            builder.append(spannableString);
            builder.append(spannableString1);
            builder.append(spannableString2);

            binding.textInfoReport.text = builder

        })
    }


    override fun onResume() {
        super.onResume()
//        val dm = DisplayMetrics()
//        activity?.windowManager?.defaultDisplay?.getMetrics(dm)

// will either be DENSITY_LOW, DENSITY_MEDIUM or DENSITY_HIGH

// will either be DENSITY_LOW, DENSITY_MEDIUM or DENSITY_HIGH
     //   val dpiClassification = dm.densityDpi

// these will return the actual dpi horizontally and vertically

// these will return the actual dpi horizontally and vertically
//        val xDpi = dm.xdpi
//        val yDpi = dm.ydpi
//        val density = resources.displayMetrics.density
//        val densityDpi = resources.displayMetrics.densityDpi
//        val px = 35 * (densityDpi / 160)
//        val dp = 10.43 * (160 / yDpi)
//
//        val x = (dm.widthPixels / dm.xdpi).toDouble().pow(2.0)
//        val y = (dm.heightPixels / dm.ydpi).toDouble().pow(2.0)
//        var screenInches = sqrt(x + y)
//        screenInches = (screenInches * 10).roundToLong().toDouble() / 10

        //binding.root.requestLayout();

    }
}