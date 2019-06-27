package ru.elminn.google_maps_app

import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

internal class DataParser {


    private fun getDuration(googleDirectionsJson: JSONArray): HashMap<String, String> {
        val googleDirectionsMap = HashMap<String, String>()
        var duration = ""
        var distance = ""


        try {

            duration = googleDirectionsJson.getJSONObject(0).getJSONObject("duration").getString("text")
            distance = googleDirectionsJson.getJSONObject(0).getJSONObject("distance").getString("text")

            googleDirectionsMap["duration"] = duration
            googleDirectionsMap["distance"] = distance

        } catch (e: JSONException) {
            e.printStackTrace()
        }


        return googleDirectionsMap
    }


    private fun getPlace(googlePlaceJson: JSONObject): HashMap<String, String> {
        val googlePlacesMap = HashMap<String, String>()
        var placeName = "-NA-"
        var vicinity = "-NA-"
        var latitude = ""
        var longitude = ""
        var reference = ""
        Log.d("getPlace", "Entered")


        try {
            if (!googlePlaceJson.isNull("name")) {

                placeName = googlePlaceJson.getString("name")

            }
            if (!googlePlaceJson.isNull("vicinity")) {
                vicinity = googlePlaceJson.getString("vicinity")

            }
            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat")
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng")

            reference = googlePlaceJson.getString("reference")

            googlePlacesMap["place_name"] = placeName
            googlePlacesMap["vicinity"] = vicinity
            googlePlacesMap["lat"] = latitude
            googlePlacesMap["lng"] = longitude
            googlePlacesMap["reference"] = reference


            Log.d("getPlace", "Putting Places")

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return googlePlacesMap
    }


    private fun getPlaces(jsonArray: JSONArray): List<HashMap<String, String>> {
        val count = jsonArray.length()
        val placesList = ArrayList<HashMap<String, String>>()
        var placeMap: HashMap<String, String>? = null
        Log.d("Places", "getPlaces")

        for (i in 0 until count) {
            try {
                placeMap = getPlace(jsonArray.get(i) as JSONObject)
                placesList.add(placeMap)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }

        return placesList

    }

    fun parse(jsonData: String): List<HashMap<String, String>> {
        var jsonArray: JSONArray? = null
        val jsonObject: JSONObject

        try {
            Log.d("Places", "parse")

            jsonObject = JSONObject(jsonData)
            jsonArray = jsonObject.getJSONArray("results")

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return getPlaces(jsonArray!!)
    }

    fun parseDirections(jsonData: String): Array<String?> {
        var jsonArray: JSONArray? = null
        val jsonObject: JSONObject

        try {
            jsonObject = JSONObject(jsonData)
            jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0)
                .getJSONArray("steps")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return getPaths(jsonArray)
    }

    fun getPaths(googleStepsJson: JSONArray?): Array<String?> {
        val count = googleStepsJson!!.length()
        val polylines = arrayOfNulls<String>(count)

        for (i in 0 until count) {
            try {
                polylines[i] = getPath(googleStepsJson.getJSONObject(i))
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }

        return polylines
    }

    fun getPath(googlePathJson: JSONObject): String {
        var polyline = ""
        try {
            polyline = googlePathJson.getJSONObject("polyline").getString("points")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return polyline
    }


}
