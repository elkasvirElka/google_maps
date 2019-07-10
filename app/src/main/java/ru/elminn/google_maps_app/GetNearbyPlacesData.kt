package ru.elminn.google_maps_app

import android.os.AsyncTask
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ru.elminn.google_maps_app.utils.DataParser
import ru.elminn.google_maps_app.utils.DownloadUrl
import java.io.IOException
import java.util.HashMap

class GetNearbyPlacesData : AsyncTask<Any, String, String>() {

    var googlePlacesData: String = ""
    var mMap: GoogleMap?= null
    var url: String = ""

    override fun doInBackground(vararg objects: Any): String {
        mMap = objects[0] as GoogleMap
        url = objects[1] as String

        val downloadUrl = DownloadUrl()
        try {
            googlePlacesData = downloadUrl.readUrl(url)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return googlePlacesData
    }

    override fun onPostExecute(s: String) {
        var nearbyPlaceList: List<HashMap<String, String>>? = null
        val parser = DataParser()
        nearbyPlaceList = parser.parse(s)
        showNearbyPlaces(nearbyPlaceList!!)
    }

    private fun showNearbyPlaces(nearbyPlaceList: List<HashMap<String, String>>) {
        for (i in nearbyPlaceList.indices) {
            val markerOptions = MarkerOptions()
            val googlePlace = nearbyPlaceList[i]
            Log.d("onPostExecute", "Entered into showing locations")

            val placeName = googlePlace["place_name"]
            val vicinity = googlePlace["vicinity"]
            val lat = java.lang.Double.parseDouble(googlePlace["lat"]!!)
            val lng = java.lang.Double.parseDouble(googlePlace["lng"]!!)

            val latLng = LatLng(lat, lng)
            markerOptions.position(latLng)
            markerOptions.title("$placeName : $vicinity")
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            mMap!!.addMarker(markerOptions)
            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            mMap!!.animateCamera(CameraUpdateFactory.zoomTo(10f))


        }


    }

}
