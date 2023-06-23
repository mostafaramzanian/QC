package com.project.test.dataclass

data class DataReport(
    val cpId:Int,
    val csId:Int,
    val idReports:Int,
    val csName:String,
    val cpName:String,
    val createTime:String,
    val lastChangeTime:String,
    val Status: String,
    val nameProduct:String,
    val sum: Int
)
