package com.project.test

data class DataInfoRegister(
    val id: Int,
    val name:String,
    val report_id: Int,
    val parameter_id: Int,
    val tool_id: Int,
    val report_value: String,
    val attachment: String?,
    val description: String?,
    val status: String?,
    val created_datetime: String,
    val report_order: Int,
    val lab_request_code: String?,

)
