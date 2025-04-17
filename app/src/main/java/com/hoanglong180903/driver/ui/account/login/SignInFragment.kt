package com.hoanglong180903.driver.ui.account.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hoanglong180903.driver.databinding.FragmentSignInBinding
import com.hoanglong180903.driver.ui.dashboard.DashBoardActivity

class SignInFragment : Fragment() {
    private lateinit var binding : FragmentSignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        action()
    }

    private fun action(){
        binding.signInBtnLogin.setOnClickListener {
            startActivity(Intent(requireActivity(), DashBoardActivity::class.java))
        }
    }
}