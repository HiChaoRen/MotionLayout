package com.example.motionlayout.demo;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;


public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    public ViewDataBinding mBinding;

    public RecyclerViewHolder(@NonNull ViewDataBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    public RecyclerViewHolder(@NonNull View view) {
        super(view);
        mBinding = DataBindingUtil.bind(view);
    }
}
