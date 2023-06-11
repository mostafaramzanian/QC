package com.project.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.test.databinding.InfoRegisterBinding


class DetailsReportNotActiveFragment : Fragment() {
    private lateinit var binding: InfoRegisterBinding
    private lateinit var adapter: InfoRegisterRecyclerAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = InfoRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataInfoRegister = GetData(requireActivity()).infoRegister(0)

        adapter = InfoRegisterRecyclerAdapter(
            requireActivity(),
            dataInfoRegister as ArrayList<DataInfoRegister>
        )
        binding.recyclerViewInfoRegister.layoutManager = LinearLayoutManager(
            requireActivity(), RecyclerView.VERTICAL, false
        )

        binding.recyclerViewInfoRegister.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        //binding.root.requestLayout();
    }
}



