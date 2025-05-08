package com.hoanglong180903.driver.ui.account.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.hoanglong180903.driver.common.base.BaseFragment
import com.hoanglong180903.driver.data.enity.GetShipperInfoRequest
import com.hoanglong180903.driver.data.enity.GetShipperInfoResponse
import com.hoanglong180903.driver.data.enity.LoginAccountRequest
import com.hoanglong180903.driver.data.enity.LoginAccountResponse
import com.hoanglong180903.driver.databinding.FragmentSignInBinding
import com.hoanglong180903.driver.ui.main.MainActivity
import com.hoanglong180903.driver.ui.main.user.UserViewModel
import com.hoanglong180903.driver.utils.Event
import com.hoanglong180903.driver.utils.Resource
import com.hoanglong180903.driver.utils.SnackBar


class SignInFragment : BaseFragment() {
    private lateinit var binding : FragmentSignInBinding
    override var isVisibleActionBar: Boolean = false
    private val viewModel by activityViewModels<SignInViewModel>()
    private val userViewModel by activityViewModels<UserViewModel>()
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
            if(binding.signInEdEmail.text.toString().isEmpty() || binding.signInEdPassword.text.toString().isEmpty()){
                viewModel.getLogin(LoginAccountRequest(email = "driver2", password = "123"))
            }else{
                viewModel.getLogin(LoginAccountRequest(email = binding.signInEdEmail.text.toString(),
                    password = binding.signInEdPassword.text.toString()))
            }
        }

        binding.signInIconBack.setOnClickListener {

        }

    }

    override fun initView() {
    }

    override fun setView() {
    }

    override fun setAction() {
    }

    override fun setObserve() {
        viewModel.getLoginResult().observe(viewLifecycleOwner, Observer {
            getLoginResult(it)
        })
        userViewModel.getShipperInfoResult().observe(viewLifecycleOwner , Observer {
            getShipperInfoResult(it)
        })
    }

    private fun getLoginResult(event: Event<Resource<LoginAccountResponse>>){
        event.getContentIfNotHandled()?.let { response ->
            when (response) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    response.data?.let{ userViewModel.getShipperInfo(GetShipperInfoRequest(id = it.shipper?._id)) }
                }

                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    response.message?.let { message ->
                        SnackBar.showSnackBar(requireView(),message)
                    }
                }

                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun getShipperInfoResult(event: Event<Resource<GetShipperInfoResponse>>){
        event.getContentIfNotHandled()?.let { response ->
            when (response) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    response.data?.shipper?.let {
                        userViewModel.getConvertShipperInfo(it)
                        startActivity(Intent(requireContext(),MainActivity::class.java))
                        requireActivity().finish()
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                }

                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }
}