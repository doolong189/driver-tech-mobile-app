package com.hoanglong180903.driver.ui.dashboard.order.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.hoanglong180903.driver.R
import com.hoanglong180903.driver.common.application.MyApplication
import com.hoanglong180903.driver.domain.enity.ErrorResponse
import com.hoanglong180903.driver.domain.enity.GetDetailOrderRequest
import com.hoanglong180903.driver.domain.enity.GetDetailOrderResponse
import com.hoanglong180903.driver.utils.Event
import com.hoanglong180903.driver.utils.Resource
import com.hoanglong180903.driver.utils.Utils
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException

class DetailOrderViewModel(private val application: Application) : AndroidViewModel(application) {

    private val getDetailOrderResult = MutableLiveData<Event<Resource<GetDetailOrderResponse>>>()

    fun getDetailOrderResult(): LiveData<Event<Resource<GetDetailOrderResponse>>> {
        return getDetailOrderResult
    }

}