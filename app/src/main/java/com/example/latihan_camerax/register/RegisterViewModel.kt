package com.example.latihan_camerax.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.latihan_camerax.api.UserModel
import com.example.latihan_camerax.api.UserPreference
import kotlinx.coroutines.launch

class RegisterViewModel(private val pref: UserPreference) : ViewModel() {
    fun saveUser(user: UserModel) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }
}