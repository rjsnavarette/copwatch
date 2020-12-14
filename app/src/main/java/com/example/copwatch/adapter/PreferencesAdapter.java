package com.example.copwatch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.copwatch.R;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

public class PreferencesAdapter extends PagerAdapter {

    private final LayoutInflater mLayoutInflater;
    private final int[] screens = {R.layout.activity_select_storage, R.layout.activity_preferences, R.layout.activity_mode_selection,
            R.layout.activity_permissions, R.layout.activity_advance_permissions, R.layout.activity_legal_disclaimer};

    public PreferencesAdapter(Context context) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return screens.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((ConstraintLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View screenOne = mLayoutInflater.inflate(screens[0], container, false);
        View screenTwo = mLayoutInflater.inflate(screens[1], container, false);
        View screenThree = mLayoutInflater.inflate(screens[2], container, false);
        View screenFour = mLayoutInflater.inflate(screens[3], container, false);
        View screenFive = mLayoutInflater.inflate(screens[4], container, false);
        View screenSix = mLayoutInflater.inflate(screens[5], container, false);

        View[] screenViews = {screenOne, screenTwo, screenThree, screenFour, screenFive, screenSix};

        container.addView(screenViews[position]);

        return screenViews[position];
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
