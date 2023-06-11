package com.project.test


import android.app.Activity
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.project.test.databinding.RecyclerInforegisterBinding
import saman.zamani.persiandate.PersianDate
import java.text.SimpleDateFormat
import java.util.Locale


class InfoRegisterRecyclerAdapter(
    private val context: Activity,
    private val InfoRegister: ArrayList<DataInfoRegister>
) : RecyclerView.Adapter<InfoRegisterRecyclerAdapter.InfoRegisterViewHolder>() {

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
            val dateFormat = SimpleDateFormat("y-M-d H:m:s.S", Locale.US)
            val date = dateFormat.parse(data.created_datetime)
            val timeString = SimpleDateFormat("HH:mm:ss", Locale.US).format(date!!)
            val persianDate = PersianDate(date.time)

            val day = when (persianDate.shDay) {
                1 -> "01"
                2 -> "02"
                3 -> "03"
                4 -> "04"
                5 -> "05"
                6 -> "06"
                7 -> "07"
                8 -> "08"
                9 -> "09"
                else -> persianDate.shDay.toString()
            }
            val month = when (persianDate.shMonth) {
                1 -> "01"
                2 -> "02"
                3 -> "03"
                4 -> "04"
                5 -> "05"
                6 -> "06"
                7 -> "07"
                8 -> "08"
                9 -> "09"
                else -> persianDate.shMonth.toString()
            }

            val finalDate = persianDate.dayName()
                .toString() + "  " + day + " / " + month + " / " + persianDate.shYear.toString()

            binding.txtTitleDoc1.text = data.name
            binding.showObservedValue.text = data.report_value
            binding.showDateRegister.text = finalDate
            binding.showTimeRegister.text = timeString
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

            val fontSizeBtn = Size(context).fontSize(0.037f)
            val fontSizeTitle = Size(context).fontSize(0.035f)
            val fontSizeContent = Size(context).fontSize(0.035f)

            binding.txtTitleDoc.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle)
            binding.txtTitleDoc1.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeContent)
            binding.observedValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle)
            binding.showObservedValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeContent)
            binding.status.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle)
            binding.showStatus.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeContent)
            binding.dateRegister.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle)
            binding.showDateRegister.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeContent)
            binding.timeRegister.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTitle)
            binding.showTimeRegister.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeContent)
            binding.infoSaved.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeBtn)
        }

    }
}