package com.example.copwatch.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.copwatch.R;
import com.example.copwatch.adapter.OnBoardingScreenAdapter;
import com.example.copwatch.utils.CustomScroller;

import java.lang.reflect.Field;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OnBoardingActivity extends AppCompatActivity {

    private static final String TAG = "ONBOARDING";
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
    @BindView(R.id.tv_skip)
    TextView tvSkip;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.iv_prev)
    ImageView ivPrev;

    private TextView[] mDots;
    private int mCurrentPage;
    private final Handler timerHandler = new Handler();
    private final Runnable timerRunnable = this::signIn;
    private final Runnable scrollBoard = new Runnable() {
        @Override
        public void run() {
            vpScreens.setCurrentItem(mCurrentPage + 1);
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        ButterKnife.bind(this);

        OnBoardingScreenAdapter onBoardingScreenAdapter = new OnBoardingScreenAdapter(this);
        vpScreens.setAdapter(onBoardingScreenAdapter);
        timerHandler.postDelayed(scrollBoard, 2000);
        dotsIndicator(0);
        vpScreens.addOnPageChangeListener(pageChangeListener);

        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            mScroller.set(vpScreens, new CustomScroller(this, 1000));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        vpScreens.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    timerHandler.removeCallbacks(scrollBoard);
                    return true;
                case MotionEvent.ACTION_UP:
                    if (mCurrentPage != mDots.length - 1) {
                        timerHandler.postDelayed(scrollBoard, 2000);
                    }
                    break;
            }
            return false;
        });
    }

    public void dotsIndicator(int position) {
        mDots = new TextView[4];
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
            if (position == mDots.length - 1) {
                bNext.setText(R.string.nav_sign_in);
                tvSkip.setVisibility(View.INVISIBLE);
                tvSkip.setEnabled(false);
                timerHandler.removeCallbacks(scrollBoard);
                timerHandler.postDelayed(timerRunnable, 3000);
            } else {
                removeAllCallbacks();
                timerHandler.postDelayed(scrollBoard, 2000);
                bNext.setText(R.string.button_next);
                tvSkip.setVisibility(View.VISIBLE);
                tvSkip.setEnabled(true);
            }

            if (position == 0) {
                ivPrev.setVisibility(View.INVISIBLE);
                ivPrev.setEnabled(false);
            } else {
                ivPrev.setVisibility(View.VISIBLE);
                ivPrev.setEnabled(true);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @SuppressLint("NonConstantResourceId")
    @OnClick(value = {R.id.b_next, R.id.iv_prev, R.id.tv_skip})
    public void userClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        switch (view.getId()) {
            case R.id.b_next:
                if (bNext.getText().equals("GO TO SIGN IN")) {
                    removeAllCallbacks();
                    signIn();
                } else vpScreens.setCurrentItem(mCurrentPage + 1);
                break;
            case R.id.iv_prev:
                vpScreens.setCurrentItem(mCurrentPage - 1);
                break;
            case R.id.tv_skip:
                removeAllCallbacks();
                signIn();
                break;
        }
    }

    private void signIn() {
        Intent intent = new Intent(OnBoardingActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void removeAllCallbacks() {
        timerHandler.removeCallbacks(timerRunnable);
        timerHandler.removeCallbacks(scrollBoard);
    }

    @Override
    protected void onStop() {
        super.onStop();
        removeAllCallbacks();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        removeAllCallbacks();
    }
}
