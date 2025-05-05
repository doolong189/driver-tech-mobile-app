package com.hoanglong180903.driver.ui.dashboard.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.util.Util
import com.google.gson.Gson
import com.hoanglong180903.driver.R
import com.hoanglong180903.driver.common.application.DriverApplication
import com.hoanglong180903.driver.data.enity.ErrorResponse
import com.hoanglong180903.driver.data.enity.GetShipperInfoRequest
import com.hoanglong180903.driver.data.enity.GetShipperInfoResponse
import com.hoanglong180903.driver.data.enity.RegisterAccountRequest
import com.hoanglong180903.driver.data.enity.RegisterAccountResponse
import com.hoanglong180903.driver.data.usecase.UserRepository
import com.hoanglong180903.driver.model.UserInfo
import com.hoanglong180903.driver.utils.Event
import com.hoanglong180903.driver.utils.Resource
import com.hoanglong180903.driver.utils.Utils
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException

class UserViewModel(private val app: Application) : AndroidViewModel(app) {
    private val repository  = UserRepository()
    var getShipperInfo : UserInfo? = null
    private val getShipperInfoResult = MutableLiveData<Event<Resource<GetShipperInfoResponse>>>()
    fun getShipperInfoResult(): LiveData<Event<Resource<GetShipperInfoResponse>>> {
        return getShipperInfoResult
    }

    private val registerAccountResult = MutableLiveData<Event<Resource<RegisterAccountResponse>>>()
    fun registerAccountResult() : LiveData<Event<Resource<RegisterAccountResponse>>>{
        return registerAccountResult
    }

    fun getShipperInfo(request : GetShipperInfoRequest) : Job = viewModelScope.launch {
        getShipperInfoResult.postValue(Event(Resource.Loading()))
        try {
            if (Utils.hasInternetConnection(getApplication<DriverApplication>())) {
                val response = repository.getShipperInfo(request)
                if (response.isSuccessful) {
                    response.body()?.let { resultResponse ->
                        getShipperInfoResult.postValue(Event(Resource.Success(resultResponse)))
                    }
                } else {
                    val errorResponse = response.errorBody()?.let {
                        val gson = Gson()
                        gson.fromJson(it.string(), ErrorResponse::class.java)
                    }
                    getShipperInfoResult.postValue(Event(Resource.Error(errorResponse?.message ?: "")))
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    getShipperInfoResult.postValue(Event(Resource.Error(getApplication<DriverApplication>().getString(
                        R.string.network_failure
                    ))))
                }
                else -> {
                    getShipperInfoResult.postValue(Event(Resource.Error(getApplication<DriverApplication>().getString(
                        R.string.conversion_error
                    ))))
                }
            }
        }
    }


    fun registerAccount(request : RegisterAccountRequest) : Job = viewModelScope.launch {
        registerAccountResult.postValue(Event(Resource.Loading()))
        try {
            if (Utils.hasInternetConnection(getApplication<DriverApplication>())){
                val response = repository.registerAccount(request)
                if (response.isSuccessful){
                    response.body()?.let { resultResponse ->
                        registerAccountResult.postValue(Event(Resource.Success(resultResponse)))
                    }
                }else{
                    val errorResponse = response.errorBody()?.let {
                        val gson = Gson()
                        gson.fromJson(it.string(), ErrorResponse::class.java)
                    }
                    registerAccountResult.postValue(Event(Resource.Error(errorResponse?.message ?: "")))
                }
            }
        }catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    registerAccountResult.postValue(Event(Resource.Error(getApplication<DriverApplication>().getString(
                        R.string.network_failure
                    ))))
                }
                else -> {
                    registerAccountResult.postValue(Event(Resource.Error(getApplication<DriverApplication>().getString(
                        R.string.conversion_error
                    ))))
                }
            }
        }
    }

    fun getConvertShipperInfo(response : UserInfo){
        getShipperInfo = UserInfo(
            _id = response._id,
            name = response.name,
            address = response.address,
            password = response.password,
            email = response.email,
            phone = response.phone,
            image = response.image,
            location = response.location,
            token = response.token
        )
    }

}