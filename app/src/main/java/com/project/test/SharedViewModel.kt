package com.project.test

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val message = MutableLiveData<String>()
    val message1 = MutableLiveData<String>()
    val show = MutableLiveData<String>()
    val fragment = MutableLiveData<String>()
    val spinner = MutableLiveData<String>()
    val spinnerInfo = MutableLiveData<String>()
    val sum = MutableLiveData<String>()
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

    fun statusViewSpinner(text: String) {
        spinner.value = text
    }

    fun statusSpinnerInfo(text: String) {
        spinnerInfo.value = text
    }

    fun sum(text: String) {
        sum.value = text
    }

}