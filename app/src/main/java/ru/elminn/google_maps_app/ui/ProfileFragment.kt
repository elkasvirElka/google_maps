package ru.elminn.google_maps_app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import ru.elminn.google_maps_app.R
import ru.elminn.google_maps_app.utils.PreferenceHelper
import ru.elminn.google_maps_app.utils.LocaleManager


class ProfileFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        var view = inflater.inflate(R.layout.fragment_profile, container, false)
        var back = view.findViewById<ImageView>(R.id.back)
        var exit = view.findViewById<Button>(R.id.exit)
        back.setOnClickListener {activity!!.onBackPressed()}
        var number = view.findViewById<EditText>(R.id.number)
        var numberValue =  PreferenceHelper.getInstance().getString(PreferenceHelper.number)
        number.setText(numberValue)

        exit.setOnClickListener{
            PreferenceHelper.getInstance().clearAllString()
            activity?.onBackPressed()
        }
        context?.let { LocaleManager.updateResources(it, "en") }
        return view

    }
    companion object {
        @JvmStatic
        fun newInstance(): ProfileFragment {
            val fragment = ProfileFragment()
            /* val args = Bundle()
        fragment.setArguments(args)*/
            return fragment
        }
    }


}