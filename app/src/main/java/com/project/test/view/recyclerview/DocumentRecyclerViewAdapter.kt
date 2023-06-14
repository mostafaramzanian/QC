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
            binding.btnDoc.setOnClickListener {
                val appSpecificExternalStorageDirectory =
                    context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)

                val file =
                    File(appSpecificExternalStorageDirectory.toString() + "/instructions/" + data.attachment)

                FileHelperUtils.openFile(file, context)
            }

//            val fontSizeBtn = Size(context).fontSize(0.032f)
//            val fontSizeTitle = Size(context).fontSize(0.039f)
//            val fontSizeContent = Size(context).fontSize(0.037f)

//            binding.btnDoc.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeBtn)
//            binding.txtTitleDoc.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle)
//            binding.txtTitleDoc1.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeContent)
//
//            binding.codeDoc.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle)
//            binding.codeDoc1.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeContent)
        }

    }
}