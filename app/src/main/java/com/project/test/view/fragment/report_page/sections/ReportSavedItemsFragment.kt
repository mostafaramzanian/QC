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
import com.project.test.view.recyclerview.ReportSavedItemsRecyclerViewAdapter


class ReportSavedItemsFragment : Fragment() {
    private lateinit var binding: InfoRegisterBinding
    private lateinit var adapter: ReportSavedItemsRecyclerViewAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = InfoRegisterBinding.inflate(inflater)
        val model = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        model.message.observe(viewLifecycleOwner) {
            if (it == "1") {
                binding.recyclerViewInfoRegister.visibility = View.VISIBLE
                showReport()
            } else {
                binding.recyclerViewInfoRegister.visibility = View.GONE
                binding.infoInnerLayout.visibility = View.GONE
                binding.titleDoc2.visibility = View.GONE
            }

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.infoInnerLayout.visibility = View.GONE
        binding.titleDoc2.visibility = View.GONE

        adapter = ReportSavedItemsRecyclerViewAdapter(
            requireActivity()
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

    private fun showReport() {
        val model = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        model.isDraft.observe(viewLifecycleOwner) {
            Thread {
                val dataInfoRegister = GetData(requireActivity(),requireActivity()).infoRegister(it)
                if (dataInfoRegister.size == 0) {
                    activity?.runOnUiThread {
                        binding.infoInnerLayout.visibility = View.VISIBLE
                        binding.titleDoc2.visibility = View.GONE
                    }
                } else {
                    activity?.runOnUiThread {
                        adapter.differ.submitList(dataInfoRegister)
                    }
                }
            }.start()
        }

    }

}

