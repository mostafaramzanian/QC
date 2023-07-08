package com.project.test.view.recyclerview

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.project.test.R
import com.project.test.databinding.RecyclerReportCardviewBinding

import com.project.test.dataclass.DataReport
import com.project.test.utils.CurrentTime
import com.project.test.utils.NavigationApp
import com.project.test.utils.SharedPreferences
import com.project.test.utils.SharedViewModel
import saman.zamani.persiandate.PersianDate
import java.text.SimpleDateFormat
import java.util.Locale

class ReportNotActiveRecyclerView(
    private val context: Activity,
    private val ViewModelStoreOwner: ViewModelStoreOwner,
    private val LifecycleOwner: LifecycleOwner,
    private val context1: Context,
    private val activity: AppCompatActivity,
    private val fragmentManager: FragmentManager,
    private val reports: ArrayList<DataReport>
) : RecyclerView.Adapter<ReportNotActiveRecyclerView.ProductViewHolder>() {
    private val reportsFull = ArrayList<DataReport>()
    private val reportMain = ArrayList<DataReport>()

    init {
        reportsFull.addAll(reports)
        reportMain.addAll(reports)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = RecyclerReportCardviewBinding.inflate(context.layoutInflater, parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.setData(reports[position])
    }

    override fun getItemCount(): Int = reports.size

    inner class ProductViewHolder(
        private val binding: RecyclerReportCardviewBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun setData(data: DataReport) {

            binding.txtTitle1.text = data.csName
            binding.userText.text=data.user
            binding.txtControlStationName1.text = data.cpName
            binding.txtFirstTime1.text = CurrentTime().date(data.createTime).first
            binding.txtLastTime1.text = CurrentTime().date(data.lastChangeTime).first
//            binding.txtStatus.text = data.Status
            binding.root.setOnClickListener {
                val sharedPreferences = SharedPreferences(context)
                sharedPreferences.putString(
                    "cpValueSelected",
                    data.cpName
                )
                sharedPreferences.putString(
                    "csValueSelected",
                    data.csName
                )
                sharedPreferences.putInt(
                    "csIdSelected",
                    data.csId
                )
                sharedPreferences.putInt(
                    "cpIdSelected",
                    data.cpId
                )
                sharedPreferences.putInt(
                    "idReports",
                    data.idReports
                )
                val model: SharedViewModel =
                    ViewModelProvider(activity)[SharedViewModel::class.java]
                model.sendMessage1(data.cpId.toString())
                model.sum(data.sum.toString())
                model.isDraft(0)
                NavigationApp(
                    activity,
                    fragmentManager,
                    R.id.fragmentContainer
                ).navigationForward(
                   "DetailsReportNotActiveFragment",
                )
            }
        }
    }
}