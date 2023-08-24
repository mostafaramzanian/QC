package com.project.test.view.fragment.report_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.test.databinding.OtherFormActiveBinding
import com.project.test.dataclass.DataReport
import com.project.test.model.GetData
import com.project.test.utils.SharedViewModel
import com.project.test.view.recyclerview.OtherActiveReportsRecyclerViewAdapter
import kotlin.concurrent.thread

class OtherActiveReportsPageFragment : Fragment() {
    private lateinit var binding: OtherFormActiveBinding
    private lateinit var adapter: OtherActiveReportsRecyclerViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = OtherFormActiveBinding.inflate(inflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val model = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        model.message1.observe(viewLifecycleOwner, Observer {
            thread(true) {
                val data = GetData(requireActivity(),requireActivity()).otherReports("notShowAllReports")
                if (data.size == 0) {
                    activity?.runOnUiThread {
                        binding.infoInnerLayout.visibility = View.VISIBLE
                        binding.recyclerViewOtherForm.visibility=View.GONE
                    }
                }else {
                    //data.sortByDescending { it.lastChangeTime }
                    adapter = OtherActiveReportsRecyclerViewAdapter(
                        requireActivity(),
                        requireActivity(),
                        requireActivity(),
                        requireActivity(),
                        requireActivity() as AppCompatActivity,
                        parentFragmentManager,
                        data as ArrayList<DataReport>
                    )
                    activity?.runOnUiThread {
                        binding.infoInnerLayout.visibility = View.GONE
                        binding.recyclerViewOtherForm.visibility=View.VISIBLE
                        binding.recyclerViewOtherForm.layoutManager = LinearLayoutManager(
                            requireActivity(), RecyclerView.VERTICAL, false
                        )
                        binding.recyclerViewOtherForm.adapter = adapter
                    }
                }
            }
        })
    }

}