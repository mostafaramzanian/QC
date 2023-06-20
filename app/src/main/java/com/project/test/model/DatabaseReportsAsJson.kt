package com.project.test.model

import android.app.Activity
import org.json.JSONArray
import org.json.JSONObject

class DatabaseReportsAsJson(private val context: Activity) {

    fun getJson(): JSONObject {
        val tableCpReports = mutableListOf<Map<String, Any?>>()
        val cursorCpReports = Query(context).allCpReports()
        if (cursorCpReports.moveToFirst()) {
            do {
                val rowCpReports = mutableMapOf<String, Any?>()
                rowCpReports["id"] = cursorCpReports.getInt(0)
                rowCpReports["cp_id"] = cursorCpReports.getInt(1)
                rowCpReports["station_id"] = cursorCpReports.getInt(2)
                rowCpReports["created_by_user"] = cursorCpReports.getInt(3)
                rowCpReports["created_datetime"] = cursorCpReports.getString(4)
                rowCpReports["product_tracking_code"] = cursorCpReports.getString(5)
                rowCpReports["is_draft"] = cursorCpReports.getInt(6)
                rowCpReports["completed_datetime"] = cursorCpReports.getString(7)
                rowCpReports["shift"] = cursorCpReports.getInt(8)
                rowCpReports["operator_name"] = cursorCpReports.getString(9)
                rowCpReports["production_count"] = cursorCpReports.getInt(10)

                tableCpReports.add(rowCpReports)
            } while (cursorCpReports.moveToNext())
        }


        val tableCpReportsInfo = mutableListOf<Map<String, Any?>>()
        val cursorAllCpReportsInfo = Query(context).allCpReportsInfo()
        if (cursorAllCpReportsInfo.moveToFirst()) {
            do {
                val rowCpReportsInfo = mutableMapOf<String, Any?>()
                rowCpReportsInfo["id"] = cursorAllCpReportsInfo.getInt(0)
                rowCpReportsInfo["cp_report_id"] = cursorAllCpReportsInfo.getInt(1)
                rowCpReportsInfo["parameter_type"] = cursorAllCpReportsInfo.getString(2)
                rowCpReportsInfo["parameter_value"] = cursorAllCpReportsInfo.getString(3)

                tableCpReportsInfo.add(rowCpReportsInfo)

            } while (cursorAllCpReportsInfo.moveToNext())
        }


        val tableCpReportsParameters = mutableListOf<Map<String, Any?>>()
        val cursorAllCpReportsParameters = Query(context).allCpReportsParameters()
        if (cursorAllCpReportsParameters.moveToFirst()) {
            do {
                val rowAllCpReportsParameters = mutableMapOf<String, Any?>()
                rowAllCpReportsParameters["id"] = cursorAllCpReportsParameters.getInt(0)
                rowAllCpReportsParameters["report_id"] = cursorAllCpReportsParameters.getInt(1)
                rowAllCpReportsParameters["parameter_id"] = cursorAllCpReportsParameters.getInt(2)
                rowAllCpReportsParameters["tool_id"] = cursorAllCpReportsParameters.getInt(3)
                rowAllCpReportsParameters["report_value"] =
                    cursorAllCpReportsParameters.getString(4)
                rowAllCpReportsParameters["attachment"] = cursorAllCpReportsParameters.getString(5)
                rowAllCpReportsParameters["description"] = cursorAllCpReportsParameters.getString(6)
                rowAllCpReportsParameters["status"] = cursorAllCpReportsParameters.getInt(7)
                rowAllCpReportsParameters["created_datetime"] =
                    cursorAllCpReportsParameters.getString(8)
                rowAllCpReportsParameters["report_order"] = cursorAllCpReportsParameters.getInt(9)
                rowAllCpReportsParameters["lab_request_code"] =
                    cursorAllCpReportsParameters.getInt(10)

                tableCpReportsParameters.add(rowAllCpReportsParameters)
            } while (cursorAllCpReportsParameters.moveToNext())
        }

        val jsonQC = JSONObject()
        jsonQC.put("cp_reports", JSONArray(tableCpReports))
        jsonQC.put("cp_reports_info", JSONArray(tableCpReportsInfo))
        jsonQC.put("cp_reports_parameters", JSONArray(tableCpReportsParameters))

        return jsonQC
    }
}