package org.cdu.codefair.alertcity.view

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
actual fun MapView() {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val darwin = LatLng(-12.462827, 130.841782)
    val locationInterval = 10000L
    val darwinMarkerState = rememberMarkerState(position = darwin)
    val cameraZoom = 15f
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(darwin, cameraZoom - 3)
    }
    val coroutineScope = rememberCoroutineScope()
    var currentLocation by remember { mutableStateOf(darwin) }

    val locationPermissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    )
    LaunchedEffect(locationPermissionState.allPermissionsGranted) {
        if (!locationPermissionState.allPermissionsGranted) {
            locationPermissionState.launchMultiplePermissionRequest()
        } else {
            val locationRequest = LocationRequest.Builder(locationInterval)
                .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
                .setIntervalMillis(locationInterval).build()
            fusedLocationClient.requestLocationUpdates(
                locationRequest, object : LocationCallback() {
                    override fun onLocationResult(result: LocationResult) {
                        result.locations.firstOrNull()?.let { location ->
                            currentLocation = LatLng(location.latitude, location.longitude)
                            coroutineScope.launch {
                                cameraPositionState.animate(
                                    CameraUpdateFactory.newLatLngZoom(
                                        currentLocation, cameraZoom
                                    )
                                )
                            }
                        }
                    }
                }, context.mainLooper
            )
        }
    }
    Box(Modifier.fillMaxSize()) {
        if (locationPermissionState.allPermissionsGranted) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(myLocationButtonEnabled = true),
                properties = MapProperties(isMyLocationEnabled = true)
            ) {
//                Marker(
//                    state = MarkerState(currentLocation),
//                    title = "You are here",
//                    snippet = "Current location"
//                )
                Marker(darwinMarkerState, title = "Darwin", snippet = "Marker in Darwin")
            }
        } else {
            // Permissions not granted, show a message
            Text("Location permission required to display the map with current location.")
        }
    }
}