package com.project.test

import android.app.Activity
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.test.databinding.RecyclerDocBinding


class DocumentRecyclerAdapter (private val context: Activity,
                               private val Doc: ArrayList<DataDocument>
) : RecyclerView.Adapter<DocumentRecyclerAdapter.DocumentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        val binding = RecyclerDocBinding.inflate(context.layoutInflater, parent, false)
        return DocumentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        holder.setData(Doc[position])
    }

    override fun getItemCount(): Int = Doc.size

    inner class DocumentViewHolder(
        private val binding: RecyclerDocBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun setData(data: DataDocument) {
            binding.txtTitleDoc1.text = data.subject
            binding.codeDoc1.text = data.code

            val fontSizeBtn = Size(context).fontSize(0.032f)
            val fontSizeTitle = Size(context).fontSize(0.039f)
            val fontSizeContent = Size(context).fontSize(0.037f)

            binding.btnDoc.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeBtn)
            binding.txtTitleDoc.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle)
            binding.txtTitleDoc1.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeContent)

            binding.codeDoc.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle)
            binding.codeDoc1.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeContent)
        }

    }
}