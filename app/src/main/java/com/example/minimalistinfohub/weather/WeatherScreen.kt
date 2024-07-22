package com.example.minimalistinfohub.weather

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.minimalistinfohub.MainActivity
import com.example.minimalistinfohub.location.LocationUtils
import com.example.minimalistinfohub.location.LocationViewModel
import com.example.minimalistinfohub.utils.BackButton
import com.example.minimalistinfohub.utils.RefreshState


@SuppressLint("SuspiciousIndentation")
@Composable
fun WeatherScreen(
    locationUtils: LocationUtils,
    context: Context
) {
    val weatherViewModel : WeatherViewModel = viewModel()
    val weatherViewState by weatherViewModel.weatherState
    val locationViewModel : LocationViewModel = viewModel()
    val locationViewState by locationViewModel.location.collectAsState()
    val addressViewState by locationViewModel.address.collectAsState()

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions -> if(permissions.containsKey(Manifest.permission.ACCESS_COARSE_LOCATION) && permissions.containsKey(
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            //I have access to location
            locationUtils.requestLocationUpdates(locationViewModel)
        } else {
            // Ask for permission
            val rationaleRequired = ActivityCompat.shouldShowRequestPermissionRationale(
                context as MainActivity, Manifest.permission.ACCESS_FINE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        context, Manifest.permission.ACCESS_COARSE_LOCATION)

            if(rationaleRequired) {
                Toast.makeText(context, "Location Permission is required for this feature", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Location Permission is required for this feature, set it manually", Toast.LENGTH_LONG).show()
            }
        }
        })


        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp)
        ) {

            RefreshState({locationViewModel.fetchAddress()})

            if(locationUtils.hasLocationPermission(context)) {
                locationUtils.requestLocationUpdates(locationViewModel)
                weatherViewModel.fetchWeather(locationViewModel.getLatitude(), locationViewModel.getLongitude())
                WeatherPanel(weatherViewState, addressViewState, locationViewState)

            } else {
                requestPermissionLauncher.launch(arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ))
            }
        }

}

@Composable
fun WeatherPanel(weatherViewState : WeatherViewModel.WeatherState,
                 addressViewState : LocationViewModel.AddressState,
                 locationViewState : LocationViewModel.LocationState
) {

    Card(
        shape = CutCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            Row {
                Text("Temperature",
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = weatherViewState.weather?.current?.temperature_2m.toString()+ " °C",  fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Text("Location:",
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = addressViewState.address?.firstOrNull()?.formatted_address ?: "No Address")
            }
            Spacer(modifier = Modifier.height(26.dp))
            Row {
                Text(text = "Maps coordinates:",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Text("Longitude ",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold)
                Text(text = locationViewState.location?.longitude.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Text("Latitude ",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(text = locationViewState.location?.latitude.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

}