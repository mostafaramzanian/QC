package com.project.test.view.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.project.test.R
import com.project.test.databinding.HomeFragmentBinding
import com.project.test.model.GetData
import com.project.test.utils.CurrentTime
import com.project.test.utils.NavigationApp
import com.project.test.utils.SharedPreferences
import com.project.test.utils.SharedViewModel


class HomeFragment : Fragment() {

    private lateinit var binding: HomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val model = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        model.showHide("Hide")
        val sh = SharedPreferences(requireActivity())

        binding.username.text =
            "${sh.getString("fullName", "")}، به نرم افزار کنترل کیفیت خوش آمدید."
        binding.userRole.text =
            "${sh.getString("userTypeTitle", "")} - ${sh.getString("process_name", "")}"


        val countReportActive = GetData(requireActivity()).reportActive()
        if (countReportActive.size > 0) {
            val countReportActive1 = countReportActive[0]
            binding.innerConstraintLayout.visibility = View.VISIBLE
            binding.noValuesActive.visibility = View.GONE

            //binding.inner4ConstraintLayout.visibility = View.GONE
            binding.countReportText.text = countReportActive1.count.toString()
            binding.userText.text = countReportActive1.user
            binding.titleLastReportText.text = countReportActive1.csName
            binding.titleLastReportCpText.text = countReportActive1.cpName
            binding.timeLastReportText.text = CurrentTime().date(countReportActive1.time).first
        } else {
            binding.innerConstraintLayout.visibility = View.GONE
            binding.noValuesActive.visibility = View.VISIBLE
//            binding.inner4ConstraintLayout.visibility = View.VISIBLE
        }
        binding.viewActiveReports.setOnClickListener {
            NavigationApp(
                requireActivity(),
                parentFragmentManager,
                R.id.fragmentContainer
            ).navigationForward("ShowReportNotRegisteredFragment")

//            FragmentReplacer(parentFragmentManager).replaceFragments(
//                HomeFragment(),
//                ShowReportNotRegisteredFragment(),
//                R.id.fragmentsContainer
//            )
        }

        val countReportNotActive = GetData(requireActivity()).reportNotActive()

        if (countReportNotActive.size > 0) {
            val countReportNotActive1 = countReportNotActive[0]
            binding.inner1ConstraintLayout.visibility = View.VISIBLE
            binding.noValuesNotActive.visibility = View.GONE

            binding.countReportNoActiveText.text = countReportNotActive1.count.toString()
            binding.user1Text.text = countReportNotActive1.user
            binding.titleLastReportNoActiveText.text = countReportNotActive1.csName
            binding.lastCpNotActiveText.text = countReportNotActive1.cpName
            binding.timeLastReportNoActiveText.text =
                CurrentTime().date(countReportNotActive1.time).first
        } else {
            binding.inner1ConstraintLayout.visibility = View.GONE
            binding.noValuesNotActive.visibility = View.VISIBLE
        }
        binding.viewActiveReportsNoActive.setOnClickListener {
            NavigationApp(
                requireActivity(),
                parentFragmentManager,
                R.id.fragmentContainer
            ).navigationForward("ShowReportRegisteredFragment")
            /*
            FragmentReplacer(parentFragmentManager).replaceFragments(
                HomeFragment(),
                ShowReportRegisteredFragment(),
                R.id.fragmentsContainer
            )


             */

        }


//        val fontSize = Size(requireContext()).fontSize(0.029f)
//        val width1 = Size(requireContext()).calWidth(0.04f)
//        val height1 = Size(requireContext()).calHeight(0.03f)
//
//
//        val textView = view.findViewById<TextView>(R.id.text_no_report)
//        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
//        val ssb =
//            SpannableStringBuilder("هیچ گزارشی ثبت نشده است. شما می توانید با استفاده از آیکن  گزارش جدید ثبت نمایید. ( به فلش نمایش داده شده در پایین توجه کنید )")
//        val image = ContextCompat.getDrawable(requireContext(), R.drawable.icon_add)
//        image?.setBounds(0, 0, width1, height1)
//        val span = image?.let { ImageSpan(it, ImageSpan.ALIGN_BASELINE) }
//        ssb.setSpan(span, 57, 58, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
//        textView.text = ssb


        /*


         */
    }


}