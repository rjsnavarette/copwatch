package com.example.copwatch.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.copwatch.R;
import com.example.copwatch.service.Constants;
import com.example.copwatch.utils.AfterTextChangedWatcher;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;

public class SignInActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_email)
    EditText etEmail;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_password)
    EditText etPassword;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_status)
    TextView tvStatus;

    private static final String TAG = "SIGN-IN";
    private Context mCon;

    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private Handler timerHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        mCon = this;

        callbackManager = CallbackManager.Factory.create();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        etEmail.addTextChangedListener(new AfterTextChangedWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (etEmail.getText().length() > 0) {
                    tvStatus.setVisibility(View.INVISIBLE);
                }
            }
        });
        etPassword.addTextChangedListener(new AfterTextChangedWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (etPassword.getText().length() > 0) {
                    tvStatus.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(value = {R.id.b_signIn, R.id.b_facebook, R.id.b_google, R.id.tv_forgotPass, R.id.tv_register})
    public void userClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        switch (view.getId()) {
            case R.id.b_signIn:
                Constants.hideKeyboard(this);
                if (!validEmailInputs() && !validPasswordInputs()) {
                    goToHome(Constants.DEFAULT);
                } else goToHome("");
                break;
            case R.id.b_facebook:
                findViewById(R.id.b_facebook).setEnabled(false);

                LoginManager.getInstance().logInWithReadPermissions(SignInActivity.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        Log.e(TAG, "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                        goToHome(Constants.FACEBOOK);

                    }

                    @Override
                    public void onCancel() {

                        Log.e(TAG, "facebook:onCancel");

                    }

                    @Override
                    public void onError(FacebookException error) {

                        Log.e(TAG, "facebook:onError: " + error.getMessage());
                        goToHome("");

                    }
                });
                break;
            case R.id.b_google:
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, Constants.GOOGLE_SIGN_IN_INTENT_CODE);
                break;
            case R.id.tv_forgotPass:
                Intent forgotIntent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
                startActivity(forgotIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.tv_register:
                Intent registerIntent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(registerIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.GOOGLE_SIGN_IN_INTENT_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.e(TAG, "handleFacebookAccessToken:" + token);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            goToHome(Constants.GOOGLE);
        } catch (ApiException e) {
            Log.e(TAG, "signInResult:failed code=" + e.getStatusCode());
            goToHome("");
        }
    }

    private void goToHome(String login) {
        if (!login.equals("")) {
            Log.e(TAG, "Success " + login);
            Intent loginIntent = new Intent(SignInActivity.this, HomeActivity.class);
            loginIntent.putExtra(Constants.LOGIN_MODE, login);
            startActivity(loginIntent);
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else {
            tvStatus.setText(R.string.error_login);
            tvStatus.setVisibility(View.VISIBLE);
            tvStatus.setTextColor(ContextCompat.getColor(this, R.color.error_red));
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

    private boolean validPasswordInputs() {
        if (etPassword.getText().length() > 0) {
            String password = etPassword.getText().toString().trim();
            if (password.length() < 6) {
                etPassword.setError(getString(R.string.error_password_limit));
                return true;
            } else {
                return false;
            }
        } else {
            etPassword.setError(getString(R.string.error_password));
            return true;
        }
    }

    private void clearFields() {
        etEmail.setError(null);
        etPassword.setError(null);
        etEmail.setText("");
        etPassword.setText("");
        tvStatus.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.hideKeyboard(this);
        clearFields();
    }
}