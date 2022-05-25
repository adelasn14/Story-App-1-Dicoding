package com.example.latihan_camerax.story

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.latihan_camerax.api.AllStoriesResponse
import com.example.latihan_camerax.api.ApiConfig
import com.example.latihan_camerax.api.UserModel
import com.example.latihan_camerax.api.UserPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllStoryViewModel(private val pref: UserPreference) : ViewModel() {
    private val _allStories = MutableLiveData<AllStoriesResponse>()
    val allStories: LiveData<AllStoriesResponse> = _allStories

    companion object {
        const val TAG = "AllStoriesViewModel"
    }

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun getAllStories(token: String) {
        val client = ApiConfig().getApiService().allStories(token)
        client.enqueue(object : Callback<AllStoriesResponse> {
            override fun onResponse(
                call: Call<AllStoriesResponse>,
                response: Response<AllStoriesResponse>
            ) {
                if (response.isSuccessful) {
                    _allStories.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<AllStoriesResponse>, t: Throwable) {
                Log.e(TAG, "OnFailure : ${t.message}")
            }
        })
    }
}
