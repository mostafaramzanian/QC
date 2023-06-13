package com.project.test.view.fragment


import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.project.test.R
import com.project.test.databinding.ShowMoreFormBinding
import com.project.test.utils.SharedViewModel
import com.project.test.utils.Size
import com.project.test.view.adapter.ShowMoreFormAdapter


class ShowMoreFormFragment : Fragment() {
    private lateinit var binding: ShowMoreFormBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ShowMoreFormBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var test = ""
        val model = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        model.message1.observe(viewLifecycleOwner, Observer {
            test = it
            binding.viewPagerMore.setCurrentItem(1, false)
        })
        model.showHide("Show")
        val tabTitle = arrayOf("سایر گزارشات فعال", "گزارش فعال در حال تکمیل")
        binding.viewPagerMore.adapter =
            ShowMoreFormAdapter(tabTitle.size, childFragmentManager, lifecycle)

        binding.viewPagerMore.isUserInputEnabled = false

        val fontSize = Size(requireContext()).fontSize(0.036f)
        val fontSize1 = Size(requireContext()).fontSize(0.030f)
        val width1 = Size(requireContext()).calWidth(0.045f)
        val height1 = Size(requireContext()).calHeight(0.03f)
        val width2 = Size(requireContext()).calWidth(0.05f)
        val height2 = Size(requireContext()).calHeight(0.03f)
        TabLayoutMediator(binding.tabLayoutMore, binding.viewPagerMore) { tab, position ->
            when (position) {
                0 -> {
                    val count1 = 90
                    tab.setCustomView(R.layout.custom_view)
                    val tvTabText = tab.customView!!.findViewById<TextView>(R.id.text_tab_1)
                    tvTabText.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
                    val counter1 = tab.customView!!.findViewById<TextView>(R.id.counter1)
                    counter1.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize1)
                    if (count1 < 100) {
                        tab.customView?.findViewById<ImageView>(R.id.fabCounter1)
                            ?.size(width1, height1)
                        tab.customView?.findViewById<TextView>(R.id.counter1)?.text =
                            count1.toString()
                    } else {
                        tab.customView?.findViewById<ImageView>(R.id.fabCounter1)
                            ?.size(width2, height2)
                        tab.customView?.findViewById<TextView>(R.id.counter1)?.text = "99+"
                    }
                }

                1 -> {
                    tab.setCustomView(R.layout.custom_view_report_now)
                    val tvTabText2 = tab.customView!!.findViewById<TextView>(R.id.text_tab_2)
                    tvTabText2.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
                }
            }
        }.attach()

        binding.viewPagerMore.setCurrentItem(1, false)
        binding.tabLayoutMore.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

                when (tab?.position) {
                    0 -> {
                        /*
                                                tab.customView?.findViewById<Group>(R.id.groupTabLayout)?.visibility =
                                                    View.GONE

                         */
                        tab.customView?.findViewById<TextView>(R.id.text_tab_1)?.setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.TabActive)
                        )
                    }

                    1 -> {
                        tab.customView?.findViewById<TextView>(R.id.text_tab_2)?.setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.TabActive)
                        )
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        tab.customView?.findViewById<TextView>(R.id.text_tab_1)?.setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.TabNonActive)
                        )
                    }

                    1 -> {
                        tab.customView?.findViewById<TextView>(R.id.text_tab_2)?.setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.TabNonActive)
                        )
                    }
                }

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
}


private fun View.size(Width: Int, Height: Int) {
    layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
        width = Width
        height = Height
    }
}


