package com.project.test.dataclass

data class DataInfo(
    val id: Int,
    val cpId: Int,
    val name: String,
    val standardType: String,
    val importanceLevel: String,
    val measurementUnit: String,
    val toolType: Int,
    val acceptableRangeType: String,
    val acceptableRangeTarget: Float,
    val acceptableRangeMin: Float,
    val acceptableRangeMax: Float,
    val acceptableRangeOtherValue: String,
    val sampleAmount: String,
    val sampleUnit: String,
    val samplingInterval: Int,
    val isLab: Int,
)
