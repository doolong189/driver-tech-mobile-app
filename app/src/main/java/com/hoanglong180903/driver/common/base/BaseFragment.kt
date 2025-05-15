package com.hoanglong180903.driver.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    private var _binding: ViewBinding? = null
    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() { return _binding as VB }
    abstract fun initView()
    abstract fun initData()
    abstract fun initEvents()
    abstract fun initObserve()
    abstract var isShowHideActionBar: Boolean

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        return requireNotNull(_binding).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActionBar(isShowHideActionBar)
        view.fitsSystemWindows = true
        initView()
        initData()
        initEvents()
        initObserve()
    }
    private fun setActionBar(isVisible: Boolean) {
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            if (isVisible) {
                show()
                setDisplayHomeAsUpEnabled(true)
            } else {
                hide()
                setDisplayHomeAsUpEnabled(false)
            }
        }
    }

    fun getBaseActivity(): BaseActivity<*>? {
        return activity as? BaseActivity<*>
    }

    fun onBackFragment() {
        if (activity != null) {
            val count = getBaseActivity()?.supportFragmentManager?.backStackEntryCount ?: 0
            if (count > 0) {
                val manager = getBaseActivity()?.supportFragmentManager
                if (manager?.isStateSaved == false) {
                    manager.popBackStack()
                }
            } else {
                getBaseActivity()?.finish()
            }
        }
    }
}