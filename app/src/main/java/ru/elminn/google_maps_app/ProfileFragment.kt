package ru.elminn.google_maps_app

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment

class ProfileFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        var view = inflater.inflate(R.layout.fragment_profile, container, false)
        var back = view.findViewById<ImageView>(R.id.back)
        back.setOnClickListener {activity!!.onBackPressed()}

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