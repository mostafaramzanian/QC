package com.project.test.dataclass

data class DataLab(
    val reportId: Int,
    val parameterId: Int,
    val toolId: Int,
    val description: String,
    val createdDatetime: String,
    val reportOrder: Int,
    val labRequestCode: String,
)
