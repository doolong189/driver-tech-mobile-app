package com.hoanglong180903.driver.ui.account.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.hoanglong180903.driver.R
import com.hoanglong180903.driver.common.application.DriverApplication
import com.hoanglong180903.driver.data.responsemodel.ErrorResponse
import com.hoanglong180903.driver.data.requestmodel.LoginAccountRequest
import com.hoanglong180903.driver.data.responsemodel.LoginAccountResponse
import com.hoanglong180903.driver.data.usecase.UserRepository
import com.hoanglong180903.driver.utils.Event
import com.hoanglong180903.driver.utils.Resource
import com.hoanglong180903.driver.utils.Utils
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException
class SignInViewModel (private val app: Application) : AndroidViewModel(app) {
    private val repository  = UserRepository()
    private val getLoginResult = MutableLiveData <Event<Resource<LoginAccountResponse>>>()
    fun getLoginResult(): LiveData<Event<Resource<LoginAccountResponse>>> {
        return getLoginResult
    }
    fun getLogin(request : LoginAccountRequest) : Job = viewModelScope.launch{
        getLoginResult.postValue(Event(Resource.Loading()))
        try {
            if (Utils.hasInternetConnection(getApplication<DriverApplication>())) {
                val response = repository.loginAccount(request)
                if (response.isSuccessful){
                    response.body()?.let { resultResponse ->
                        getLoginResult.postValue(Event(Resource.Success(resultResponse)))
                    }
                }else {
                    val errorResponse = response.errorBody()?.let {
                        val gson = Gson()
                        gson.fromJson(it.string(), ErrorResponse::class.java)
                    }
                    getLoginResult.postValue(Event(Resource.Error(errorResponse?.message ?: "")))
                }
            }else{
                getLoginResult.postValue(Event(Resource.Error(getApplication<DriverApplication>().getString(
                    R.string.no_internet_connection))))
            }
        }catch (t: Throwable){
            when (t) {
                is IOException -> {
                    getLoginResult.postValue(Event(Resource.Error(getApplication<DriverApplication>().getString(
                        R.string.network_failure))))
                }
                else -> {
                    getLoginResult.postValue(Event(Resource.Error(getApplication<DriverApplication>().getString(
                        R.string.conversion_error))))
                }
            }
        }
    }

}