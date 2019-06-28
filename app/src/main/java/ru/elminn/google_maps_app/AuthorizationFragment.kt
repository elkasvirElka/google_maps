package ru.elminn.google_maps_app

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import ru.elminn.google_maps_app.utils.PreferenceHelper

class AuthorizationFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        var view = inflater.inflate(R.layout.fragment_authorization, container, false)
        var back = view.findViewById<ImageView>(R.id.back)
        var save = view.findViewById<Button>(R.id.save)
        var number = view.findViewById<EditText>(R.id.number)
       var token =  PreferenceHelper.getInstance().getToken()
        number.setText(token)
        back.setOnClickListener {activity!!.onBackPressed()}
        save.setOnClickListener {
            PreferenceHelper.getInstance().putToken(number.text.toString())
            activity!!.onBackPressed()
        }
        return view

    }
    companion object {
        @JvmStatic
        fun newInstance(): AuthorizationFragment {
            val fragment = AuthorizationFragment()
            /* val args = Bundle()
        fragment.setArguments(args)*/
            return fragment
        }
    }
}