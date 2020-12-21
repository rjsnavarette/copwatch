package com.example.copwatch.activities;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.copwatch.R;
import com.example.copwatch.fragments.CameraFragment;
import com.example.copwatch.interfaces.TermsAccepted;
import com.example.copwatch.service.Constants;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity implements TermsAccepted {

    private static final String TAG = "HOME";
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.iv_profile)
    ImageView ivProfile;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_name)
    TextView tvName;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_phone)
    TextView tvPhone;

    private GoogleSignInClient mGoogleSignInClient;
    private String loginAccount;

    private boolean isCameraClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        SharedPreferences userData = getSharedPreferences(Constants.USERDATA, Context.MODE_PRIVATE);

        loginAccount = userData.getString(Constants.LOGIN_MODE, "");
        if (!loginAccount.equals("")) {
            switch (loginAccount) {
                case Constants.FACEBOOK:
                    if (AccessToken.getCurrentAccessToken() != null) {
                        loadUserProfile(AccessToken.getCurrentAccessToken());
                    }
                    break;
                case Constants.GOOGLE:
                    if (account != null) {
                        updateUI(account.getPhotoUrl(), account.getGivenName() + " " + account.getFamilyName(),
                                account.getEmail(), "");
                    }
                    break;
                default:
                    updateUI(null, userData.getString(Constants.FIRST_NAME, "") + " " +
                                    userData.getString(Constants.LAST_NAME, ""), userData.getString(Constants.EMAIL_ADDRESS, ""),
                            userData.getString(Constants.PHONE_NUMBER, ""));
                    break;
            }
        }
    }

    @SuppressLint({"NonConstantResourceId", "QueryPermissionsNeeded"})
    @OnClick(value = {R.id.cv_camera, R.id.cv_preferences, R.id.iv_logout})
    public void userClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        switch (view.getId()) {
            case R.id.cv_camera:
//                Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                if (videoIntent.resolveActivity(getPackageManager()) != null) {
//                    startActivityForResult(videoIntent, Constants.VIDEO_CAMERA_INTENT_CODE);
//                }
                AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, CameraFragment.newInstance())
                        .commit();
                isCameraClicked = true;
                break;
            case R.id.cv_preferences:
                Intent prefIntent = new Intent(HomeActivity.this, PreferencesActivity.class);
                prefIntent.putExtra("isHomeAccess", true);
                startActivity(prefIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.fade_out);
                break;
            case R.id.iv_logout:
                Constants.showInputDialog(this, 2);
                break;
        }
    }

    private void loadUserProfile(AccessToken newAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, (object, response) -> {
            Log.e(TAG, "Facebook: " + response.toString());
            try {
//                JSONObject picture = new JSONObject(String.valueOf(response.getJSONObject())).getJSONObject("picture");
//                JSONObject jsonObject = picture.getJSONObject("data");
//                String ppUrl = jsonObject.getString("url");
                String ppUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");
//                URL ppUrl = new URL("https://graph.facebook.com/" + object.getString("id") + "/picture?type=large");
                String first = object.getString("first_name");
                String last = object.getString("last_name");
                String email = object.getString("email");

                updateUI(Uri.parse(ppUrl), first + " " + last, email, "");

            } catch (JSONException e) {
                Log.e(TAG, "Facebook: " + e.toString());
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,email,picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();
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

    private void updateUI(Uri uri, String name, String email, String phone) {
        Log.e(TAG, "updateUI: " + uri + " " + name + " " + email + " " + phone);
        Picasso.get().load(uri).noFade().fit().centerCrop().placeholder(R.drawable.blank_profile).into(ivProfile);
        tvName.setText(name);
        tvEmail.setText(email);
        tvPhone.setText(phone);

        tvName.setVisibility(View.VISIBLE);
        tvEmail.setVisibility(View.VISIBLE);
        tvPhone.setVisibility(View.VISIBLE);
    }

    private void backToHome() {
        Intent logoutIntent = new Intent(HomeActivity.this, SignInActivity.class);
        startActivity(logoutIntent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        if (!isCameraClicked) {
            Constants.showInputDialog(this, 2);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else {
            getFragmentManager().beginTransaction().remove(CameraFragment.closeInstance()).commit();
            isCameraClicked = false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.VIDEO_CAMERA_INTENT_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    Uri videoUri = data.getData();
                    Log.e(TAG, "onActivityResult: " + videoUri);
                }
                break;
        }
    }

    @Override
    public void onTermsAccepted() {
        logout();
    }

}
