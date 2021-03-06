package com.example.copwatch.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.copwatch.R;
import com.example.copwatch.adapter.TermsAndConditionsAdapter;
import com.example.copwatch.interfaces.TermsAccepted;
import com.example.copwatch.utils.CustomScroller;

import java.lang.reflect.Field;

import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InputDialog extends Dialog {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rl_main)
    RelativeLayout rlMain;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.vp_terms)
    ViewPager vpTerms;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ll_dots)
    LinearLayout llDots;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_dialog)
    TextView tvDialog;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.b_accept)
    Button bAccept;

    private TextView[] mDots;
    private int mCurrentPage;
    private int statusCode;
    private final TermsAccepted activity;
    private final Handler timerHandler = new Handler();
    private final Runnable scrollBoard = new Runnable() {
        @Override
        public void run() {
            vpTerms.setCurrentItem(mCurrentPage + 1);
        }
    };

    public InputDialog(Context context, int themeResId, int statusCode) {
        super(context, themeResId);

        setContentView(R.layout.dialog_inputs);
        ButterKnife.bind(this);
        if (getWindow() != null) {
            getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        }

        this.activity = (TermsAccepted) context;

        switch (statusCode) {
            case 1:
                tvDialog.setVisibility(View.GONE);
                rlMain.setVisibility(View.VISIBLE);
                llDots.setVisibility(View.VISIBLE);
                bAccept.setText(R.string.button_next);
                TermsAndConditionsAdapter termsAndConditionsAdapter = new TermsAndConditionsAdapter(getContext());
                vpTerms.setAdapter(termsAndConditionsAdapter);
                vpTerms.addOnPageChangeListener(pageChangeListener);
                tvTitle.setText(R.string.head_terms_and_conditions);
                timerHandler.postDelayed(scrollBoard, 2000);
                dotsIndicator(0);
                break;
            case 2:
                tvDialog.setVisibility(View.VISIBLE);
                tvTitle.setText(R.string.head_logout);
                rlMain.setVisibility(View.GONE);
                llDots.setVisibility(View.INVISIBLE);
                bAccept.setText(R.string.button_confirm);
                break;
        }

        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            mScroller.set(vpTerms, new CustomScroller(context, 1000));
        } catch (Exception e) {
            Log.e("DIALOG", e.getMessage());
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(value = {R.id.iv_close, R.id.iv_prev, R.id.b_accept})
    public void userClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        switch (view.getId()) {
            case R.id.iv_prev:
                if (mCurrentPage != 0) {
                    vpTerms.setCurrentItem(mCurrentPage - 1);
                } else {
                    dismiss();
                }
                break;
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.b_accept:
                if (bAccept.getText().equals("Accept")) {
                    activity.onTermsAccepted();
                    dismiss();
                } else vpTerms.setCurrentItem(mCurrentPage + 1);

                if (bAccept.getText().equals("Confirm")) {
                    activity.onTermsAccepted();
                    dismiss();
                }
                break;
            default:
                break;

        }
    }

    public void dotsIndicator(int position) {
        mDots = new TextView[2];
        llDots.removeAllViews();

        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(getContext());
            mDots[i].setText(Html.fromHtml("&#8226;", HtmlCompat.FROM_HTML_MODE_LEGACY));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(ContextCompat.getColor(getContext(), R.color.purple_200));

            llDots.addView(mDots[i]);
        }

        if (mDots.length > 0) {
            mDots[position].setTextColor(ContextCompat.getColor(getContext(), R.color.purple_500));
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
                ivClose.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.close_icon));
                tvTitle.setText(R.string.head_privacy_policy);
                bAccept.setText(R.string.button_accept);
                timerHandler.removeCallbacks(scrollBoard);
            } else {
                tvTitle.setText(R.string.head_terms_and_conditions);
                bAccept.setText(R.string.button_next);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
