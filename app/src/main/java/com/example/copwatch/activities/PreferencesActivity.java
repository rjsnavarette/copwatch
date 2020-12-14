package com.example.copwatch.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import java.lang.reflect.Field;

public class PreferencesActivity extends AppCompatActivity {

    private static final String TAG = "Preferences";
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.vp_slide)
    ViewPager vpScreens;
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
    @BindView(R.id.tv_skip)
    TextView tvSkip;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.iv_prev)
    ImageView ivPrev;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.divider)
    View divider;

    private int mCurrentPage;
    private PreferencesAdapter preferencesAdapter;

    GoogleSignInClient mGoogleSignInClient;
    private String loginAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        ButterKnife.bind(this);

        loginAccount = getIntent().getStringExtra(Constants.LOGIN_MODE);

        preferencesAdapter = new PreferencesAdapter(this);
        vpScreens.setAdapter(preferencesAdapter);
        vpScreens.addOnPageChangeListener(pageChangeListener);

        tvTitle.setVisibility(View.VISIBLE);
        ivPrev.setVisibility(View.VISIBLE);
        tvSkip.setVisibility(View.INVISIBLE);
        divider.setVisibility(View.VISIBLE);

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

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mCurrentPage = position;
            switch (mCurrentPage) {
                case 0:
                    tvTitle.setText(R.string.head_cloud_storage);
                    break;
                case 1:
                    tvTitle.setText(R.string.head_preferences);
                    break;
                case 2:
                    tvTitle.setText(R.string.head_mode_selection);
                    break;
                case 3:
                    tvTitle.setText(R.string.head_permissions);
                    break;
                case 4:
                    tvTitle.setText(R.string.head_advance_permission);
                    break;
                case 5:
                    tvTitle.setText(R.string.head_legal);
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
                } else vpScreens.setCurrentItem(mCurrentPage + 1);
                break;
            case R.id.iv_prev:
                if (mCurrentPage == 0) {
                    logout();
                } else vpScreens.setCurrentItem(mCurrentPage - 1);
                break;
        }
    }

    public void logout() {
        switch (loginAccount) {
            case Constants.FACEBOOK:
                LoginManager.getInstance().logOut();
                backToHome();
                break;
            case Constants.GOOGLE:
                mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> backToHome());
                break;
            default:
                backToHome();
                break;
        }
    }

    private void backToHome() {
        Intent logoutIntent = new Intent(PreferencesActivity.this, SignInActivity.class);
        startActivity(logoutIntent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}