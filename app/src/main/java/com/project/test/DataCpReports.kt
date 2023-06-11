package com.project.test

data class DataCpReports(
    val cpId: Int,
    val stationId: Int,
    val createdByUser: Int,
    val createdDateTime: String,
    val productTrackingCode: String,
    val isDraft: Int,
    val completedDatetime: String,
    val shift: String,
    val operatorName: String,
    val productionCount: Int,
)
