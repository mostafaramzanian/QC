package com.project.test.view.fragment.report_page.sections


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.test.databinding.FragmentReportInstructionsBinding
import com.project.test.dataclass.DataDocument
import com.project.test.model.GetData
import com.project.test.utils.SharedViewModel
import com.project.test.view.recyclerview.InstructionsRecyclerViewAdapter
import kotlin.concurrent.thread


class ReportInstructionsFragment : Fragment() {
    private lateinit var binding: FragmentReportInstructionsBinding
    private lateinit var adapter: InstructionsRecyclerViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val model = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        model.message.observe(viewLifecycleOwner, Observer {
            if (it == "3") {
                binding.constraintDoc.visibility = View.VISIBLE
            } else {
                binding.constraintDoc.visibility = View.INVISIBLE
            }

        })
        binding = FragmentReportInstructionsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val model = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        model.message1.observe(viewLifecycleOwner, Observer {
            thread(start = true) {
                val data = GetData(requireActivity(),requireActivity()).doc()
                if (data.size > 0) {
                    Handler(Looper.getMainLooper()).post {
                    binding.itemNotFound.visibility = View.GONE
                        adapter =
                            InstructionsRecyclerViewAdapter(
                                requireActivity(),
                                data as ArrayList<DataDocument>
                            )
                        binding.recyclerViewDoc.layoutManager = LinearLayoutManager(
                            requireActivity(), RecyclerView.VERTICAL, false
                        )
                        binding.recyclerViewDoc.adapter = adapter
                    }
                } else {
                    binding.itemNotFound.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        // binding.root.requestLayout();

    }
}