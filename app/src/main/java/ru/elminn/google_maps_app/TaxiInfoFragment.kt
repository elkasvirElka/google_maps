package ru.elminn.google_maps_app

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.GridView
import androidx.fragment.app.Fragment
import ru.elminn.google_maps_app.utils.PreferenceHelper

class TaxiInfoFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        var view = inflater.inflate(R.layout.fragment_taxi_info, container, false)

        val locationFrom = view.findViewById<EditText>(R.id.TF_location)
        val locationTo = view.findViewById<EditText>(R.id.TF_locationTo)
        val orderTaxi = view.findViewById<Button>(R.id.order_taxi)
        val grid = view.findViewById<GridLayout>(R.id.grid)

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
                grid.visibility = View.VISIBLE
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                (activity as MainActivity).onClick(locationTo)
            }


        })

        orderTaxi.setOnClickListener{

            if(PreferenceHelper.getInstance().getPassword().isNullOrBlank()) {
                activity!!.supportFragmentManager!!.beginTransaction()
                        .add(R.id.drawer_layout, AuthorizationFragment.newInstance())
                        .addToBackStack(null)
                        .commit()
            }
        }
        return view

    }
}