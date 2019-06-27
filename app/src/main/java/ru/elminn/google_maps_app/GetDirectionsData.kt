package ru.elminn.google_maps_app

import android.graphics.Color
import android.os.AsyncTask
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import java.io.IOException

class GetDirectionsData(polylines: ArrayList<Polyline>) : AsyncTask<Any, String, String>() {

    var mMap: GoogleMap? = null
    var url: String = ""
    var googleDirectionsData: String = ""
    var polyline : ArrayList<Polyline> = polylines
    internal var duration: String? = null
    internal var distance: String? = null
    var latLng: LatLng = LatLng(1.0, 1.0)

    override fun doInBackground(vararg objects: Any): String {
        mMap = objects[0] as GoogleMap
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

        for(item in polyline){
            item.remove()
        }
        polyline.clear()

        val count = directionsList.size
        for (i in 0 until count) {
            val options = PolylineOptions()
            options.color(Color.BLUE)
            options.width(10F)
            options.addAll(PolyUtil.decode(directionsList[i]))
            polyline.add(mMap!!.addPolyline(options))
        }
    }

}

