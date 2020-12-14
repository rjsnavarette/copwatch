package com.example.copwatch.activities;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.copwatch.R;
import com.example.copwatch.service.Constants;


public class ForgotPasswordActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_email)
    EditText etEmail;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_status)
    TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(value = {R.id.b_sendLink, R.id.iv_prev})
    public void userClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        switch (view.getId()) {
            case R.id.b_sendLink:
                Constants.hideKeyboard(this);
                if (!validEmailInputs()) {
                    tvStatus.setVisibility(View.VISIBLE);
                    etEmail.setText("");
                }
                break;
            case R.id.iv_prev:
                onBackPressed();
                Constants.hideKeyboard(this);
                break;
        }
    }

    private boolean validEmailInputs() {
        if (etEmail.getText().length() > 0) {
            String userName = etEmail.getText().toString().trim();
            if (Constants.isValidEmail(userName)) {
                return false;
            } else {
                etEmail.setError(getString(R.string.error_invalid_email));
                return true;
            }
        } else {
            etEmail.setError(getString(R.string.error_email_address));
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}