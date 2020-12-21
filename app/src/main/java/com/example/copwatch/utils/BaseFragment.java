package com.example.copwatch.utils;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

public abstract class BaseFragment extends Fragment {
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUp(view);
    }

    protected abstract void setUp(View view);
}
