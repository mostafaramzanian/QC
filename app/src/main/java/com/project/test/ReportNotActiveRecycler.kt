package com.project.test

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
import com.project.test.databinding.RecyclerReportActiveBinding
import com.project.test.databinding.RecyclerReportFinalBinding

class ReportNotActiveRecycler(
    private val context: Activity,
    private val ViewModelStoreOwner: ViewModelStoreOwner,
    private val LifecycleOwner: LifecycleOwner,
    private val context1: Context,
    private val activity: AppCompatActivity,
    private val fragmentManager: FragmentManager,
    private val reports: ArrayList<DataReport>
) : RecyclerView.Adapter<ReportNotActiveRecycler.ProductViewHolder>() {
    private val reportsFull = ArrayList<DataReport>()
    private val reportMain = ArrayList<DataReport>()

    init {
        reportsFull.addAll(reports)
        reportMain.addAll(reports)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = RecyclerReportFinalBinding.inflate(context.layoutInflater, parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.setData(reports[position])
    }

    override fun getItemCount(): Int = reports.size

    inner class ProductViewHolder(
        private val binding: RecyclerReportFinalBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun setData(data: DataReport) {
            binding.txtTitle1.text = data.csName
            binding.txtControlStationName1.text = data.cpName
            binding.txtFirstTime1.text = data.createTime
            binding.txtLastTime1.text = data.lastChangeTime
            binding.txtStatus.text = data.Status

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

                FragmentReplacer(fragmentManager).replaceFragments(
                    ShowReportNotRegistered(),
                    DetailsReportNotActiveFragment(),
                    R.id.fragmentContainer
                )
            }
        }
    }
}