package com.project.test.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.project.test.view.recyclerview.OtherRecyclerView

class OtherFormActiveFragment : Fragment() {
    private lateinit var binding: OtherFormActiveBinding
    private lateinit var adapter: OtherRecyclerView


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
            val data = GetData(requireActivity()).otherReports("notShowAllReports")
            //data.sortByDescending { it.lastChangeTime }
            adapter = OtherRecyclerView(
                requireActivity(),
                requireActivity(),
                requireActivity(),
                requireActivity(),
                requireActivity() as AppCompatActivity,
                parentFragmentManager,
                data as ArrayList<DataReport>
            )
            binding.recyclerViewOtherForm.layoutManager = LinearLayoutManager(
                requireActivity(), RecyclerView.VERTICAL, false
            )
            binding.recyclerViewOtherForm.adapter = adapter
        })
    }

}