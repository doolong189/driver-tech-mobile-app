package com.hoanglong180903.driver.ui.account.verify

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.hoanglong180903.driver.common.base.BaseFragment
import com.hoanglong180903.driver.databinding.FragmentPhoneInputBinding

class PhoneInputFragment : BaseFragment() {
    private lateinit var binding : FragmentPhoneInputBinding
    override var isVisibleActionBar: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentPhoneInputBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun initView() {
    }

    override fun setView() {
    }

    override fun setAction() {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.SEND_SMS), 1)
        binding.btnContinue.setOnClickListener {
            try {
                val smsManager : SmsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    requireActivity().getSystemService(SmsManager::class.java)
                } else {
                    SmsManager.getDefault()
                }
                smsManager.sendTextMessage("0981454803", "Driver Tech", "Send OTP", null, null)
                Toast.makeText(requireContext(), "Message Sent", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Please enter all the data.."+e.message.toString(), Toast.LENGTH_LONG)
                    .show()
                Log.e("zzzzz",""+e.localizedMessage.toString())
            }
        }
    }

    override fun setObserve() {
    }

}