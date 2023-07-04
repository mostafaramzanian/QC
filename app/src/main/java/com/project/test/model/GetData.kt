package com.project.test.model

import android.app.Activity
import android.database.Cursor
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.project.test.dataclass.DataCp
import com.project.test.dataclass.DataCpReports
import com.project.test.dataclass.DataDocument
import com.project.test.dataclass.DataFinalRegister1
import com.project.test.dataclass.DataInfo
import com.project.test.dataclass.DataInfoRegister
import com.project.test.dataclass.DataNode
import com.project.test.dataclass.DataReport
import com.project.test.dataclass.DataReportActive
import com.project.test.dataclass.DataReportNotActive
import com.project.test.dataclass.DataUser
import com.project.test.utils.CurrentTime
import com.project.test.utils.SharedPreferences
import com.project.test.utils.SharedViewModel


class GetData(private val context: Activity) {
    private val sharedPreferences = SharedPreferences(context)
    private val cpId = sharedPreferences.getInt(
        "cpIdSelected", 0
    )
    private val csId = sharedPreferences.getInt(
        "csIdSelected", 0
    )
    private val csName = sharedPreferences.getString(
        "csValueSelected", ""
    )

    fun controlStation(): MutableList<DataNode> {
        val processId = SharedPreferences(context).getInt("process_id", 0)
        val controlStation = Query(context).controlStation(processId)
        val nodes = mutableListOf<DataNode>()

        if (controlStation.moveToFirst()) {
            do {
                val name = controlStation.getString(controlStation.getColumnIndexOrThrow("name"))
                val parent =
                    controlStation.getString(controlStation.getColumnIndexOrThrow("parent"))
                val id = controlStation.getInt(controlStation.getColumnIndexOrThrow("id"))
                val node = DataNode(id, name, parent)
                val parentNode = nodes.find { it.id.toString() == parent }
                if (parentNode != null) {
                    parentNode.children += node
                    node.space = parentNode.space + 1
                    node.space1 = parentNode.space1 + 6
                }
                nodes += node

            } while (controlStation.moveToNext())
        } else {
            //CustomToast(context).toastAlert(null, "ایستگاه کنترلی یافت نشد!",null,null)
        }
        return (nodes)
    }

    fun findNode1(
        context: Activity, nods: MutableList<DataNode>, idSelect: String
    ): MutableList<DataCp> {

        val cp = Query(context).cp(idSelect)
        val listName = mutableListOf<DataCp>()
        val listId = mutableListOf<Int>()
        if (cp.moveToFirst()) {
            do {
                val cpCode = cp.getString(cp.getColumnIndexOrThrow("cp_code"))
                val selectCp = Query(context).selectFromCp(cpCode)
                if (selectCp.moveToFirst()) {
                    val cpName = selectCp.getString(selectCp.getColumnIndexOrThrow("cp_code"))
                    val cpId = selectCp.getInt(selectCp.getColumnIndexOrThrow("cpId"))
                    val product = selectCp.getString(selectCp.getColumnIndexOrThrow("name"))
                    val cpAccess =
                        selectCp.getString(selectCp.getColumnIndexOrThrow("access_user_group"))
                    val list = cpAccess.replace("'", "").split(",")
                        .map { it.trim().replace("[", "").replace("]", "") }
                    // val index = list.indexOf( SharedPreferences(context).getString("userType", ""))
                    val index = list.indexOf("MASTER_WORKER")
                    if (index != -1 && cpName != null) {
                        val data = DataCp(cpId, cpName, product)
                        listName.add(data)
                        // listId.add(cpId)
                    }
                }
            } while (cp.moveToNext())
        }
        return (listName)

    }


    fun infoRegister(isDraft: Int): MutableList<DataInfoRegister> {

        val cpReports = Query(context).cpSelectReports1(cpId, isDraft)
        val dataInfoRegister = mutableListOf<DataInfoRegister>()
        if (cpReports.moveToFirst()) {
            val id =
                cpReports.getInt(cpReports.getColumnIndexOrThrow("cp_reports_id"))
            val firstname =
                cpReports.getString(cpReports.getColumnIndexOrThrow("firstname"))
            val lastname =
                cpReports.getString(cpReports.getColumnIndexOrThrow("lastname"))
            val name1 = "$firstname $lastname"
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
                        name = idCpStandardParameters.getString(
                            idCpStandardParameters.getColumnIndexOrThrow(
                                "name"
                            )
                        )
                    }
                    try {
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
                            name1
                        )
                        dataInfoRegister.add(data)
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }


                } while (cpReportsParameters.moveToNext())
                cpReportsParameters.close()
            }
        }
        cpReports.close()

        return dataInfoRegister

    }


    fun information1(): MutableList<DataInfo> {
        val cpStandardParameters = Query(context).cpStandardParameters(cpId)
        val dataInfo = mutableListOf<DataInfo>()
        try {
            if (cpStandardParameters.moveToFirst()) {
                do {
                    val data = DataInfo(
                        cpStandardParameters.getInt(cpStandardParameters.getColumnIndexOrThrow("id")),
                        cpStandardParameters.getInt(cpStandardParameters.getColumnIndexOrThrow("cp_id")),
                        cpStandardParameters.getString(cpStandardParameters.getColumnIndexOrThrow("name")),
                        cpStandardParameters.getString(cpStandardParameters.getColumnIndexOrThrow("standard_type")),
                        cpStandardParameters.getString(cpStandardParameters.getColumnIndexOrThrow("importance_level")),
                        cpStandardParameters.getString(cpStandardParameters.getColumnIndexOrThrow("measurement_unit")),
                        cpStandardParameters.getInt(cpStandardParameters.getColumnIndexOrThrow("tool_type")),
                        cpStandardParameters.getString(cpStandardParameters.getColumnIndexOrThrow("acceptable_range_type")),
                        cpStandardParameters.getFloat(cpStandardParameters.getColumnIndexOrThrow("acceptable_range_target")),
                        cpStandardParameters.getDouble(cpStandardParameters.getColumnIndexOrThrow("acceptable_range_min")),
                        cpStandardParameters.getDouble(cpStandardParameters.getColumnIndexOrThrow("acceptable_range_max")),
                        cpStandardParameters.getString(cpStandardParameters.getColumnIndexOrThrow("acceptable_range_other_value")),
                        cpStandardParameters.getString(cpStandardParameters.getColumnIndexOrThrow("sample_amount")),
                        cpStandardParameters.getString(cpStandardParameters.getColumnIndexOrThrow("sample_unit")),
                        cpStandardParameters.getInt(cpStandardParameters.getColumnIndexOrThrow("sampling_interval")),
                        cpStandardParameters.getInt(cpStandardParameters.getColumnIndexOrThrow("is_lab"))
                    )
                    dataInfo.add(data)
                } while (cpStandardParameters.moveToNext())
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        cpStandardParameters.close()
        return dataInfo
    }

    fun information(): Triple<DataCpReports?, Int?, Int?> {
        var idCpReport1: Int? = null
        var reportOrder: Int? = null
        var dataCpReports: DataCpReports? = null
        val time = CurrentTime().time()
        val cpReports = Query(context).cpSelectReports(cpId)
        if (cpReports.count == 0) {
            try {
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
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
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
            statusSet1.close()
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
                statusSet1.close()
            }
        }
        cpReports.close()

        return Triple(dataCpReports, idCpReport1, reportOrder)
    }

    fun count(
        context: ViewModelStoreOwner, context1: LifecycleOwner, context2: Activity, reportId: Int
    ): Int {
        val cpReports = Query(context2).cpSelectReports(cpId)
        var sum = 0
        val model1 = ViewModelProvider(context)[SharedViewModel::class.java]
        if (cpReports.moveToFirst()) {
            val id = cpReports.getInt(cpReports.getColumnIndexOrThrow("id"))
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
                val cpReport = cpReports.getInt(cpReports.getColumnIndexOrThrow("cp_report"))
                val productName =
                    cpReports.getString(cpReports.getColumnIndexOrThrow("productName"))
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
                val firstname =
                    cpReports.getString(cpReports.getColumnIndexOrThrow("firstname"))
                val lastname =
                    cpReports.getString(cpReports.getColumnIndexOrThrow("lastname"))

                val fullName = "$firstname $lastname"

                val cpReports1 = Query(context).cpReportsJoin(cpReport)
                val cpReportsParameters = Query(context).cpReportsParameters(cpReport)
                val sum = cpReportsParameters.count

                if (cpReports1.moveToFirst()) {
                    try {
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
                            productName,
                            sum,
                            fullName
                        )
                        dataInfo.add(data)
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }
                cpReports1.close()
                cpReportsParameters.close()
            } while (cpReports.moveToNext())
        }
        cpReports.close()
        return dataInfo
    }

    fun doc(): MutableList<DataDocument> {
        val cursor = Query(context).documents(cpId)
        val dataInfo = mutableListOf<DataDocument>()
        try {
            if (cursor.moveToFirst()) {
                do {
                    val data = DataDocument(
                        cursor.getString(cursor.getColumnIndexOrThrow("title")),
                        cursor.getString(cursor.getColumnIndexOrThrow("document_code")),
                        cursor.getString(cursor.getColumnIndexOrThrow("attachment")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name"))
                    )
                    dataInfo.add(data)

                } while (cursor.moveToNext())
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        cursor.close()
        return dataInfo
    }

    fun getUser(username: String): DataUser? {
        val cursor = Query(context).login(username)
        val dataUser: DataUser
        try {
            if (cursor.moveToFirst()) {

                dataUser = DataUser(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("code")),
                    cursor.getString(cursor.getColumnIndexOrThrow("username")),
                    cursor.getString(cursor.getColumnIndexOrThrow("passwd")),
                    cursor.getString(cursor.getColumnIndexOrThrow("firstname")),
                    cursor.getString(cursor.getColumnIndexOrThrow("lastname")),
                    cursor.getString(cursor.getColumnIndexOrThrow("user_type")),
                    cursor.getString(cursor.getColumnIndexOrThrow("access_level")),
                    cursor.getString(cursor.getColumnIndexOrThrow("title")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("process_id"))
                )
                cursor.close()
                return dataUser
            } else {
                return null
            }
        } catch (_: Exception) {
            return null
        }

    }

    fun insertReportFragment(cpIndexSelected: Int): Int {
        var id = 0
        val cpReports = Query(context).cpSelectReports(
            cpIndexSelected
        )
        if (cpReports.moveToFirst()) {
            id = cpReports.getInt(cpReports.getColumnIndexOrThrow("id"))
        }
        cpReports.close()
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
        cpReports.close()
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
            val firstname = cursor.getString(cursor.getColumnIndexOrThrow("firstname"))
            val lastname = cursor.getString(cursor.getColumnIndexOrThrow("lastname"))

            val fullName = "$firstname $lastname"
            try {
                val data = DataReportActive(
                    count,
                    csName,
                    cpName,
                    time,
                    fullName
                )
                dataInfo.add(data)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        cursor.close()
        cursor1.close()
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
            val firstname = cursor.getString(cursor.getColumnIndexOrThrow("firstname"))
            val lastname = cursor.getString(cursor.getColumnIndexOrThrow("lastname"))

            val fullName = "$firstname $lastname"
            try {
                val data = DataReportNotActive(
                    count,
                    csName,
                    cpName,
                    time,
                    fullName
                )
                dataInfo.add(data)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        cursor.close()
        cursor1.close()
        return dataInfo
    }


    fun showAllReportNotActive(): MutableList<DataReport> {

        val cpReports = Query(context).reportNotActive1()
        val dataInfo = mutableListOf<DataReport>()
        if (cpReports.moveToFirst()) {
            do {
                val cpReport = cpReports.getInt(cpReports.getColumnIndexOrThrow("cp_report"))
                val cpId = cpReports.getInt(cpReports.getColumnIndexOrThrow("cp_id"))
                val csId = cpReports.getInt(cpReports.getColumnIndexOrThrow("station_id"))
                val cpName = cpReports.getString(cpReports.getColumnIndexOrThrow("cp_code"))
                val csName1 = cpReports.getString(cpReports.getColumnIndexOrThrow("name"))
                val createTime = cpReports.getString(cpReports.getColumnIndexOrThrow("firstTime"))
                val lastTime =
                    cpReports.getString(cpReports.getColumnIndexOrThrow("completed_datetime"))
                val firstname =
                    cpReports.getString(cpReports.getColumnIndexOrThrow("firstname"))
                val lastname =
                    cpReports.getString(cpReports.getColumnIndexOrThrow("lastname"))

                val fullName = "$firstname $lastname"
                try {
                    val data = DataReport(
                        cpId,
                        csId,
                        cpReport,
                        csName1,
                        cpName,
                        createTime,
                        lastTime,
                        "غیر فعال",
                        "",
                        0,
                        fullName
                    )
                    dataInfo.add(data)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            } while (cpReports.moveToNext())
        }
        cpReports.close()
        return dataInfo
    }

    fun finalRegister(): MutableList<DataFinalRegister1> {
        val cpReports = Query(context).finalRegister(cpId)
        val dataInfo = mutableListOf<DataFinalRegister1>()
        if (cpReports.moveToFirst()) {
            do {
                val productTrackingCode =
                    cpReports.getString(cpReports.getColumnIndexOrThrow("product_tracking_code"))
                val shift = cpReports.getInt(cpReports.getColumnIndexOrThrow("shift"))
                val operatorName =
                    cpReports.getString(cpReports.getColumnIndexOrThrow("operator_name"))
                val productionCount =
                    cpReports.getInt(cpReports.getColumnIndexOrThrow("production_count"))
                val parameterType =
                    cpReports.getString(cpReports.getColumnIndexOrThrow("parameter_type"))
                val parameterValue =
                    cpReports.getString(cpReports.getColumnIndexOrThrow("parameter_value"))
                try {
                    val data = DataFinalRegister1(
                        productTrackingCode,
                        operatorName,
                        productionCount,
                        shift,
                        parameterType,
                        parameterValue,
                    )
                    dataInfo.add(data)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            } while (cpReports.moveToNext())
        }
        cpReports.close()
        return dataInfo
    }


    fun tools(toolType: Int): Triple<MutableList<Int>, MutableList<String>, MutableList<Double>> {
        val tools = Query(context).tools(
            toolType, sharedPreferences.getInt("process_id", 0)
        )
        val idTools = mutableListOf<Int>()
        val listTools = mutableListOf<String>()
        val correctionFactor = mutableListOf<Double>()

        if (tools.moveToFirst()) {
            do {
                idTools.add(tools.getInt(tools.getColumnIndexOrThrow("id")))
                listTools.add(tools.getString(tools.getColumnIndexOrThrow("title")))
                correctionFactor.add(tools.getDouble(tools.getColumnIndexOrThrow("correction_factor")))
            } while (tools.moveToNext())
        }
        idTools.add(0, 0)
        listTools.add(0, "انتخاب کنید")
        correctionFactor.add(0, 0.0)
        tools.close()
        return Triple(idTools, listTools, correctionFactor)
    }
}