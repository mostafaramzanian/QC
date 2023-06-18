package com.project.test.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.test.databinding.InformationBinding
import com.project.test.dataclass.DataInfo
import com.project.test.model.GetData
import com.project.test.utils.SharedViewModel
import com.project.test.view.recyclerview.InformationRecyclerViewAdapter

class InformationFragment : Fragment() {
    private lateinit var binding: InformationBinding
    private lateinit var adapter: InformationRecyclerViewAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = InformationBinding.inflate(inflater)
        val model = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        model.message.observe(viewLifecycleOwner, Observer {
            if (it == "2") {
                binding.constraintInformation.visibility = View.VISIBLE
            } else {
                binding.constraintInformation.visibility = View.INVISIBLE
            }

        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val model = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        model.message1.observe(viewLifecycleOwner, Observer {
            val dataInfo = GetData(requireActivity()).information1()
            adapter = InformationRecyclerViewAdapter(
                requireActivity(),
                requireActivity(),
                requireActivity(),
                dataInfo as ArrayList<DataInfo>
            )
            binding.recyclerViewInfo.layoutManager = LinearLayoutManager(
                requireActivity(), RecyclerView.VERTICAL, false
            )
            binding.recyclerViewInfo.adapter = adapter
        })
    }


    override fun onResume() {
        super.onResume()
        // binding.root.requestLayout();

    }
}