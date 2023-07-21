package com.project.test.view.fragment


import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import kotlin.concurrent.thread


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
        model.showcase("HomeFragment")
        val sh = SharedPreferences(requireActivity())

        binding.username.text =
            "${sh.getString("fullName", "")}، به نرم افزار کنترل کیفیت خوش آمدید."
        binding.userRole.text =
            "${sh.getString("userTypeTitle", "")} - ${sh.getString("process_name", "")}"

        binding.inner1ConstraintLayout.visibility = View.GONE
        binding.noValuesNotActive.visibility = View.GONE
        binding.innerConstraintLayout.visibility = View.GONE
        binding.noValuesActive.visibility = View.GONE
        val thread = thread(true) {
            val countReportActive = GetData(requireActivity()).reportActive()
            if (countReportActive.size > 0) {
                val countReportActive1 = countReportActive[0]
                activity?.runOnUiThread{
                    binding.innerConstraintLayout.visibility = View.VISIBLE
                    binding.noValuesActive.visibility = View.GONE
                    binding.countReportText.text = countReportActive1.count.toString()
                    binding.userText.text = countReportActive1.user
                    binding.titleLastReportText.text = countReportActive1.csName
                    binding.titleLastReportCpText.text = countReportActive1.cpName
                    binding.timeLastReportText.text =
                        CurrentTime().date(countReportActive1.time).first
                }
            } else {
                activity?.runOnUiThread{
                    binding.innerConstraintLayout.visibility = View.GONE
                    binding.noValuesActive.visibility = View.VISIBLE
                }
            }
        }

        binding.viewActiveReports.setOnClickListener {
            NavigationApp(
                requireActivity(),
                parentFragmentManager,
                R.id.fragmentContainer
            ).navigationForward("ShowReportNotRegisteredFragment")
        }
        val thread1 = Thread() {
            val countReportNotActive = GetData(requireActivity()).reportNotActive()
            if (countReportNotActive.size > 0) {
                val countReportNotActive1 = countReportNotActive[0]
                activity?.runOnUiThread(){
                    binding.inner1ConstraintLayout.visibility = View.VISIBLE
                    binding.noValuesNotActive.visibility = View.GONE
                    binding.countReportNoActiveText.text = countReportNotActive1.count.toString()
                    binding.user1Text.text = countReportNotActive1.user
                    binding.titleLastReportNoActiveText.text = countReportNotActive1.csName
                    binding.lastCpNotActiveText.text = countReportNotActive1.cpName
                    binding.timeLastReportNoActiveText.text =
                        CurrentTime().date(countReportNotActive1.time).first
                }
            } else {
                activity?.runOnUiThread(){
                    binding.inner1ConstraintLayout.visibility = View.GONE
                    binding.noValuesNotActive.visibility = View.VISIBLE
                }

            }
        }.start()

        binding.viewActiveReportsNoActive.setOnClickListener {
            NavigationApp(
                requireActivity(),
                parentFragmentManager,
                R.id.fragmentContainer
            ).navigationForward("ShowReportRegisteredFragment")
        }
    }
}