package com.project.test.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.test.dataclass.DataReport
import com.project.test.view.recyclerview.ReportNotRegisterRecyclerView
import com.project.test.utils.SharedViewModel
import com.project.test.databinding.ShowReportRegisteredBinding
import com.project.test.model.GetData

class ShowReportNotRegisteredFragment: Fragment()  {

    private lateinit var binding : ShowReportRegisteredBinding
    private lateinit var adapter: ReportNotRegisterRecyclerView
    lateinit var model: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ShowReportRegisteredBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = GetData(requireActivity()).otherReports("showAllReport")

        adapter = ReportNotRegisterRecyclerView(
            requireActivity(),
            requireActivity(),
            requireActivity(),
            requireActivity(),
            requireActivity() as AppCompatActivity,
            parentFragmentManager,
            data as ArrayList<DataReport>
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(
            requireActivity(), RecyclerView.VERTICAL, false
        )
        binding.recyclerView.adapter = adapter

    }
}