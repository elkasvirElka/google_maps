package ru.elminn.google_maps_app

import android.graphics.Color
import android.os.AsyncTask
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import java.io.IOException
import java.util.ArrayList

class GetDirectionsData : AsyncTask<Any, String, String>() {

    var mMap: GoogleMap? = null
    var url: String = ""
    var googleDirectionsData: String = ""
    internal var duration: String? = null
    internal var distance: String? = null
    var latLng: LatLng = LatLng(1.0, 1.0)

    override fun doInBackground(vararg objects: Any): String {
        var item = objects[0] as ArrayList<Any>
        mMap = item[0] as GoogleMap
        url = objects[1] as String
        latLng = objects[2] as LatLng


        val downloadUrl = DownloadUrl()
        try {
            googleDirectionsData = downloadUrl.readUrl(url)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return googleDirectionsData
    }

    override fun onPostExecute(s: String) {

        val directionsList: Array<String?>
        val parser = DataParser()
        directionsList = parser.parseDirections(s)
        displayDirection(directionsList)

    }

    fun displayDirection(directionsList: Array<String?>) {

        val count = directionsList.size
        for (i in 0 until count) {
            val options = PolylineOptions()
            options.color(Color.RED)
            options.width(10F)
            options.addAll(PolyUtil.decode(directionsList[i]))

            mMap!!.addPolyline(options)
        }
    }
}

