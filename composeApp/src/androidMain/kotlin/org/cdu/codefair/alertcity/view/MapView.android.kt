package org.cdu.codefair.alertcity.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
actual fun MapView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        val darwin = LatLng(-12.462827, 130.841782)
        val darwinMarkerState = rememberMarkerState(position = darwin)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(darwin, 10f)
        }
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
        ) {
            Marker(darwinMarkerState, title = "Darwin", snippet = "Marker in Darwin")
        }
    }
}