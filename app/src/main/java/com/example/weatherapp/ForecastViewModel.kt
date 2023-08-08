package com.example.weatherapp


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(private val apiService: ApiService): ViewModel() {

    private val userZip: MutableLiveData<String> = MutableLiveData("55119")
    private val _multiForecast: MutableLiveData<ForecastLists> = MutableLiveData()
    val multiForecast: LiveData<ForecastLists>
        get() = _multiForecast

    fun viewAppeared() = viewModelScope.launch {
        _multiForecast.value = apiService.getForecast(zip = userZip.value.toString() + ",us")
    }

    fun validateZip(zip : String?): Boolean {
        if (
            (zip.isNullOrEmpty() || zip.length != 5) || (zip.any() { !it.isDigit() })) {
            return false
        } else {
            Log.d("forecastVM.validateZip()", "valid Zip")
            userZip.value = zip
            viewAppeared()
            return true
        }
    }

}