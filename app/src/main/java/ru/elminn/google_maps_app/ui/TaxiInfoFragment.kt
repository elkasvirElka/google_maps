package ru.elminn.google_maps_app.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_taxi_info.*
import ru.elminn.google_maps_app.utils.PreferenceHelper
import android.widget.*
import androidx.annotation.RequiresApi
import ru.elminn.google_maps_app.R
import ru.elminn.google_maps_app.adapter.SelectGroupsAdapter
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList


class TaxiInfoFragment : Fragment() {

    lateinit var adapter: SelectGroupsAdapter
    lateinit var mRecyclerView: RecyclerView
    lateinit var list: ArrayList<String>
    var selectedTaxi: Int = 0


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        var view = inflater.inflate(R.layout.fragment_taxi_info, container, false)

        var list = resources.getStringArray(R.array.d_taxi).toCollection(ArrayList())
        val locationFrom = view.findViewById<EditText>(R.id.TF_location)
        val locationTo = view.findViewById<EditText>(R.id.TF_locationTo)
        val orderTaxi = view.findViewById<Button>(R.id.order_taxi)
        val child_place = view.findViewById<CheckBox>(R.id.child_place)
        val pet_place = view.findViewById<CheckBox>(R.id.pet_place)
        val not_smoke = view.findViewById<CheckBox>(R.id.not_smoke)
        val nameplace_meet = view.findViewById<CheckBox>(R.id.nameplace_meet)

        mRecyclerView = view.findViewById(R.id.recycler_view)
        val mLayoutManager = LinearLayoutManager(context)
        mRecyclerView.layoutManager = mLayoutManager
        adapter = SelectGroupsAdapter()
        mRecyclerView.adapter = adapter
        adapter.setListeners(object : SelectGroupsAdapter.OnClickAdapterListener {
            @SuppressLint("ResourceAsColor")
            override fun onClickItem(position: Int) {
                for (i in 0..adapter.itemCount - 1) {
                    mRecyclerView[i].setBackgroundColor(Color.TRANSPARENT)
                }
                mRecyclerView[position].setBackgroundColor(R.color.colorAccent)
                selectedTaxi = position
            }
        })
        adapter.addData(list)


        if (locationFrom.text.isNullOrBlank()) {
            Handler().postDelayed(
                    {
                        val geocoder = Geocoder(activity)
                        var mainActivity = activity as MainActivity
                        if (mainActivity.latitude > 0) {
                            val addr = geocoder.getFromLocation(mainActivity.latitude, mainActivity.longitude, 1)
                            if (addr != null && addr.size > 0) {
                                locationFrom.setText(addr[0].getAddressLine(0))
                            }
                        }
                    }, 1000)
        }

        locationFrom.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                (activity as MainActivity).onClick(locationFrom)
            }


        })
        locationTo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                recycler_view.visibility = View.VISIBLE
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                (activity as MainActivity).onClick(locationTo)
            }


        })

        orderTaxi.setOnClickListener {

            if (PreferenceHelper.getInstance().getString(PreferenceHelper.password).isNullOrBlank()) {
                activity!!.supportFragmentManager!!.beginTransaction()
                        .add(
                            R.id.drawer_layout,
                            AuthorizationFragment.newInstance()
                        )
                        .addToBackStack(null)
                        .commit()
            } else {
                var taxi = list[selectedTaxi]
                val filename = "/taxi"
                val path = Environment.getExternalStorageDirectory().toString()
                var dataWrite = taxi.plus(" - ")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    dataWrite = dataWrite.plus(LocalDateTime.now().toString()) + "\r\n"
                } else {
                    var date = Date()
                    val formatter = SimpleDateFormat("MMM dd yyyy HH:mma")
                    val dateForm: String = formatter.format(date)
                    dataWrite = dataWrite.plus(dateForm) + "\r\n"
                }
                dataWrite = dataWrite.plus("Маршрут:").plus(locationFrom.text).plus(" - ").plus(locationTo.text) + "\r\n"
                dataWrite = dataWrite.plus("Опции:").plus("\r\n")

                if (pet_place.isChecked)
                    dataWrite = dataWrite.plus(getString(R.string.pet_place)).plus(";").plus("\r\n")
                if (child_place.isChecked)
                    dataWrite = dataWrite.plus(getString(R.string.child_place)).plus(";").plus("\r\n")
                if (not_smoke.isChecked)
                    dataWrite = dataWrite.plus(getString(R.string.not_smoke)).plus(";").plus("\r\n")
                if (nameplace_meet.isChecked)
                    dataWrite = dataWrite.plus(getString(R.string.nameplace_meet)).plus(";").plus("\r\n")

                if (!(pet_place.isChecked || child_place.isChecked || not_smoke.isChecked || nameplace_meet.isChecked))
                    dataWrite = dataWrite.plus(" Не выбрано").plus("\r\n")
                dataWrite = dataWrite.plus("\r\n")

                try {
                    val fw = FileWriter(path + filename, true)
                    fw.write(dataWrite)
                    fw.close()
                    Toast.makeText(activity, "Такси заказано!", Toast.LENGTH_SHORT).show()
                } catch (e: IOException) {
                }
            }
        }
        return view

    }
}