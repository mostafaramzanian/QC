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
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.project.test.utils.Alert
import com.project.test.R
import com.project.test.utils.SharedPreferences
import com.project.test.utils.SharedViewModel
import com.project.test.view.adapter.ShowFormAdapter
import com.project.test.utils.Size
import com.project.test.utils.Stack
import com.project.test.databinding.ShowFormReportBinding
import com.project.test.model.GetData


class ShowFormReportFragment : Fragment() {
    private lateinit var binding: ShowFormReportBinding
    lateinit var model: SharedViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ShowFormReportBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentList = Stack()
        fragmentList.push(requireActivity(), R.id.fragmentContainer)
        model = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        model.message1.observe(viewLifecycleOwner, Observer {
            binding.viewPager.setCurrentItem(4, false)
        })

        val fontSize = Size(requireContext()).fontSize(0.029f)
        val fontSize1 = Size(requireContext()).fontSize(0.030f)
        val width1 = Size(requireContext()).calWidth(0.04f)
        val height1 = Size(requireContext()).calHeight(0.024f)
        val width2 = Size(requireContext()).calWidth(0.05f)
        val height2 = Size(requireContext()).calHeight(0.03f)
        //val tabTitle = arrayOf("راهنما","ثبت نهایی گزارش", "گزارش های ثبت شده", "ثبت گزارش", "اسناد و مدارک")
        binding.viewPager.adapter = ShowFormAdapter(5, parentFragmentManager, lifecycle)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.setCustomView(R.layout.custom_view_info_register)
                    val savedFinal = tab.customView!!.findViewById<TextView>(R.id.text_tab)
                    savedFinal.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
                }

                1 -> {
                    tab.setCustomView(R.layout.custom_view_1)
                    val counter1 = tab.customView!!.findViewById<TextView>(R.id.counter)
                    counter1.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize1)
                    val report = tab.customView!!.findViewById<TextView>(R.id.text_tab_report)
                    report.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
                    model.sum.observe(viewLifecycleOwner, Observer {
                        when (val count = it.toInt()) {
                            0 -> {
                                tab.customView?.findViewById<ImageView>(R.id.fabCounter)
                                    ?.visibility = View.GONE
                                tab.customView?.findViewById<TextView>(R.id.counter)?.visibility =
                                    View.GONE
                            }

                            in 1..99 -> {
                                tab.customView?.findViewById<ImageView>(R.id.fabCounter)
                                    ?.visibility = View.VISIBLE
                                tab.customView?.findViewById<TextView>(R.id.counter)?.visibility =
                                    View.VISIBLE
                                tab.customView?.findViewById<ImageView>(R.id.fabCounter)
                                    ?.size(width1, height1)
                                tab.customView?.findViewById<TextView>(R.id.counter)?.text =
                                    count.toString()

                            }

                            else -> {
                                tab.customView?.findViewById<ImageView>(R.id.fabCounter)
                                    ?.visibility = View.VISIBLE
                                tab.customView?.findViewById<TextView>(R.id.counter)?.visibility =
                                    View.VISIBLE
                                tab.customView?.findViewById<ImageView>(R.id.fabCounter)
                                    ?.size(width2, height2)
                                tab.customView?.findViewById<TextView>(R.id.counter)?.text = "99+"

                            }
                        }
                    })
                }

                2 -> {
                    tab.setCustomView(R.layout.custom_view_info)
                    val saved = tab.customView!!.findViewById<TextView>(R.id.text_tab_saved)
                    saved.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
                }

                3 -> {
                    tab.setCustomView(R.layout.custom_view_doc)
                    val doc = tab.customView!!.findViewById<TextView>(R.id.text_tab_doc)
                    doc.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
                }

                4 -> {
                    tab.setCustomView(R.layout.custom_view_help)
                    val help = tab.customView!!.findViewById<TextView>(R.id.text_tab_help)
                    help.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
                }
            }
        }.attach()

        binding.viewPager.setCurrentItem(4, false)

        model.sendMessage("4")
        var position = ""
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        val sharedPreferences = SharedPreferences(requireContext())
                        val sum = GetData(requireActivity()).count(
                            requireActivity(),
                            requireActivity(),
                            requireActivity(),
                            sharedPreferences.getInt("idReports", 5)
                        )

                        if (sum == 0) {
                            val textAlert =
                                "کاربر گرامی برای استفاده از این قسمت باید حداقل یک گزارش ثبت شده داشته باشید!"
                            val alert = Alert(
                                requireActivity(),
                                textAlert,
                                null,
                                null,
                                "متوجه شدم",
                                null,
                                "خطا"
                            )
                            alert.setOnClick(View.OnClickListener {
                                binding.tabLayout.setScrollPosition(2, 0f, true);
                                binding.viewPager.currentItem = 2;

                                position = "2"
                            })
                            alert.alert()
                        }
                        tab.customView?.findViewById<TextView>(R.id.text_tab)?.setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.TabActive)
                        )
                        position = tab.position.toString()
                    }

                    1 -> {
                        /*
                        tab.customView?.findViewById<Group>(R.id.groupTabLayout_1)?.visibility =
                            View.GONE

                         */
                        tab.customView?.findViewById<TextView>(R.id.text_tab_report)?.setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.TabActive)
                        )
                        model.sendMessage(tab.position.toString())
                        position = tab.position.toString()
                    }

                    2 -> {
                        tab.customView?.findViewById<TextView>(R.id.text_tab_saved)?.setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.TabActive)
                        )
                        model.sendMessage(tab.position.toString())
                        position = tab.position.toString()
                    }

                    3 -> {
                        tab.customView?.findViewById<TextView>(R.id.text_tab_doc)?.setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.TabActive)
                        )
                        model.sendMessage(tab.position.toString())
                        position = tab.position.toString()
                    }

                    4 -> {
                        tab.customView?.findViewById<TextView>(R.id.text_tab_help)?.setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.TabActive)
                        )
                        model.sendMessage(tab.position.toString())
                        position = tab.position.toString()
                    }

                    else -> {

                    }
                }
                model.sendMessage(position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        tab.customView?.findViewById<TextView>(R.id.text_tab)?.setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.TabNonActive)
                        )
                    }

                    1 -> {
                        tab.customView?.findViewById<TextView>(R.id.text_tab_report)?.setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.TabNonActive)
                        )
                    }

                    2 -> {
                        tab.customView?.findViewById<TextView>(R.id.text_tab_saved)?.setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.TabNonActive)
                        )
                    }

                    3 -> {
                        tab.customView?.findViewById<TextView>(R.id.text_tab_doc)?.setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.TabNonActive)
                        )
                    }

                    4 -> {
                        tab.customView?.findViewById<TextView>(R.id.text_tab_help)?.setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.TabNonActive)
                        )
                    }
                }

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })


        view.post {
            val wMeasureSpec =
                View.MeasureSpec.makeMeasureSpec(binding.viewPager.width, View.MeasureSpec.EXACTLY)
            val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            view.measure(wMeasureSpec, hMeasureSpec)
            val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)
            val layoutParams = viewPager.layoutParams
            layoutParams.height = view.measuredHeight
            viewPager.layoutParams = layoutParams
        }
    }

    override fun onPause() {
        super.onPause()
    }
}

private fun View.size(Width: Int, Height: Int) {
    layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
        width = Width
        height = Height
    }
}
