package com.project.test.model

import android.app.Activity
import android.content.ContentValues
import com.project.test.dataclass.DataCpReports
import com.project.test.dataclass.DataFinalRegister
import com.project.test.dataclass.DataLab
import com.project.test.dataclass.SetDataInfo
import com.project.test.utils.SharedPreferences

class SetData(private val context: Activity) {
    private val cpId = SharedPreferences(context).getInt(
        "cpIdSelected",
        0
    )
    private val csId = SharedPreferences(context).getInt(
        "csIdSelected",
        0
    )
    fun information(data: SetDataInfo?, dataLab: DataLab?): Long {
        if (data != null) {
            val values1 = ContentValues().apply {
                put("report_id", data.reportId)
                put("parameter_id", data.parameterId)
                put("tool_id", data.toolId)
                put("report_value", data.reportValue)
                put("description", data.description)
                put("status", data.status)
                put("created_datetime", data.createdDatetime)
                put("report_order", data.reportOrder)
            }
            return Query(context).insertCpReportsParameters(values1)
        }
        if (dataLab != null) {
            val values1 = ContentValues().apply {
                put("report_id", dataLab.reportId)
                put("parameter_id", dataLab.parameterId)
                put("tool_id", dataLab.toolId)
                put("description", dataLab.description)
                put("created_datetime", dataLab.createdDatetime)
                put("report_order", dataLab.reportOrder)
                put("lab_request_code", dataLab.labRequestCode)
            }
            /*
            val j:JSONObject = JSONObject()
            j.put("","")

            val j2:JSONObject = JSONObject()
            j2.put("","")


            val ar: JSONArray = JSONArray()
            ar.put(j)
            ar.put(j2)


             */
            return Query(context).insertCpReportsParameters(values1)
        }
        return 0
    }

    fun information1(data: DataCpReports): Long {
        val values = ContentValues().apply {

            put("cp_id", cpId)
            put("station_id", csId)
            put("created_by_user", data.createdByUser)
            put("created_datetime", data.createdDateTime)
            put("is_draft", data.isDraft)
        }
        return Query(context).insertCpReports(values)
    }

    fun finalReport(data: DataFinalRegister): Int {
        val values = ContentValues().apply {
            put("product_tracking_code", data.product_tracking_code)
            put("operator_name", data.operator_name)
            put("production_count", data.production_count)
            put("shift", data.shift)
            put("completed_datetime", data.completed_datetime)
            put("is_draft", 0)
        }
        return Query(context).updateCpReports(cpId,values)
    }
    fun finalReport1(data:MutableList<String>?, data1:MutableList<String>?,reportId:Int): Int {
        val values = ContentValues()
        if (data != null) {
            for (item in data) {
                values.put("cp_report_id", reportId)
                values.put("parameter_type", "inbound_tracking_code")
                values.put("parameter_value", item)
                if(Query(context).insertCpReportsInfo(values) == -1L) {
                    return -1
                }
            }
        }
        val values1 = ContentValues()
        if (data1 != null) {
            for (item in data1) {
                values1.put("cp_report_id", reportId)
                values1.put("parameter_type", "non_conformity_code")
                values1.put("parameter_value", item)
                if(Query(context).insertCpReportsInfo(values) == -1L) {
                    return -1
                }
            }
        }
        return 0
    }

}