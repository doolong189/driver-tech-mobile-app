package com.hoanglong180903.driver.common.base

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

private typealias ViewHolderViewBindingInflater<VB> = (
    inflater: LayoutInflater,
    parent: ViewGroup,
    attachToParent: Boolean
) -> VB
abstract class BaseAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    private var recyclerView: RecyclerView? = null
    fun <VB : ViewBinding> ViewGroup.inflateBinding(
        bindingInflater: ViewHolderViewBindingInflater<VB>
    ): VB {
        return bindingInflater.invoke(LayoutInflater.from(context), this, false)
    }
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }
}