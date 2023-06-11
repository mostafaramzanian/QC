package com.project.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.test.databinding.InfoRegisterBinding


class InfoRegisterFragment : Fragment() {
    private lateinit var binding: InfoRegisterBinding
    private lateinit var adapter: InfoRegisterRecyclerAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = InfoRegisterBinding.inflate(inflater)
        val model = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        model.message.observe(viewLifecycleOwner, Observer {
            if (it == "1") {
                binding.constraintInfoRegister.visibility = View.VISIBLE
                showReport()
            } else {
                binding.constraintInfoRegister.visibility = View.INVISIBLE
            }

        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        //binding.root.requestLayout();
    }


    private fun showReport(){

        val dataInfoRegister = GetData(requireActivity()).infoRegister(1)

        adapter = InfoRegisterRecyclerAdapter(
            requireActivity(),
            dataInfoRegister as ArrayList<DataInfoRegister>
        )
        binding.recyclerViewInfoRegister.layoutManager = LinearLayoutManager(
            requireActivity(), RecyclerView.VERTICAL, false
        )

        binding.recyclerViewInfoRegister.adapter = adapter
    }
}

