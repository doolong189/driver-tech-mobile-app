package com.hoanglong180903.driver.ui.account.login

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.FacebookSdk.getApplicationContext
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.hoanglong180903.driver.common.base.BaseFragment
import com.hoanglong180903.driver.data.enity.GetShipperInfoRequest
import com.hoanglong180903.driver.data.enity.GetShipperInfoResponse
import com.hoanglong180903.driver.data.enity.LoginAccountRequest
import com.hoanglong180903.driver.data.enity.LoginAccountResponse
import com.hoanglong180903.driver.databinding.FragmentSignInBinding
import com.hoanglong180903.driver.ui.dashboard.DashBoardActivity
import com.hoanglong180903.driver.ui.dashboard.user.UserViewModel
import com.hoanglong180903.driver.utils.Event
import com.hoanglong180903.driver.utils.Resource
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Arrays


class SignInFragment : BaseFragment() {
    private lateinit var binding : FragmentSignInBinding

    private val RC_SIGN_IN = 1
    private var mGoogleSignInClient: GoogleSignInClient? = null
    override var isVisibleActionBar: Boolean = false

    private val EMAIL: String = "email"
    private val callbackManager = CallbackManager.Factory.create()
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
            }
            viewModel.getLogin(LoginAccountRequest(email = binding.signInEdEmail.text.toString(),
                password = binding.signInEdPassword.text.toString()))
        }

        binding.googleSignIn.setOnClickListener {
            signIn()
        }


        binding.signInBtnFacebook.setOnClickListener {

        }

//        binding.signInBtnFacebook.setReadPermissions(Arrays.asList(EMAIL));
//        binding.signInBtnFacebook.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
//            override fun onSuccess(loginResult: LoginResult?) {
//                // App code
//            }
//
//            override fun onCancel() {
//                // App code
//            }
//
//            override fun onError(exception: FacebookException) {
//                // App code
//            }
//        })
//        LoginManager.getInstance().registerCallback(callbackManager,
//            object : FacebookCallback<LoginResult?> {
//                override fun onSuccess(loginResult: LoginResult?) {
//                    // App code
//                }
//
//                override fun onCancel() {
//                    // App code
//                }
//
//                override fun onError(exception: FacebookException) {
//                    // App code
//                }
//            })
//        val accessToken = AccessToken.getCurrentAccessToken()
//        val isLoggedIn = accessToken != null && !accessToken.isExpired
//        if (isLoggedIn){
//            val intent = Intent(requireContext(), DashBoardActivity::class.java)
//            startActivity(intent)
//        }
//        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

    }

    override fun initView() {
//        val gso =
//            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build()
//        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
//        FacebookSdk.sdkInitialize(getApplicationContext())
//        AppEventsLogger.activateApp(getApplicationContext())
//        printHashKey()
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
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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
                    response.data?.shipper?.let {
                        userViewModel.getConvertShipperInfo(it)
                    }
                    startActivity(Intent(requireContext(),DashBoardActivity::class.java))
                    requireActivity().finish()
                }
                is Resource.Error -> {
                }

                is Resource.Loading -> {
                }
            }
        }
    }


    override fun onStart() {
        super.onStart()
//        val account = GoogleSignIn.getLastSignedInAccount(requireActivity())
//        if(account!=null) {
//            val intent = Intent(requireContext(), DashBoardActivity::class.java)
//            startActivity(intent)
//        }
    }


    private fun signIn() {
        val intent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)
    }

    private fun printHashKey() {
        try {
            val info
                    :
//                    PackageInfo = requireActivity().packageManager.getPackageInfo(
//                "com.hoanglong180903.driver",
//                PackageManager.GET_SIGNATURES
                    PackageInfo = requireActivity().packageManager.getPackageInfo(
                requireActivity().packageName,
                PackageManager.GET_SIGNATURES
            )

            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d(
                    "KeyHash:",
                    Base64.encodeToString(
                        md.digest(),
                        Base64.DEFAULT
                    )
                )
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account : GoogleSignInAccount? = task.getResult(ApiException::class.java)
                val intent = Intent(requireContext(), DashBoardActivity::class.java)
                startActivity(intent)

            } catch (e: ApiException) {
                Log.e("TAG","signInResult:failed code=" + e.statusCode)
            }
        }
    }


}