package com.madassignment.okuable.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.madassignment.okuable.R
import com.madassignment.okuable.databinding.FragmentCaregiverAdminBinding


class CaregiverFragment_Admin : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentCaregiverAdminBinding = DataBindingUtil.inflate(inflater ,R.layout.fragment_caregiver__admin, container, false)




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}