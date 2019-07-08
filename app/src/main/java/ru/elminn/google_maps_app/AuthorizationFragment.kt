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
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.elminn.google_maps_app.utils.PreferenceHelper

class AuthorizationFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        var view = inflater.inflate(R.layout.fragment_authorization, container, false)
        var back = view.findViewById<ImageView>(R.id.back)
        var save = view.findViewById<Button>(R.id.save)
        var number = view.findViewById<EditText>(R.id.number)
        var password_field = view.findViewById<EditText>(R.id.password_field)
        // var password =  PreferenceHelper.getInstance().getPassword()
        //  number.setText(password)
        back.setOnClickListener { activity!!.onBackPressed() }
        save.setOnClickListener {
            val numberValue = number.text.toString()
            val passwordValue = password_field.text.toString()
            if (numberValue.isNotEmpty() && passwordValue.isNotEmpty()) {
                PreferenceHelper.getInstance().putString(PreferenceHelper.number, number.text.toString())
                PreferenceHelper.getInstance().putString(PreferenceHelper.password, password_field.text.toString())
                activity!!.onBackPressed()
            } else {
                Toast.makeText(activity, "Для регистрации введите номер и пароль", Toast.LENGTH_LONG).show()
            }
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