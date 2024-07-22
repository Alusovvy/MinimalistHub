package com.example.minimalistinfohub.location

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minimalistinfohub.utils.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LocationViewModel: ViewModel() {
    private val _location = MutableStateFlow(LocationState(null))
    val location: MutableStateFlow<LocationState> = _location

    private val _address = MutableStateFlow(AddressState(emptyList()))
    val address: StateFlow<AddressState> = _address

    fun updateLocation(newLocation: LocationData) {
        _location.value = LocationState(newLocation)
    }

    fun getLongitude() : Double {
        return _location.value.location?.longitude ?: 0.0
    }

    fun getLatitude() : Double {
        return _location.value.location?.latitude ?: 0.0
    }

    fun fetchAddress() {
        if(_location.value.location == null) return
        val location = _location.value
        val latlng = "${location.location?.latitude},${location.location?.longitude}"
        fetchAddress(latlng)
    }

    private fun fetchAddress(latlng: String) {
        try {
            viewModelScope.launch {
                val result = RetrofitClient.create().getAddressFromCoordinates(
                    latlng,
                    "heheNIE-MA_ZA-FRICO"
                )

                _address.value = AddressState(result.results)
            }

        }catch (e:Exception) {
            Log.d("res1", "${e.cause} " + " ${e.message}")
        }
    }


    data class LocationState(
        val location: LocationData? = null,
    )

    data class AddressState(
        val address: List<GeoCodingResult>? = null
    )
}