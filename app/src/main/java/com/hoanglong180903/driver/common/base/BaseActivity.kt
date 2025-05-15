package com.hoanglong180903.driver.common.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity(){

    private var _binding: ViewBinding? = null
    abstract val bindingInflater: (LayoutInflater) -> VB

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isFinishing && !isDestroyed) {
            _binding = bindingInflater.invoke(layoutInflater)
            setContentView(requireNotNull(_binding).root)
            initView()
            initData()
            initEvents()
            initObserve()
        }
    }

    abstract fun initView()
    abstract fun initData()
    abstract fun initEvents()
    abstract fun initObserve()

    fun onNextScreen(nextActivity: Class<*>, bundle: Bundle?, isFinish: Boolean) {
        val intent = Intent(this, nextActivity)
        bundle?.let {
            intent.putExtras(it)
        }
        startActivity(intent)
        if (isFinish) {
            finish()
        }
    }

    fun onNextScreenBundle(nextActivity: Class<*>, bundle: Bundle?) {
        val intent = Intent(this, nextActivity)
        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        bundle?.let {
            intent.putExtras(it)
        }
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}