package com.project.test.view.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.project.test.R
import com.project.test.databinding.ShowMoreFormBinding
import com.project.test.model.GetData
import com.project.test.utils.SharedViewModel
import com.project.test.view.adapter.ShowMoreFormAdapter
import kotlin.concurrent.thread


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
        thread(true) {
            val data = GetData(requireActivity()).otherReports("notShowAllReports")
            val count1 = data.size
            activity?.runOnUiThread {
                TabLayoutMediator(binding.tabLayoutMore, binding.viewPagerMore) { tab, position ->
                    when (position) {
                        0 -> {

                            tab.setCustomView(R.layout.custom_view)
                            if (count1 > 0) {
                                if (count1 < 100) {
                                    tab.customView?.findViewById<TextView>(R.id.counter)?.apply {
                                        text = count1.toString()
//                                textSize = 16f // Change the text size here
//                                size(50, 50)
//                                setMargins(-50, 0, 0, 50)
                                    }
                                } else {
//                        tab.customView?.findViewById<ImageView>(R.id.fabCounter1) ?.size(width2, height2)
                                    tab.customView?.findViewById<TextView>(R.id.counter)?.apply {
                                        text = "+99"
//                                textSize = 16f // Change the text size here
//                                size(60, 60)
//                                setMargins(-50, 0, 0, 50)
                                    }
                                }
                            } else {
                                tab.customView?.findViewById<TextView>(R.id.counter)?.visibility =
                                    View.GONE
                            }
                        }

                        1 -> {
                            val v =
                                layoutInflater.inflate(
                                    R.layout.custom_view,
                                    binding.tabLayoutMore,
                                    false
                                )
                            v.findViewById<AppCompatTextView>(R.id.counter).visibility = View.GONE
                            v.findViewById<TextView>(R.id.text_tab_1).text = "گزارش در حال تکمیل"
                            v.findViewById<TextView>(R.id.text_tab_1).setTextColor(
                                ContextCompat.getColor(requireContext(), R.color.TabActive)
                            )
                            tab.customView = v
                        }
                    }
                }.attach()
            }
        }
        binding.viewPagerMore.setCurrentItem(1, false)
        binding.tabLayoutMore.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<TextView>(R.id.text_tab_1)?.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.TabActive)
                )
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<TextView>(R.id.text_tab_1)?.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.TabNonActive)
                )
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


