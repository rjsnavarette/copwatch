package com.example.copwatch.activities;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.copwatch.R;
import com.example.copwatch.service.Constants;
import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {

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

        loginAccount = getIntent().getStringExtra(Constants.LOGIN_MODE);
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
                updateUI(null, getIntent().getStringExtra("first_name") + " " +
                                getIntent().getStringExtra("last_name"), getIntent().getStringExtra("email"),
                        getIntent().getStringExtra("phone_number"));
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(value = {R.id.cv_logout})
    public void userClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        switch (view.getId()) {
            case R.id.cv_logout:
                logout();
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

    private void updateUI(Uri uri, String name, String email, String phone) {
        Log.e(TAG, "updateUI: " + uri + " " + name + " " + email + " " + phone);
        Picasso.get().load(uri).noFade().fit().centerCrop().placeholder(R.drawable.blank_profile).into(ivProfile);
        tvName.setText(name);
        tvEmail.setText(email);
        tvPhone.setText(phone);
    }

    private void backToHome() {
        Intent logoutIntent = new Intent(HomeActivity.this, SignInActivity.class);
        startActivity(logoutIntent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        logout();
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
