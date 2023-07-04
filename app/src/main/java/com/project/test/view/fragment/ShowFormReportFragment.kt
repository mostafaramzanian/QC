package com.project.test.view.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getColorStateList
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.project.test.R
import com.project.test.databinding.ShowFormReportBinding
import com.project.test.model.GetData
import com.project.test.utils.Alert
import com.project.test.utils.SharedPreferences
import com.project.test.utils.SharedViewModel
import com.project.test.view.adapter.ShowFormAdapter


class ShowFormReportFragment : Fragment() {
    private lateinit var binding: ShowFormReportBinding
    lateinit var model: SharedViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = ShowFormReportBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        model.message1.observe(viewLifecycleOwner, Observer {
            binding.viewPager.setCurrentItem(4, false)
        })

        val tabTitle = arrayOf(
            "ثبت نهایی گزارش", "گزارشات ثبت شده", "ثبت گزارش", "اسناد و مدارک", "راهنما"
        )
        val tabImages = arrayOf(
            R.drawable.register,
            R.drawable.result,
            R.drawable.form,
            R.drawable.doc,
            R.drawable.info,
        )
        binding.viewPager.offscreenPageLimit = 10

        binding.viewPager.adapter = ShowFormAdapter(5, childFragmentManager, lifecycle)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->

            val v = layoutInflater.inflate(
                R.layout.custom_report_tab_items, binding.tabLayout, false
            )
            v.findViewById<TextView>(R.id.text_tab).text = tabTitle[position]
            v.findViewById<ImageView>(R.id.icon_tab).setImageResource(tabImages[position])

            tab.customView = v


//            when (position) {
//                1 -> {
//                    tab.setCustomView(R.layout.custom_view_1)
//                    val counter1 = tab.customView!!.findViewById<TextView>(R.id.counter)
////                    counter1.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize1)
//                    val report = tab.customView!!.findViewById<TextView>(R.id.text_tab_report)
////                    report.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
//                    model.sum.observe(viewLifecycleOwner, Observer {
//                        when (val count = it.toInt()) {
//                            0 -> {
//                                tab.customView?.findViewById<ImageView>(R.id.fabCounter)
//                                    ?.visibility = View.GONE
//                                tab.customView?.findViewById<TextView>(R.id.counter)?.visibility =
//                                    View.GONE
//                            }
//
//                            in 1..99 -> {
//                                tab.customView?.findViewById<ImageView>(R.id.fabCounter)
//                                    ?.visibility = View.VISIBLE
//                                tab.customView?.findViewById<TextView>(R.id.counter)?.visibility =
//                                    View.VISIBLE
////                                tab.customView?.findViewById<ImageView>(R.id.fabCounter)
////                                    ?.size(width1, height1)
//                                tab.customView?.findViewById<TextView>(R.id.counter)?.text =
//                                    count.toString()
//
//                            }
//
//                            else -> {
//                                tab.customView?.findViewById<ImageView>(R.id.fabCounter)
//                                    ?.visibility = View.VISIBLE
//                                tab.customView?.findViewById<TextView>(R.id.counter)?.visibility =
//                                    View.VISIBLE
////                                tab.customView?.findViewById<ImageView>(R.id.fabCounter)
////                                    ?.size(width2, height2)
//                                tab.customView?.findViewById<TextView>(R.id.counter)?.text = "99+"
//
//                            }
//                        }
//                    })
//                }
//
        }.attach()

        binding.viewPager.setCurrentItem(4, false)

        model.sendMessage("4")
        var position = ""
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

                tab?.customView?.findViewById<TextView>(R.id.text_tab)?.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.TabActive)
                )
                tab?.customView?.findViewById<ImageView>(R.id.icon_tab)?.backgroundTintList =
                    getColorStateList(requireContext(), R.color.tab_icon_active)

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
                                requireActivity(), textAlert, null, null, "متوجه شدم", null, "خطا"
                            )
                            alert.setOnClick(View.OnClickListener {
                                binding.tabLayout.setScrollPosition(2, 0f, true)
                                binding.viewPager.currentItem = 2
                                position = "2"
                            })
                            alert.alert()
                        }
                        tab.customView?.findViewById<TextView>(R.id.text_tab)?.setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.TabActive)
                        )
                        position = tab.position.toString()
                    }
                }
                position = tab?.position.toString()
                model.sendMessage(position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

                tab?.customView?.findViewById<TextView>(R.id.text_tab)?.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.TabNonActive)
                )
                tab?.customView?.findViewById<ImageView>(R.id.icon_tab)?.backgroundTintList =
                    getColorStateList(requireContext(), R.color.tab_icon_inactive)

            }
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

}

