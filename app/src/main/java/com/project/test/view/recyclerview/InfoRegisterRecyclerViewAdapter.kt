package com.project.test.view.recyclerview


import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.project.test.R
import com.project.test.databinding.RecyclerInforegisterBinding
import com.project.test.dataclass.DataInfoRegister
import com.project.test.utils.CurrentTime


class InfoRegisterRecyclerViewAdapter(
    private val context: Activity
) : RecyclerView.Adapter<InfoRegisterRecyclerViewAdapter.InfoRegisterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoRegisterViewHolder {
        val binding = RecyclerInforegisterBinding.inflate(context.layoutInflater, parent, false)
        return InfoRegisterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InfoRegisterViewHolder, position: Int) {
        holder.setData(differ.currentList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class InfoRegisterViewHolder(
        private val binding: RecyclerInforegisterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun setData(data: DataInfoRegister) {

            val timeString = CurrentTime().date(data.created_datetime).second
            val finalDate = CurrentTime().date(data.created_datetime).third
            binding.userText.text = data.user
            binding.txtTitleDoc1.text = data.name
            binding.showDateRegister.text = "$finalDate ساعت $timeString"

            if (data.report_value != "null") {
                binding.observedValue.text = "مقدار مشاهده شده:"
                binding.showObservedValue.text = data.report_value
                binding.status.visibility = View.VISIBLE
                binding.showStatus.visibility = View.VISIBLE
                binding.innerInfo.setBackgroundResource(R.drawable.background_info)
                binding.titleInfo.setBackgroundResource(R.drawable.background_title_info)

            } else {
                binding.observedValue.text = "کد درخواست آزمایشگاه: "
                binding.showObservedValue.text = data.lab_request_code
                binding.status.visibility = View.GONE
                binding.showStatus.visibility = View.GONE
                binding.innerInfo.setBackgroundResource(R.drawable.background_lab)
                binding.titleInfo.setBackgroundResource(R.drawable.lab)
                val typeface = ResourcesCompat.getFont(context, R.font.vazirmatn_fd_regular)
                binding.showStatus.typeface = typeface
            }

            if (data.status == "1") {
                binding.showStatus.setTextColor(
                    ContextCompat.getColor(context, R.color.green_bold)
                )
                binding.showStatus.text = "تایید"
            } else if (data.status == "0") {
                binding.showStatus.setTextColor(
                    ContextCompat.getColor(context, R.color.red)
                )
                binding.showStatus.text = "رد"
            }

        }

    }

    private val differCallback = object : DiffUtil.ItemCallback<DataInfoRegister>() {
        override fun areItemsTheSame(
            oldItem: DataInfoRegister,
            newItem: DataInfoRegister
        ): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: DataInfoRegister,
            newItem: DataInfoRegister
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
}
