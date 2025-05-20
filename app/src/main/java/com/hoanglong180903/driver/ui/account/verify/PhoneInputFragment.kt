package com.hoanglong180903.driver.ui.account.verify

import android.Manifest
import android.os.Build
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.hoanglong180903.driver.common.base.BaseFragment
import com.hoanglong180903.driver.databinding.FragmentPhoneInputBinding
import com.hoanglong180903.driver.utils.PopupUtils

class PhoneInputFragment : BaseFragment<FragmentPhoneInputBinding>() {
    override var isShowHideActionBar: Boolean = false
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPhoneInputBinding
        get() = FragmentPhoneInputBinding::inflate

    override fun initView() {
    }

    override fun initData() {
    }

    override fun initEvents() {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.SEND_SMS), 1)
        binding.btnContinue.setOnClickListener {
            try {
                val smsManager : SmsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    requireActivity().getSystemService(SmsManager::class.java)
                } else {
                    SmsManager.getDefault()
                }
                smsManager.sendTextMessage("0981454803", "Driver Tech", "Send OTP", null, null)
                PopupUtils.showToast(requireContext(), "Message Sent")
            } catch (e: Exception) {
                PopupUtils.showToast(requireContext(), "Please enter all the data.."+e.message.toString())
                Log.e("zzzzz",""+e.localizedMessage.toString())
            }
        }    }

    override fun initObserve() {
    }
}