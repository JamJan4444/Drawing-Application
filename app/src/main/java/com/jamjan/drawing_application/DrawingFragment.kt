package com.jamjan.drawing_application

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

class DrawingFragment : Fragment() {

    var drawing_view : ImageView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v : View? = inflater.inflate(R.layout.fragment_drawing, container, false)
        return v
    }
}
