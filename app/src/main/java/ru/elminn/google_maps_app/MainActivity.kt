package ru.elminn.google_maps_app

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.View
import android.widget.*
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_main.view.*
import java.io.IOException

class MainActivity : FragmentActivity(), NavigationView.OnNavigationItemSelectedListener,
    OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    LocationListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener {

    @Volatile var polylines: ArrayList<Polyline> = ArrayList()
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        // setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        var where = findViewById<Button>(R.id.where)
        where.setOnClickListener{v -> onClick(v)}
        navView.setNavigationItemSelectedListener(this)
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission()
        }

        //Check if Google Play Services Available or not
        if (!CheckGooglePlayServices()) {
            Log.d("onCreate", "Finishing test case since Google Play Services are not available")
            finish()
        } else {
            Log.d("onCreate", "Google Play Services available.")
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        var locationButton = mapFragment.view?.findViewById(2) as ImageView
        locationButton.setImageResource(R.drawable.my_location_button)
        var layoutParams =
            locationButton.getLayoutParams() as RelativeLayout.LayoutParams
        // position on right bottom
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        layoutParams.setMargins(0, 0, 30, 250)
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_profile -> {
             //   Utils.addFragmentToActivity(supportFragmentManager, ProfileFragment(), R.id.drawer_layout)
                supportFragmentManager!!.beginTransaction()
                    .add(R.id.drawer_layout, ProfileFragment.newInstance())
                    .addToBackStack(null)
                    .commit()
            }
            R.id.nav_history -> {

            }
            R.id.nav_lovely_addr -> {

            }
            R.id.nav_payment -> {

            }
            R.id.nav_taxi_service -> {

            }
            R.id.nav_settings -> {

            }
            R.id.nav_info -> {

            }
            R.id.nav_support -> {

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    private var mMap: GoogleMap? = null
    internal var mGoogleApiClient: GoogleApiClient? = null
    var mLastLocation: Location = Location("")
    internal var mCurrLocationMarker: Marker? = null
    var mLocationRequest: LocationRequest = LocationRequest()
    internal var PROXIMITY_RADIUS = 10000
    internal var latitude: Double = 0.toDouble()
    internal var longitude: Double = 0.toDouble()
    internal var end_latitude: Double = 0.toDouble()
    internal var end_longitude: Double = 0.toDouble()

    private val directionsUrl: String
        get() {
            val googleDirectionsUrl = StringBuilder("https://maps.googleapis.com/maps/api/directions/json?")
            googleDirectionsUrl.append("origin=$latitude,$longitude")
            googleDirectionsUrl.append("&destination=$end_latitude,$end_longitude")
            googleDirectionsUrl.append("&key=" + getString(R.string.google_maps_key))

            return googleDirectionsUrl.toString()
        }


    private fun CheckGooglePlayServices(): Boolean {
        val googleAPI = GoogleApiAvailability.getInstance()
        val result = googleAPI.isGooglePlayServicesAvailable(this)
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(
                    this, result,
                    0
                ).show()
            }
            return false
        }
        return true
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

        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) === PackageManager.PERMISSION_GRANTED
            ) {
                buildGoogleApiClient()
                mMap!!.isMyLocationEnabled = true
            }
        } else {
            buildGoogleApiClient()
            mMap!!.isMyLocationEnabled = true
        }

        mMap!!.setOnMarkerDragListener(this)
        mMap!!.setOnMarkerClickListener(this)
        mMap!!.setOnMapClickListener { v ->
            var fragment = findViewById<ConstraintLayout>(R.id.taxi_info)
            fragment.visibility = View.GONE
            findViewById<Button>(R.id.where).visibility = View.VISIBLE
        }
    }


    @Synchronized
    protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        mGoogleApiClient!!.connect()
    }

    fun onClick(v: View) {
        //   var dataTransfer = arrayOfNulls<Any>(2)
        var getNearbyPlacesData = GetNearbyPlacesData()


        when (v.id) {
            R.id.where ->{
                var fragment = findViewById<ConstraintLayout>(R.id.taxi_info)
                if(fragment == null) {
                    Utils.addFragmentToActivity(supportFragmentManager, TaxiInfoFragment(), R.id.my_container)
                }else{
                    fragment.visibility = View.VISIBLE
                }
                v.visibility = View.GONE
            }
            R.id.B_search -> {
                val tf_location = findViewById(R.id.TF_location) as EditText
                val location = tf_location.text.toString()
                var addressList: List<Address>? = null
                val markerOptions = MarkerOptions()

                Log.d("location = ", location)

                if (location != "") {
                    val geocoder = Geocoder(this)
                    try {
                        addressList = geocoder.getFromLocationName(location, 5)

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    if (addressList != null) {
                        for (i in addressList.indices) {
                            val myAddress = addressList[i]
                            val latLng = LatLng(myAddress.latitude, myAddress.longitude)
                            markerOptions.position(latLng)
                            mMap!!.addMarker(markerOptions)
                            mMap!!.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                        }
                    }

                }
            }


         /*   R.id.B_to -> {
                // dataTransfer = arrayOfNulls(3)
                var url = directionsUrl
                val getDirectionsData = GetDirectionsData(polylines)
                getDirectionsData.execute(mMap, url, LatLng(end_latitude, end_longitude))
            }*/
        }
    }

    private fun getUrl(latitude: Double, longitude: Double, nearbyPlace: String): String {
        val googlePlacesUrl = StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?")
        googlePlacesUrl.append("location=$latitude,$longitude")
        googlePlacesUrl.append("&radius=$PROXIMITY_RADIUS")
        googlePlacesUrl.append("&type=$nearbyPlace")
        googlePlacesUrl.append("&sensor=true")
        googlePlacesUrl.append("&key=" + getString(R.string.google_maps_key))
        Log.d("getUrl", googlePlacesUrl.toString())
        return googlePlacesUrl.toString()
    }


    override fun onConnected(bundle: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 1000
        mLocationRequest.fastestInterval = 1000
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) === PackageManager.PERMISSION_GRANTED
        ) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
        }
    }


    override fun onConnectionSuspended(i: Int) {

    }

    override fun onLocationChanged(location: Location) {
        Log.d("onLocationChanged", "entered")

        mLastLocation = location
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker!!.remove()
        }

        latitude = location.latitude
        longitude = location.longitude


        val latLng = LatLng(location.latitude, location.longitude)
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.draggable(true)
        markerOptions.title("Current Position")
        markerOptions.icon(bitmapDescriptorFromVector(R.drawable.ic_location))
        mCurrLocationMarker = mMap!!.addMarker(markerOptions)

        //move map camera
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap!!.animateCamera(CameraUpdateFactory.zoomTo(11f))


        Toast.makeText(this, "Ваше текущее местоположение", Toast.LENGTH_LONG).show()


        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
            Log.d("onLocationChanged", "Removing Location Updates")
        }

    }

    private fun bitmapDescriptorFromVector(@DrawableRes vectorDrawableResourceId: Int): BitmapDescriptor {
        var vectorDrawable = ContextCompat.getDrawable(this, vectorDrawableResourceId)
        vectorDrawable!!.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable!!.getIntrinsicHeight())
        var bitmap = Bitmap.createBitmap(
            vectorDrawable.getIntrinsicWidth(),
            vectorDrawable.getIntrinsicHeight(),
            Bitmap.Config.ARGB_8888
        )
        var canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {

    }

    fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED
        ) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
            return false
        } else {
            return true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) === PackageManager.PERMISSION_GRANTED
                    ) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient()
                        }
                        mMap!!.isMyLocationEnabled = true
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }// other 'case' lines to check for other permissions this app might request.
        // You can add here other case statements according to your requirement.
    }


    override fun onMarkerClick(marker: Marker): Boolean {
        marker.isDraggable = true
        return false
    }

    override fun onMarkerDragStart(marker: Marker) {

    }

    override fun onMarkerDrag(marker: Marker) {

    }

    override fun onMarkerDragEnd(marker: Marker) {
        end_latitude = marker.position.latitude
        end_longitude = marker.position.longitude

        Log.d("end_lat", "" + end_latitude)
        Log.d("end_lng", "" + end_longitude)

        var url = directionsUrl
        val getDirectionsData = GetDirectionsData(polylines)
        var result = getDirectionsData.execute(mMap, url, LatLng(end_latitude, end_longitude))
    }

    companion object {

        val MY_PERMISSIONS_REQUEST_LOCATION = 99
    }
}
