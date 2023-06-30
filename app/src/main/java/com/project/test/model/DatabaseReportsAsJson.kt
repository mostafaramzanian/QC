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

                rowCpReports["cp_reports_id"] =
                    cursorCpReports.getInt(cursorCpReports.getColumnIndexOrThrow("cp_reports_id"))
                rowCpReports["cp_id"] =
                    cursorCpReports.getInt(cursorCpReports.getColumnIndexOrThrow("cp_id"))
                rowCpReports["station_id"] =
                    cursorCpReports.getInt(cursorCpReports.getColumnIndexOrThrow("station_id"))
                rowCpReports["created_by_user"] =
                    cursorCpReports.getInt(cursorCpReports.getColumnIndexOrThrow("created_by_user"))
                rowCpReports["cp_reports_created_datetime"] =
                    cursorCpReports.getString(cursorCpReports.getColumnIndexOrThrow("cp_reports_created_datetime"))
                rowCpReports["product_tracking_code"] =
                    cursorCpReports.getString(cursorCpReports.getColumnIndexOrThrow("product_tracking_code"))
                rowCpReports["is_draft"] =
                    cursorCpReports.getInt(cursorCpReports.getColumnIndexOrThrow("is_draft"))
                rowCpReports["completed_datetime"] =
                    cursorCpReports.getString(cursorCpReports.getColumnIndexOrThrow("completed_datetime"))
                rowCpReports["shift"] =
                    cursorCpReports.getInt(cursorCpReports.getColumnIndexOrThrow("shift"))
                rowCpReports["operator_name"] =
                    cursorCpReports.getString(cursorCpReports.getColumnIndexOrThrow("operator_name"))
                rowCpReports["production_count"] =
                    cursorCpReports.getInt(cursorCpReports.getColumnIndexOrThrow("production_count"))

                tableCpReports.add(rowCpReports)
            } while (cursorCpReports.moveToNext())
        }


        val tableCpReportsInfo = mutableListOf<Map<String, Any?>>()
        // val cursorAllCpReportsInfo = Query(context).allCpReportsInfo()
        if (cursorCpReports.moveToFirst()) {
            do {
                val rowCpReportsInfo = mutableMapOf<String, Any?>()
                rowCpReportsInfo["cp_reports_info_id"] =
                    cursorCpReports.getInt(cursorCpReports.getColumnIndexOrThrow("cp_reports_info_id"))
                rowCpReportsInfo["cp_report_id"] =
                    cursorCpReports.getInt(cursorCpReports.getColumnIndexOrThrow("cp_report_id"))
                rowCpReportsInfo["parameter_type"] =
                    cursorCpReports.getString(cursorCpReports.getColumnIndexOrThrow("parameter_type"))
                rowCpReportsInfo["parameter_value"] =
                    cursorCpReports.getString(cursorCpReports.getColumnIndexOrThrow("parameter_value"))


                tableCpReportsInfo.add(rowCpReportsInfo)
            } while (cursorCpReports.moveToNext())
        }


        val tableCpReportsParameters = mutableListOf<Map<String, Any?>>()
        //val cursorAllCpReportsParameters = Query(context).allCpReportsParameters()
        if (cursorCpReports.moveToFirst()) {
            do {
                val rowAllCpReportsParameters = mutableMapOf<String, Any?>()
                rowAllCpReportsParameters["cp_reports_parameters_id"] =
                    cursorCpReports.getInt(cursorCpReports.getColumnIndexOrThrow("cp_reports_parameters_id"))
                rowAllCpReportsParameters["report_id"] =
                    cursorCpReports.getInt(cursorCpReports.getColumnIndexOrThrow("report_id"))
                rowAllCpReportsParameters["parameter_id"] =
                    cursorCpReports.getInt(cursorCpReports.getColumnIndexOrThrow("parameter_id"))
                rowAllCpReportsParameters["tool_id"] =
                    cursorCpReports.getInt(cursorCpReports.getColumnIndexOrThrow("tool_id"))
                rowAllCpReportsParameters["report_value"] =
                    cursorCpReports.getString(cursorCpReports.getColumnIndexOrThrow("report_value"))
                rowAllCpReportsParameters["attachment"] =
                    cursorCpReports.getString(cursorCpReports.getColumnIndexOrThrow("attachment"))
                rowAllCpReportsParameters["description"] =
                    cursorCpReports.getString(cursorCpReports.getColumnIndexOrThrow("description"))
                rowAllCpReportsParameters["status"] =
                    cursorCpReports.getInt(cursorCpReports.getColumnIndexOrThrow("status"))
                rowAllCpReportsParameters["cp_reports_parameters_created_datetime"] =
                    cursorCpReports.getString(cursorCpReports.getColumnIndexOrThrow("cp_reports_parameters_created_datetime"))
                rowAllCpReportsParameters["report_order"] =
                    cursorCpReports.getInt(cursorCpReports.getColumnIndexOrThrow("report_order"))
                rowAllCpReportsParameters["lab_request_code"] =
                    cursorCpReports.getInt(cursorCpReports.getColumnIndexOrThrow("lab_request_code"))

                tableCpReportsParameters.add(rowAllCpReportsParameters)
            } while (cursorCpReports.moveToNext())
        }

        val jsonQC = JSONObject()
        jsonQC.put("cp_reports", JSONArray(tableCpReports))
        jsonQC.put("cp_reports_info", JSONArray(tableCpReportsInfo))
        jsonQC.put("cp_reports_parameters", JSONArray(tableCpReportsParameters))

        return jsonQC
    }
}