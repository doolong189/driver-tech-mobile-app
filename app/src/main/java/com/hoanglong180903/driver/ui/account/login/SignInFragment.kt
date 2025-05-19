package com.hoanglong180903.driver.ui.account.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.hoanglong180903.driver.common.base.BaseFragment
import com.hoanglong180903.driver.data.requestmodel.LoginAccountRequest
import com.hoanglong180903.driver.data.responsemodel.LoginAccountResponse
import com.hoanglong180903.driver.databinding.FragmentSignInBinding
import com.hoanglong180903.driver.ui.main.MainActivity
import com.hoanglong180903.driver.utils.Event
import com.hoanglong180903.driver.utils.Resource
import com.hoanglong180903.driver.utils.SharedPreferences
import com.hoanglong180903.driver.utils.PopupUtils
import com.hoanglong180903.driver.utils.Utils


class SignInFragment : BaseFragment<FragmentSignInBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSignInBinding = FragmentSignInBinding::inflate
    override var isShowHideActionBar: Boolean = false
    private val viewModel by activityViewModels<SignInViewModel>()
    private lateinit var preferces : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
    }

    override fun initData() {
        preferces = SharedPreferences(requireContext())
    }

    override fun initEvents() {
        binding.signInBtnLogin.setOnClickListener {
            if(binding.signInEdEmail.text.toString().isEmpty() && binding.signInEdPassword.text.toString().isEmpty()){
                viewModel.getLogin(LoginAccountRequest(email = "driver2", password = "123"))
            }else{
                viewModel.getLogin(
                    LoginAccountRequest(email = binding.signInEdEmail.text.toString(),
                    password = binding.signInEdPassword.text.toString())
                )
            }
        }

        binding.signInIconBack.setOnClickListener {
            onBackFragment()
        }    }

    override fun initObserve() {
        viewModel.getLoginResult().observe(viewLifecycleOwner, Observer {
            getLoginResult(it)
        })
    }


    private fun getLoginResult(event: Event<Resource<LoginAccountResponse>>){
        event.getContentIfNotHandled()?.let { response ->
            when (response) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    response.data?.let{
                        preferces.saveUserData(it.user,it.user?.token)
                        startActivity(Intent(requireContext(),MainActivity::class.java))
                        requireActivity().finish()
                    }
                }

                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    response.message?.let { message ->
                        PopupUtils.showSnackBar(requireView(),message)
                    }
                }

                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }
}