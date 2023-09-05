package com.project.test.view.recyclerview

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
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

class ReportsListRecyclerView(
    private val context: Activity,
    private val ViewModelStoreOwner: ViewModelStoreOwner,
    private val LifecycleOwner: LifecycleOwner,
    private val context1: Context,
    private val activity: AppCompatActivity,
    private val fragmentManager: FragmentManager,
    private val reports: ArrayList<DataReport>
) : RecyclerView.Adapter<ReportsListRecyclerView.ProductViewHolder>() {

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
            val model = ViewModelProvider(ViewModelStoreOwner)[SharedViewModel::class.java]
            model.reportList.observe(LifecycleOwner) {
                if (it == "InCompleteReportsFragment") {
                    binding.txtTitle1.text = data.csName
                    binding.userText.text = data.user
                    binding.txtControlStationName1.text = data.cpName
                    binding.txtFirstTime1.text = CurrentTime().date(data.createTime).first
                    binding.txtLastTime1.text = CurrentTime().date(data.lastChangeTime).first
//            binding.txtStatus.text = data.Status
                    binding.root.setOnClickListener {
                        noAllReports(data)
                    }
                } else if (it == "CompleteReportsFragment") {
                    binding.txtTitle1.text = data.csName
                    binding.userText.text = data.user
                    binding.txtControlStationName1.text = data.cpName
                    binding.txtFirstTime1.text = CurrentTime().date(data.createTime).first
                    binding.txtLastTime.text = "زمان ثبت نهایی گزارش"
                    binding.txtLastTime1.text = CurrentTime().date(data.lastChangeTime).first
//            binding.txtStatus.text = data.Status
                    binding.root.setOnClickListener {
                        val model1 =
                            ViewModelProvider(ViewModelStoreOwner)[SharedViewModel::class.java]
                        model1.insertInformationData(
                            data.cpName,
                            data.csName,
                            data.nameProduct,
                            data.csId,
                            data.cpId,
                            data.idReports
                        )

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
//                val sharedPreferences = SharedPreferences(context)
//                sharedPreferences.putInt(
//                    "cpIdSelected",
//                    data.cpId
//                )
            }
        }

    }

    private fun noAllReports(data: DataReport) {
        val model1 =
            ViewModelProvider(ViewModelStoreOwner)[SharedViewModel::class.java]
        model1.insertInformationData(
            data.cpName,
            data.csName,
            data.nameProduct,
            data.csId,
            data.cpId,
            data.idReports
        )
        val model: SharedViewModel = ViewModelProvider(activity)[SharedViewModel::class.java]
        model.sendMessage1(data.cpId.toString())
        model.sum(data.sum.toString())
        model.isDraft(1)
        NavigationApp(
            activity,
            fragmentManager,
            R.id.fragmentContainer
        ).navigationForward("ShowMoreFormFragment")

    }
}