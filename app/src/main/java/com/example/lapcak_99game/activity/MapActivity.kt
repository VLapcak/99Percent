package com.example.lapcak_99game.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lapcak_99game.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.lapcak_99game.databinding.ActivityMapBinding

/**
 * Trieda [MapActivity] má za úlohu spojiť užívateľa s Google play službami.
 * Pomocou Google máp môže ukázať užívateľovi polohu daného objektu na mapách.
 * Kostra triedy vygenerovaná pomocou šablóny Google Maps Activity.
 */

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     * Na základe získaných súradníc z triedy [GameActivity], zobrazia Google mapy polohu objektu
     * vyzobrazenú markerom.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val position = intent.getIntExtra("location", 1)
        var markerLocation = LatLng(49.20406672057226, 18.754958861571296)

        when (position) {
            1 -> {
                markerLocation = LatLng(49.20144362110134, 18.75341039284054)
            } //chapel
            2 -> {
                markerLocation = LatLng(49.20406672057226, 18.754958861571296)
            } //uniza heart
            3 -> {
                markerLocation = LatLng(49.20416716687542, 18.755185703631298)
            } //rectorate
            4 -> {
                markerLocation = LatLng(45.33243803162718, 14.303497361981176)
            } //Opatia
        }
        mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
        mMap.addMarker(MarkerOptions().position(markerLocation).title("Marker"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLocation, 18f))
    }
}