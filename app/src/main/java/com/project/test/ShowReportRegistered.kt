package com.project.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.test.databinding.ShowReportRegisteredBinding

class ShowReportRegistered : Fragment() {

    private lateinit var binding: ShowReportRegisteredBinding
    private lateinit var adapter: ReportNotActiveRecyclerView
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
        val data = GetData(requireActivity()).showAllReportNotActive()

        adapter = ReportNotActiveRecyclerView(
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
