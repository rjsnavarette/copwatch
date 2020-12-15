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
import android.widget.TextView;

import com.example.copwatch.R;
import com.example.copwatch.adapter.TermsAndConditionsAdapter;
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
    @BindView(R.id.vp_terms)
    ViewPager vpTerms;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ll_dots)
    LinearLayout llDots;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.b_accept)
    Button bAccept;

    private TextView[] mDots;
    private int mCurrentPage;
    private final AcceptClicked activity;
    private final Handler timerHandler = new Handler();
    private final Runnable scrollBoard = new Runnable() {
        @Override
        public void run() {
            vpTerms.setCurrentItem(mCurrentPage + 1);
        }
    };

    public InputDialog(Context context, int themeResId) {
        super(context, themeResId);

        setContentView(R.layout.dialog_inputs);
        ButterKnife.bind(this);
        if (getWindow() != null) {
            getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        }

        this.activity = (AcceptClicked) context;

        TermsAndConditionsAdapter termsAndConditionsAdapter = new TermsAndConditionsAdapter(getContext());
        vpTerms.setAdapter(termsAndConditionsAdapter);
        vpTerms.addOnPageChangeListener(pageChangeListener);
        timerHandler.postDelayed(scrollBoard, 2000);
        dotsIndicator(0);

        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            mScroller.set(vpTerms, new CustomScroller(context, 1000));
        } catch (Exception e) {
            Log.e("DIALOG", e.getMessage());
        }
    }

    public interface AcceptClicked {
        void onAcceptClicked();
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
                    activity.onAcceptClicked();
                    dismiss();
                } else vpTerms.setCurrentItem(mCurrentPage + 1);
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
                tvTitle.setText("Privacy Policy");
                bAccept.setText("Accept");
                timerHandler.removeCallbacks(scrollBoard);
            } else {
                tvTitle.setText("Terms and Conditions");
                bAccept.setText("Next");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
