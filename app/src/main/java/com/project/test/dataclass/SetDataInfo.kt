package com.project.test.dataclass

data class SetDataInfo(
    val reportId: Int,
    val parameterId: Int,
    val toolId: Int,
    val reportValue: String,
    val description: String,
    val status: Int,
    val createdDatetime: String,
    val reportOrder: Int,
)
