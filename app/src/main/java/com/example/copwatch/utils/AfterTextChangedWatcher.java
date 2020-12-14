package com.example.copwatch.utils;

import android.text.TextWatcher;

public abstract class AfterTextChangedWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
}
