package com.project.test.view.recyclerview


import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView
import com.project.test.R
import com.project.test.databinding.RecyclerInforegisterBinding
import com.project.test.dataclass.DataInfoRegister
import com.project.test.utils.CurrentTime
import saman.zamani.persiandate.PersianDate
import java.text.SimpleDateFormat
import java.util.Locale


class InfoRegisterRecyclerViewAdapter(
    private val context: Activity,
    private val InfoRegister: ArrayList<DataInfoRegister>
) : RecyclerView.Adapter<InfoRegisterRecyclerViewAdapter.InfoRegisterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoRegisterViewHolder {
        val binding = RecyclerInforegisterBinding.inflate(context.layoutInflater, parent, false)
        return InfoRegisterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InfoRegisterViewHolder, position: Int) {
        holder.setData(InfoRegister[position])
    }

    override fun getItemCount(): Int = InfoRegister.size

    inner class InfoRegisterViewHolder(
        private val binding: RecyclerInforegisterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun setData(data: DataInfoRegister) {

            val timeString = CurrentTime().date(data.created_datetime).second
            val finalDate = CurrentTime().date(data.created_datetime).third
            binding.userText.text = data.user
            if (data.report_value != "null") {
                binding.txtTitleDoc1.text = data.name
                binding.showObservedValue.text = data.report_value
                binding.showDateRegister.text = finalDate
              //  binding.showTimeRegister.text = timeString
            } else {
                binding.txtTitleDoc1.text = "آزمایشگاه"
                binding.observedValue.text = "کد درخواست آزمایشگاه: "
                binding.showObservedValue.text = data.lab_request_code
                binding.status.text = "تاریخ ثبت: "
                binding.showStatus.text = finalDate
                binding.dateRegister.text = "ساعت ثبت: "
                binding.showDateRegister.text = timeString
              //  binding.timeRegister.visibility = View.GONE
              //  binding.showTimeRegister.visibility = View.GONE


                binding.innerInfo.setBackgroundResource(R.drawable.background_lab)
                binding.titleInfo.setBackgroundResource(R.drawable.lab)

                val typeface = ResourcesCompat.getFont(context, R.font.vazirmatn_fd_regular)
                binding.showStatus.typeface = typeface
            }

            if (data.status == "1") {
                binding.showStatus.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.green_bold
                    )
                );
                binding.showStatus.text = "تایید"
            }
            if (data.status == "0") {
                binding.showStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
                binding.showStatus.text = "رد"
            }
        }

    }
}
