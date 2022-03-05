package com.example.appnasapi.ui.pod

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cesarferreira.tempo.Tempo
import com.cesarferreira.tempo.toString
import com.example.appnasapi.BuildConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

const val DATE_FORMAT = "yyyy-MM-dd"

class PODViewModel(
    private val liveDataForViewToObserve: MutableLiveData<PODData> = MutableLiveData(),
    private val retrofitImpl: PODRetrofitImpl = PODRetrofitImpl()
) : ViewModel() {

    fun getData(): LiveData<PODData> {
        return liveDataForViewToObserve
    }


    fun sendServerRequest(date: String) {
        liveDataForViewToObserve.value = PODData.Loading(null)

        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            PODData.Error(Throwable("You need API key"))
        } else {
            retrofitImpl.getRetrofitImpl().getPOD(apiKey, date)
                .enqueue(object : Callback<PODServerResponseData> {
                    override fun onResponse(
                        call: Call<PODServerResponseData>,
                        response: Response<PODServerResponseData>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            liveDataForViewToObserve.value = PODData.Success(response.body()!!)

                        } else {
                            val message = response.message()

                            if (message.isNullOrEmpty()) {
                                liveDataForViewToObserve.value =
                                    PODData.Error(Throwable("Unidentified error"))
                            } else {
                                liveDataForViewToObserve.value = PODData.Error(Throwable(message))
                            }
                        }
                    }

                    override fun onFailure(call: Call<PODServerResponseData>, t: Throwable) {
                        liveDataForViewToObserve.value = PODData.Error(t)
                    }

                })
        }
    }

}

