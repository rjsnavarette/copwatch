package com.example.copwatch.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.copwatch.R;
import com.example.copwatch.adapter.PreferencesAdapter;
import com.example.copwatch.service.Constants;
import com.example.copwatch.utils.CustomScroller;
import com.example.copwatch.utils.CustomViewPager;

import java.lang.reflect.Field;

public class PreferencesActivity extends AppCompatActivity implements PreferencesAdapter.CheckedItem {

    private static final String TAG = "PREFERENCES";
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.vp_slide)
    CustomViewPager vpScreens;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ll_dots)
    LinearLayout llDots;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.b_next)
    Button bNext;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.iv_prev)
    ImageView ivPrev;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.divider)
    View divider;

    private int mCurrentPage;
    private PreferencesAdapter preferencesAdapter;

    private String loginAccount;
    private TextView[] mDots;
    private boolean isChecked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prehome);
        ButterKnife.bind(this);

        loginAccount = getIntent().getStringExtra(Constants.LOGIN_MODE);

        preferencesAdapter = new PreferencesAdapter(this);
        vpScreens.setAdapter(preferencesAdapter);
        vpScreens.setPagingEnabled(false);
        vpScreens.addOnPageChangeListener(pageChangeListener);

        tvTitle.setText(R.string.head_cloud_storage);
        tvTitle.setVisibility(View.VISIBLE);
        divider.setVisibility(View.VISIBLE);

        dotsIndicator(0);

        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            mScroller.set(vpScreens, new CustomScroller(this, 1000));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        SharedPreferences userData = getSharedPreferences(Constants.USERDATA, Context.MODE_PRIVATE);

        loginAccount = getIntent().getStringExtra(Constants.LOGIN_MODE);
        if (loginAccount != null) {
            userData.edit().putString(Constants.LOGIN_MODE, getIntent().getStringExtra(Constants.LOGIN_MODE)).apply();
            userData.edit().putString(Constants.FIRST_NAME, getIntent().getStringExtra(Constants.FIRST_NAME)).apply();
            userData.edit().putString(Constants.LAST_NAME, getIntent().getStringExtra(Constants.LAST_NAME)).apply();
            userData.edit().putString(Constants.EMAIL_ADDRESS, getIntent().getStringExtra(Constants.EMAIL_ADDRESS)).apply();
            userData.edit().putString(Constants.PHONE_NUMBER, getIntent().getStringExtra(Constants.PHONE_NUMBER)).apply();
        }
    }

    public void dotsIndicator(int position) {
        mDots = new TextView[preferencesAdapter.getCount()];
        llDots.removeAllViews();

        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;", HtmlCompat.FROM_HTML_MODE_LEGACY));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(ContextCompat.getColor(this, R.color.purple_200));

            llDots.addView(mDots[i]);
        }

        if (mDots.length > 0) {
            mDots[position].setTextColor(ContextCompat.getColor(this, R.color.purple_500));
        }
    }

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            dotsIndicator(position);
            mCurrentPage = position;
            switch (position) {
                case 0:
                    tvTitle.setText(R.string.head_cloud_storage);
                    ivPrev.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    tvTitle.setText(R.string.head_preferences);
                    ivPrev.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    tvTitle.setText(R.string.head_mode_selection);
                    ivPrev.setVisibility(View.VISIBLE);


                    break;
                case 3:
                    tvTitle.setText(R.string.head_permissions);
                    ivPrev.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    tvTitle.setText(R.string.head_advance_permission);
                    ivPrev.setVisibility(View.VISIBLE);
                    break;
                case 5:
                    tvTitle.setText(R.string.head_legal);
                    ivPrev.setVisibility(View.VISIBLE);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @SuppressLint("NonConstantResourceId")
    @OnClick(value = {R.id.b_next, R.id.iv_prev})
    public void userClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        switch (view.getId()) {
            case R.id.b_next:
                if (mCurrentPage == preferencesAdapter.getCount() - 1) {
                    Intent mainIntent = new Intent(PreferencesActivity.this, HomeActivity.class);
                    startActivity(mainIntent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else if (mCurrentPage == 2) {
                    if (isChecked) {
                        vpScreens.setCurrentItem(mCurrentPage + 1);
                    }
                } else vpScreens.setCurrentItem(mCurrentPage + 1);
                break;
            case R.id.iv_prev:
                if (mCurrentPage == 0) {
                    ivPrev.setEnabled(false);
                } else vpScreens.setCurrentItem(mCurrentPage - 1);
                break;
        }
    }

    @Override
    public void onCheckedItem(boolean isItemChecked) {
        isChecked = isItemChecked;
        if (!isItemChecked) {
            tvStatus.setText("Please select mode");
            tvStatus.setVisibility(View.VISIBLE);
        } else {
            tvStatus.setVisibility(View.INVISIBLE);
        }
    }
}