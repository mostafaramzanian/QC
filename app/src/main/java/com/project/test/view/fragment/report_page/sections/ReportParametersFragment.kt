package com.project.test.view.fragment.report_page.sections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.test.databinding.InfoRegisterBinding
import com.project.test.model.GetData
import com.project.test.utils.SharedViewModel
import com.project.test.view.recyclerview.ReportParametersRecyclerViewAdapter

class ReportParametersFragment : Fragment() {
    private lateinit var binding: InfoRegisterBinding
    private lateinit var adapter: ReportParametersRecyclerViewAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = InfoRegisterBinding.inflate(inflater)
        val model = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        model.message.observe(viewLifecycleOwner) {
            if (it == "2") {

//                val dataInfo = GetData(requireActivity()).information1()
//                adapter.differ.submitList(dataInfo)
                binding.recyclerViewInfoRegister.visibility = View.VISIBLE
            } else {
                binding.recyclerViewInfoRegister.visibility = View.GONE
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ReportParametersRecyclerViewAdapter(
            requireActivity(),
            requireActivity(),
            requireActivity(),
        )
        val dataInfo = GetData(requireActivity(),requireActivity()).information1()
        adapter.differ.submitList(dataInfo)
        binding.recyclerViewInfoRegister.layoutManager = LinearLayoutManager(
            requireActivity(), RecyclerView.VERTICAL, false
        )
        binding.recyclerViewInfoRegister.adapter = adapter
    }
}