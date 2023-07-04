package com.project.test.view.recyclerview

import android.app.Activity
import android.os.Environment
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.test.databinding.RecyclerDocBinding
import com.project.test.dataclass.DataDocument
import com.project.test.utils.FileHelperUtils
import java.io.File


class DocumentRecyclerViewAdapter(
    private val context: Activity, private val Doc: ArrayList<DataDocument>
) : RecyclerView.Adapter<DocumentRecyclerViewAdapter.DocumentViewHolder>() {

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
            binding.type.text = data.document_type
            if(data.description=="") {
                binding.description.text ="توضیحاتی ثبت نشده است"
            }else{
                binding.description.text = data.description
            }
            binding.root.setOnClickListener {
                var directory =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)

                directory = File("$directory/QualityControl/")

                val file = File(directory.toString() + "/" + data.attachment)

                FileHelperUtils.openFile(file, context)
            }
        }

    }
}