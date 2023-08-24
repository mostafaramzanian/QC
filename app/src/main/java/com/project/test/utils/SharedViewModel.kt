package com.project.test.utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val message = MutableLiveData<String>()
    val message1 = MutableLiveData<String>()
    val show = MutableLiveData<String>()
    val fragment = MutableLiveData<String>()
    val spinner = MutableLiveData<String>()
    val sum = MutableLiveData<String>()
    val isDraft = MutableLiveData<Int>()
    val showcase = MutableLiveData<String>()
    val reportList = MutableLiveData<String>()

    val cpValueSelectedName = MutableLiveData<String>()
    val csIndexSelectedName = MutableLiveData<String>()
    val productName = MutableLiveData<String>()
    val csIndexSelectedID = MutableLiveData<Int>()
    val cpIndexSelectedID = MutableLiveData<Int>()
    val idReports = MutableLiveData<Int>()

    fun sendMessage(text: String) {
        message.value = text
    }

    fun sendMessage1(text: String) {
        message1.value = text
    }

    fun showHide(text: String) {
        show.value = text
    }

    fun showLastFragment(text: String) {
        fragment.value = text
    }

    fun sum(text: String) {
        sum.value = text
    }

    fun isDraft(text: Int) {
        isDraft.value = text
    }

    fun showcase(text: String) {
        showcase.value = text
    }
    fun reportList1(text: String) {
        reportList.value = text
    }
    fun insertInformationData(
        cpValueSelectedNameValue: String,
        csIndexSelectedNameValue: String,
        productNameValue: String,
        csIndexSelectedIDValue: Int,
        cpIndexSelectedIDValue: Int,
        idReportsValue: Int
    ) {
        csIndexSelectedID.value = csIndexSelectedIDValue
        csIndexSelectedName.value = csIndexSelectedNameValue
        cpValueSelectedName.value = cpValueSelectedNameValue
        cpIndexSelectedID.value = cpIndexSelectedIDValue
        productName.value = productNameValue
        idReports.value = idReportsValue

    }
}