package com.project.test.view.fragment




import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.project.test.utils.FragmentReplacer
import com.project.test.R
import com.project.test.utils.Size
import com.project.test.databinding.HomeFragmentBinding
import com.project.test.model.GetData


class HomeFragment : Fragment() {

    private lateinit var binding : HomeFragmentBinding

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
        val count= GetData(requireActivity()).homePage()
        if(count>0){
            binding.inner2ConstraintLayout.visibility=View.GONE
            binding.iconAlertNoReport.visibility=View.GONE
        }else{
            binding.inner2ConstraintLayout.visibility=View.VISIBLE
            binding.iconAlertNoReport.visibility=View.VISIBLE
        }
        val countReportActive = GetData(requireActivity()).reportActive()
        val countReportActive1 =countReportActive[0]
        if(countReportActive.size>0){
            binding.innerConstraintLayout.visibility=View.VISIBLE
            binding.countReportText.text=countReportActive1.count.toString()
            binding.titleLastReportText.text=countReportActive1.csName
            binding.titleLastReportCpText.text=countReportActive1.cpName
            binding.timeLastReportText.text=countReportActive1.time
        }else{
            binding.innerConstraintLayout.visibility=View.GONE
        }
        binding.viewActiveReports.setOnClickListener {
            FragmentReplacer(parentFragmentManager).replaceFragments(
                HomeFragment(),
                ShowReportNotRegisteredFragment(),
                R.id.fragmentContainer
            )
        }



        val countReportNotActive = GetData(requireActivity()).reportNotActive()
        val countReportNotActive1 =countReportNotActive[0]
        if(countReportNotActive.size>0){
            binding.inner1ConstraintLayout.visibility=View.VISIBLE
            binding.countReportNoActiveText.text=countReportNotActive1.count.toString()
            binding.titleLastReportNoActiveText.text=countReportNotActive1.csName
            binding.lastCpNotActiveText.text=countReportNotActive1.cpName
            binding.timeLastReportNoActiveText.text=countReportNotActive1.time
        }else{
            binding.innerConstraintLayout.visibility=View.GONE
        }
        binding.viewActiveReportsNoActive.setOnClickListener {

            FragmentReplacer(parentFragmentManager).replaceFragments(
                HomeFragment(),
                ShowReportRegisteredFragment(),
                R.id.fragmentContainer
            )
        }



        val fontSize = Size(requireContext()).fontSize(0.029f)
        val width1 = Size(requireContext()).calWidth(0.04f)
        val height1= Size(requireContext()).calHeight(0.03f)


            val textView = view.findViewById<TextView>(R.id.text_no_report)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
            val ssb =
                SpannableStringBuilder("هیچ گزارشی ثبت نشده است. شما می توانید با استفاده از آیکن  گزارش جدید ثبت نمایید. ( به فلش نمایش داده شده در پایین توجه کنید )")
            val image = ContextCompat.getDrawable(requireContext(), R.drawable.icon_add)
            image?.setBounds(0, 0, width1, height1)
            val span = image?.let { ImageSpan(it, ImageSpan.ALIGN_BASELINE) }
            ssb.setSpan(span, 57, 58, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            textView.text = ssb


/*


 */
    }


}