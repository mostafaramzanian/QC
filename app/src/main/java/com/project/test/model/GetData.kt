package com.project.test.model

import android.app.Activity
import android.database.Cursor
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.project.test.utils.CurrentTime
import com.project.test.dataclass.DataCpReports
import com.project.test.dataclass.DataDocument
import com.project.test.dataclass.DataFinalRegister1
import com.project.test.dataclass.DataInfo
import com.project.test.dataclass.DataInfoRegister
import com.project.test.dataclass.DataReport
import com.project.test.dataclass.DataReportActive
import com.project.test.dataclass.DataReportNotActive
import com.project.test.utils.SharedPreferences
import com.project.test.utils.SharedViewModel


class GetData(private val context: Activity) {
    private val sharedPreferences = SharedPreferences(context)
    private val cpId = sharedPreferences.getInt(
        "cpIdSelected",
        0
    )
    private val csId = sharedPreferences.getInt(
        "csIdSelected",
        0
    )
    private val csName = sharedPreferences.getString(
        "csValueSelected",
        ""
    )

    fun infoRegister(isDraft: Int): MutableList<DataInfoRegister> {
        val cpReports = Query(context).cpSelectReports1(cpId, isDraft)
        val dataInfoRegister = mutableListOf<DataInfoRegister>()
        if (cpReports.moveToFirst()) {
            val id =
                cpReports.getInt(cpReports.getColumnIndexOrThrow("id"))
            val cpReportsParameters = Query(context).cpReportsParameters(id)

            if (cpReportsParameters.moveToFirst()) {
                do {
                    val id1 =
                        cpReportsParameters.getInt(cpReportsParameters.getColumnIndexOrThrow("id"))
                    val reportId =
                        cpReportsParameters.getInt(cpReportsParameters.getColumnIndexOrThrow("report_id"))
                    val parameterId =
                        cpReportsParameters.getInt(cpReportsParameters.getColumnIndexOrThrow("parameter_id"))
                    val toolId =
                        cpReportsParameters.getInt(cpReportsParameters.getColumnIndexOrThrow("tool_id"))
                    val reportValue =
                        cpReportsParameters.getString(cpReportsParameters.getColumnIndexOrThrow("report_value"))
                    val attachment =
                        cpReportsParameters.getString(cpReportsParameters.getColumnIndexOrThrow("attachment"))
                    val description =
                        cpReportsParameters.getString(cpReportsParameters.getColumnIndexOrThrow("description"))
                    val status =
                        cpReportsParameters.getString(cpReportsParameters.getColumnIndexOrThrow("status"))
                    val createdDatetime =
                        cpReportsParameters.getString(cpReportsParameters.getColumnIndexOrThrow("created_datetime"))
                    val reportOrder =
                        cpReportsParameters.getInt(cpReportsParameters.getColumnIndexOrThrow("report_order"))
                    val labRequestCode =
                        cpReportsParameters.getString(cpReportsParameters.getColumnIndexOrThrow("lab_request_code"))

                    val idCpStandardParameters =
                        Query(context).cpStandardParametersSelected(parameterId)
                    var name: String? = null
                    if (idCpStandardParameters.moveToFirst()) {
                        name =
                            idCpStandardParameters.getString(
                                idCpStandardParameters.getColumnIndexOrThrow(
                                    "name"
                                )
                            )
                    }
                    val data = DataInfoRegister(
                        id1,
                        name!!,
                        reportId,
                        parameterId,
                        toolId,
                        reportValue,
                        attachment,
                        description,
                        status,
                        createdDatetime,
                        reportOrder,
                        labRequestCode,
                    )
                    dataInfoRegister.add(data)

                } while (cpReportsParameters.moveToNext())
            }
        }
        return dataInfoRegister
    }


    fun information1(): MutableList<DataInfo> {
        val cpStandardParameters = Query(context).cpStandardParameters(cpId)
        val dataInfo = mutableListOf<DataInfo>()
        if (cpStandardParameters.moveToFirst()) {
            do {
                val id =
                    cpStandardParameters.getInt(cpStandardParameters.getColumnIndexOrThrow("id"))
                val cpId =
                    cpStandardParameters.getInt(cpStandardParameters.getColumnIndexOrThrow("cp_id"))
                val name =
                    cpStandardParameters.getString(cpStandardParameters.getColumnIndexOrThrow("name"))
                val standardType =
                    cpStandardParameters.getString(cpStandardParameters.getColumnIndexOrThrow("standard_type"))
                val importanceLevel =
                    cpStandardParameters.getString(cpStandardParameters.getColumnIndexOrThrow("importance_level"))
                val measurementUnit =
                    cpStandardParameters.getString(cpStandardParameters.getColumnIndexOrThrow("measurement_unit"))
                val toolType =
                    cpStandardParameters.getInt(cpStandardParameters.getColumnIndexOrThrow("tool_type"))
                val acceptableRangeType =
                    cpStandardParameters.getString(cpStandardParameters.getColumnIndexOrThrow("acceptable_range_type"))
                val acceptableRangeTarget =
                    cpStandardParameters.getFloat(cpStandardParameters.getColumnIndexOrThrow("acceptable_range_target"))
                val acceptableRangeMin =
                    cpStandardParameters.getFloat(cpStandardParameters.getColumnIndexOrThrow("acceptable_range_min"))
                val acceptableRangeMax =
                    cpStandardParameters.getFloat(cpStandardParameters.getColumnIndexOrThrow("acceptable_range_max"))
                val acceptableRangeOtherValue =
                    cpStandardParameters.getString(cpStandardParameters.getColumnIndexOrThrow("acceptable_range_other_value"))
                val sampleAmount =
                    cpStandardParameters.getString(cpStandardParameters.getColumnIndexOrThrow("sample_amount"))
                val sampleUnit =
                    cpStandardParameters.getString(cpStandardParameters.getColumnIndexOrThrow("sample_unit"))
                val samplingInterval =
                    cpStandardParameters.getInt(cpStandardParameters.getColumnIndexOrThrow("sampling_interval"))
                val isLab =
                    cpStandardParameters.getInt(cpStandardParameters.getColumnIndexOrThrow("is_lab"))

                val data = DataInfo(
                    id,
                    cpId,
                    name,
                    standardType,
                    importanceLevel,
                    measurementUnit,
                    toolType,
                    acceptableRangeType,
                    acceptableRangeTarget,
                    acceptableRangeMin,
                    acceptableRangeMax,
                    acceptableRangeOtherValue,
                    sampleAmount,
                    sampleUnit,
                    samplingInterval,
                    isLab
                )
                dataInfo.add(data)
            } while (cpStandardParameters.moveToNext())
        }

        return dataInfo
    }

    fun information(): Triple<DataCpReports?, Int?, Int?> {
        var idCpReport1: Int? = null
        var reportOrder: Int? = null
        var dataCpReports: DataCpReports? = null
        val time = CurrentTime().time()
        val cpReports = Query(context).cpSelectReports(cpId)
        if (cpReports.count == 0) {
            dataCpReports = DataCpReports(
                sharedPreferences.getInt("cpIdSelected", 0),
                sharedPreferences.getInt("csIdSelected", 0),

                sharedPreferences.getInt("userId", 0),
                time,
                "null",
                1,
                "null",
                "null",
                "null",
                0
            )
            val statusSet1 = Query(context).getLastRecord()
            if (statusSet1.count == 0) {
                idCpReport1 = 1
            } else {
                if (statusSet1.moveToFirst()) {
                    val idCpReport = statusSet1.getInt(statusSet1.getColumnIndexOrThrow("id"))
                    idCpReport1 = idCpReport + 1
                }
            }
            reportOrder = 1
        } else {
            if (cpReports.moveToFirst()) {
                idCpReport1 = cpReports.getInt(cpReports.getColumnIndexOrThrow("id"))
                // Query(context).delete("cp_reports", "cp_id", cpId)
                val statusSet1 = Query(context).cpReportsSelectedParameters(idCpReport1)
                if (statusSet1.moveToFirst()) {
                    val reportOrder1 =
                        statusSet1.getInt(statusSet1.getColumnIndexOrThrow("report_order"))
                    reportOrder = reportOrder1 + 1
                }
            }
        }
        return Triple(dataCpReports, idCpReport1, reportOrder)
    }

    fun count(
        context: ViewModelStoreOwner,
        context1: LifecycleOwner,
        context2: Activity,
        reportId: Int
    ): Int {
        val cpReports = Query(context2).cpSelectReports(cpId)
        var sum = 0
        val model1 = ViewModelProvider(context)[SharedViewModel::class.java]
        if (cpReports.moveToFirst()) {
            val id =
                cpReports.getInt(cpReports.getColumnIndexOrThrow("id"))
            val cpReportsParameters = Query(context2).cpReportsParameters(id)
            sum = cpReportsParameters.count
            model1.sum(sum.toString())
            cpReportsParameters.close()
        } else {
            model1.sum("0")
        }
        cpReports.close()

        return sum

    }

    fun otherReports(status: String): MutableList<DataReport> {
        val cpReports: Cursor = if (status == "showAllReport") {
            Query(context).otherReportQuery1(cpId)
        } else {
            Query(context).otherReportQuery(cpId)
        }
        val dataInfo = mutableListOf<DataReport>()
        if (cpReports.moveToFirst()) {
            do {
                val cpReport =
                    cpReports.getInt(cpReports.getColumnIndexOrThrow("cp_report"))
                val cpId =
                    cpReports.getInt(cpReports.getColumnIndexOrThrow("cp_id"))
                val csId =
                    cpReports.getInt(cpReports.getColumnIndexOrThrow("station_id"))
                val cpName =
                    cpReports.getString(cpReports.getColumnIndexOrThrow("cp_code"))
                val csName1 =
                    cpReports.getString(cpReports.getColumnIndexOrThrow("name"))
                val createTime =
                    cpReports.getString(cpReports.getColumnIndexOrThrow("firstTime"))

                val cpReports1 = Query(context).cpReportsJoin(cpReport)
                val cpReportsParameters = Query(context).cpReportsParameters(cpReport)
                val sum = cpReportsParameters.count
                if (cpReports1.moveToFirst()) {
                    val lastTime =
                        cpReports1.getString(cpReports1.getColumnIndexOrThrow("lastTime"))
                    val data = DataReport(
                        cpId,
                        csId,
                        cpReport,
                        csName1,
                        cpName,
                        createTime,
                        lastTime,
                        "فعال",
                        sum
                    )
                    dataInfo.add(data)
                }
            } while (cpReports.moveToNext())
        }

        return dataInfo
    }

    fun doc(): MutableList<DataDocument> {
        val cursor = Query(context).documents(cpId)
        val dataInfo = mutableListOf<DataDocument>()
        if (cursor.moveToFirst()) {
            do {
                val subject =
                    cursor.getString(cursor.getColumnIndexOrThrow("title"))
                val code =
                    cursor.getString(cursor.getColumnIndexOrThrow("document_code"))
                val attachment =
                    cursor.getString(cursor.getColumnIndexOrThrow("attachment"))
                val description =
                    cursor.getString(cursor.getColumnIndexOrThrow("description"))
                val documentType =
                    cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val data = DataDocument(
                    subject,
                    code,
                    attachment,
                    description,
                    documentType,
                )
                dataInfo.add(data)
            } while (cursor.moveToNext())
        }
        return dataInfo
    }

    fun insertReportFragment(cpIndexSelected: Int): Int {
        var id = 0
        val cpReports = Query(context).cpSelectReports(
            cpIndexSelected
        )
        if (cpReports.moveToFirst()) {
            id = cpReports.getInt(cpReports.getColumnIndexOrThrow("id"))
        }
        return id
    }


    fun homePage(): Int {
        val cpReports = Query(context).count(
            "cp_reports"
        )
        var count = 0
        if (cpReports.moveToFirst()) {
            count = cpReports.getInt(0)
            cpReports.close()
        }

        return count
    }

    fun reportActive(): MutableList<DataReportActive> {
        val cursor = Query(context).reportActive()
        val dataInfo = mutableListOf<DataReportActive>()
        var count = 0
        val cursor1 = Query(context).count2(1)
        cursor1.moveToFirst()
        count = cursor1.getInt(0)

        if (cursor.moveToFirst()) {
            val csName = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val cpName = cursor.getString(cursor.getColumnIndexOrThrow("cp_code"))
            val time = cursor.getString(cursor.getColumnIndexOrThrow("time"))

            val data = DataReportActive(
                count,
                csName,
                cpName,
                time
            )
            dataInfo.add(data)
        }
        return dataInfo
    }


    fun reportNotActive(): MutableList<DataReportNotActive> {
        val cursor = Query(context).reportNotActive()
        val dataInfo = mutableListOf<DataReportNotActive>()
        var count = 0
        val cursor1 = Query(context).count2(0)
        cursor1.moveToFirst()
        count = cursor1.getInt(0)

        if (cursor.moveToFirst()) {
            val csName = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val cpName = cursor.getString(cursor.getColumnIndexOrThrow("cp_code"))
            val time = cursor.getString(cursor.getColumnIndexOrThrow("time"))
            val data = DataReportNotActive(
                count,
                csName,
                cpName,
                time
            )
            dataInfo.add(data)
        }
        return dataInfo
    }


    fun showAllReportNotActive(): MutableList<DataReport> {

        val cpReports = Query(context).reportNotActive1()

        val dataInfo = mutableListOf<DataReport>()
        if (cpReports.moveToFirst()) {
            do {
                val cpReport =
                    cpReports.getInt(cpReports.getColumnIndexOrThrow("cp_report"))
                val cpId =
                    cpReports.getInt(cpReports.getColumnIndexOrThrow("cp_id"))
                val csId =
                    cpReports.getInt(cpReports.getColumnIndexOrThrow("station_id"))
                val cpName =
                    cpReports.getString(cpReports.getColumnIndexOrThrow("cp_code"))
                val csName1 =
                    cpReports.getString(cpReports.getColumnIndexOrThrow("name"))
                val createTime =
                    cpReports.getString(cpReports.getColumnIndexOrThrow("firstTime"))
                val lastTime =
                    cpReports.getString(cpReports.getColumnIndexOrThrow("completed_datetime"))
                val data = DataReport(
                    cpId,
                    csId,
                    cpReport,
                    csName1,
                    cpName,
                    createTime,
                    lastTime,
                    "غیر فعال",
                    0
                )
                dataInfo.add(data)
            } while (cpReports.moveToNext())
        }

        return dataInfo
    }

    fun finalRegister(): MutableList<DataFinalRegister1> {
        val cpReports = Query(context).finalRegister(cpId)
        val dataInfo = mutableListOf<DataFinalRegister1>()
        if (cpReports.moveToFirst()) {
            do {
                val productTrackingCode =
                    cpReports.getString(cpReports.getColumnIndexOrThrow("product_tracking_code"))
                val shift =
                    cpReports.getInt(cpReports.getColumnIndexOrThrow("shift"))
                val operatorName =
                    cpReports.getString(cpReports.getColumnIndexOrThrow("operator_name"))
                val productionCount =
                    cpReports.getInt(cpReports.getColumnIndexOrThrow("production_count"))
                val parameterType =
                    cpReports.getString(cpReports.getColumnIndexOrThrow("parameter_type"))
                val parameterValue =
                    cpReports.getString(cpReports.getColumnIndexOrThrow("parameter_value"))
                val data = DataFinalRegister1(
                    productTrackingCode,
                    operatorName,
                    productionCount,
                    shift,
                    parameterType,
                    parameterValue,
                )
                dataInfo.add(data)
            } while (cpReports.moveToNext())
        }
        return dataInfo
    }
}