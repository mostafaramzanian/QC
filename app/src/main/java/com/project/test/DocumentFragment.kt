package com.project.test


import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.test.databinding.DocumentsBinding


class DocumentFragment: Fragment()  {
    private lateinit var binding :DocumentsBinding
    private lateinit var adapter: DocumentRecyclerAdapter



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val model = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        model.message.observe(viewLifecycleOwner, Observer {
            if(it=="3")
            {
                binding.constraintDoc.visibility=View.VISIBLE
            }else{
                binding.constraintDoc.visibility=View.INVISIBLE
            }

        })
        binding = DocumentsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fontSize = Size(requireContext()).fontSize(0.036f)

        val model = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        model.message1.observe(viewLifecycleOwner, Observer {
            val textView = view.findViewById<TextView>(R.id.title_doc2)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
            val data = GetData(requireActivity()).doc()
            adapter = DocumentRecyclerAdapter(requireActivity(), data as ArrayList<DataDocument>)
            binding.recyclerViewDoc.layoutManager = LinearLayoutManager(
                requireActivity(), RecyclerView.VERTICAL, false
            )
            binding.recyclerViewDoc.adapter = adapter
        })
    }

    override fun onResume() {
        super.onResume()
      // binding.root.requestLayout();

    }
}
