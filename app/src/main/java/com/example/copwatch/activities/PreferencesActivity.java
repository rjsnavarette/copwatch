package com.example.copwatch.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
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
import com.example.copwatch.interfaces.PreferencesCheckListener;
import com.example.copwatch.interfaces.TermsAccepted;
import com.example.copwatch.service.Constants;
import com.example.copwatch.service.DriveServiceHelper;
import com.example.copwatch.utils.CustomScroller;
import com.example.copwatch.utils.CustomViewPager;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.lang.reflect.Field;
import java.util.Collections;

public class PreferencesActivity extends AppCompatActivity implements PreferencesCheckListener, TermsAccepted {

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

    private GoogleSignInClient mGoogleSignInClient;
    private DriveServiceHelper mDriveServiceHelper;

    private String loginAccount;
    private boolean isHomeAccess;
    private TextView[] mDots;
    private boolean isChecked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prehome);
        ButterKnife.bind(this);

        loginAccount = getIntent().getStringExtra(Constants.LOGIN_MODE);
        isHomeAccess = getIntent().getBooleanExtra("isHomeAccess", false);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        preferencesAdapter = new PreferencesAdapter(this);
        vpScreens.setAdapter(preferencesAdapter);
        if (isHomeAccess) {
            llDots.setVisibility(View.INVISIBLE);
            vpScreens.setCurrentItem(1, false);
            mCurrentPage = 1;
            bNext.setText("Ok");
            Log.e(TAG, "onCreate: " + mCurrentPage);
        }
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
                } else if (mCurrentPage == 1 && isHomeAccess) {
                    Intent mainIntent = new Intent(PreferencesActivity.this, HomeActivity.class);
                    startActivity(mainIntent);
                    finish();
                    overridePendingTransition(0, R.anim.slide_out_left);
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

    private void logout() {
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

    @Override
    public void onBackPressed() {
        if (!isHomeAccess) {
            Constants.showInputDialog(this, 2);
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.fade_in, R.anim.slide_out_left);
        }
    }

    @Override
    public void onModeSelected(boolean isModeChecked) {
        isChecked = isModeChecked;
        if (!isModeChecked) {
            tvStatus.setText("Please select mode");
            tvStatus.setVisibility(View.VISIBLE);
        } else {
            tvStatus.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onRadioButtonChecked(String radioButtonText) {
        switch (radioButtonText) {
            case "Google Drive":
                startActivityForResult(mGoogleSignInClient.getSignInIntent(), Constants.GOOGLE_SIGN_IN_INTENT_CODE);
                break;
            case "iCloud":
                Intent iCloudIntent = new Intent(Intent.ACTION_WEB_SEARCH);
                iCloudIntent.putExtra(SearchManager.QUERY, "https://www.icloud.com/");
                startActivityForResult(iCloudIntent, Constants.ICLOUD_WEB_INTENT_CODE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.GOOGLE_SIGN_IN_INTENT_CODE:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    GoogleSignIn.getSignedInAccountFromIntent(data)
                            .addOnSuccessListener(googleAccount -> {
                                Log.d(TAG, "Signed in as " + googleAccount.getEmail());

                                // Use the authenticated account to sign in to the Drive service.
                                GoogleAccountCredential credential =
                                        GoogleAccountCredential.usingOAuth2(
                                                this, Collections.singleton(DriveScopes.DRIVE_FILE));
                                credential.setSelectedAccountName(googleAccount.getAccount().toString());
                                Drive googleDriveService = new Drive.Builder(
                                        new NetHttpTransport(),
                                        new GsonFactory(),
                                        credential)
                                        .setApplicationName("Drive API Migration")
                                        .build();

                                // The DriveServiceHelper encapsulates all REST API and SAF functionality.
                                // Its instantiation is required before handling any onClick actions.
                                mDriveServiceHelper = new DriveServiceHelper(googleDriveService);
                                vpScreens.setCurrentItem(mCurrentPage + 1);
                            })
                            .addOnFailureListener(exception -> {
                                Log.e(TAG, "Unable to sign in.", exception);
                                preferencesAdapter.clearAllCheck();
                            });
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Log.e(TAG, "Sign-in cancelled");
                    preferencesAdapter.clearAllCheck();
                }
                break;
            case Constants.ICLOUD_WEB_INTENT_CODE:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    vpScreens.setCurrentItem(mCurrentPage + 1);
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Log.e(TAG, "Sign-in cancelled");
                    preferencesAdapter.clearAllCheck();
                }
                break;


        }
    }

    @Override
    public void onTermsAccepted() {
        logout();
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}