package com.example.copwatch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.copwatch.R;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

public class OnBoardingScreenAdapter extends PagerAdapter {

    private final LayoutInflater mLayoutInflater;
    private final int[] screens = {R.layout.screen_one_layout, R.layout.screen_two_layout, R.layout.screen_two_layout,
            R.layout.screen_three_layout};

    public OnBoardingScreenAdapter(Context context) {
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
        View screenOne = mLayoutInflater.inflate(R.layout.screen_one_layout, container, false);
        View screenTwo = mLayoutInflater.inflate(R.layout.screen_two_layout, container, false);
        View screenThree = mLayoutInflater.inflate(R.layout.screen_three_layout, container, false);

        View[] screenViews = {screenOne, screenTwo, screenTwo, screenThree};

        if (position == 2) {
            TextView tvInfo = screenTwo.findViewById(R.id.tv_info);
            tvInfo.setText(R.string.app_onboarding_one);
        } else if (position == 1) {
            TextView tvInfo = screenTwo.findViewById(R.id.tv_info);
            tvInfo.setText(R.string.app_onboarding_two);
        }

        container.addView(screenViews[position]);

        return screenViews[position];
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
