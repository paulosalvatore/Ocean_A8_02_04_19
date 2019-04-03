package br.com.paulosalvatore.ocean_a8_02_04_19

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        initLocation()
    }

    //    @RequiresPermission(ACCESS_FINE_LOCATION)
    @SuppressLint("MissingPermission")
    private fun initLocation() {
        if (isNotPermissionGranted(ACCESS_FINE_LOCATION) &&
            isNotPermissionGranted(ACCESS_COARSE_LOCATION)
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )

            return
        }

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val locationProvider = LocationManager.GPS_PROVIDER

        val lastKnownLocation: Location? = locationManager.getLastKnownLocation(locationProvider)

        lastKnownLocation?.let {
            val latLng = LatLng(it.latitude, it.longitude)
            mMap.addMarker(MarkerOptions().position(latLng).title("My Position"))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19F))
        }

        locationManager
            .requestLocationUpdates(
                locationProvider,
                1000L,
                1F,
                object : LocationListener {
                    override fun onLocationChanged(location: Location?) {
                        location?.let {
                            val latLng = LatLng(it.latitude, it.longitude)
                            mMap.addMarker(MarkerOptions().position(latLng).title("My Position"))
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19F))
                        }
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                    }

                    override fun onProviderEnabled(provider: String?) {
                    }

                    override fun onProviderDisabled(provider: String?) {
                    }
                })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> if (grantResults[0] == PERMISSION_GRANTED) {
                initLocation()
            }
        }
    }

}
