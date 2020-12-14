package com.example.copwatch.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.example.copwatch.R;
import com.example.copwatch.service.Constants;
import com.example.copwatch.utils.AfterTextChangedWatcher;
import com.squareup.picasso.Picasso;

import java.io.File;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SIGNUP";
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_firstName)
    EditText etFirstName;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_lastName)
    EditText etLastName;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_email)
    EditText etEmail;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_phone)
    EditText etPhoneNumber;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_password)
    EditText etPassword;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_confirmPass)
    EditText etConfirmPass;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.iv_profile)
    ImageView ivProfile;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.cb_terms)
    CheckBox cbTerms;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_terms)
    TextView tvTerms;

    private RequestOptions options;
    private DrawableCrossFadeFactory factory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        factory = new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
        options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        cbTerms.setOnCheckedChangeListener((buttonView, isChecked) -> tvStatus.setVisibility(View.INVISIBLE));

        etFirstName.addTextChangedListener(new AfterTextChangedWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (etFirstName.getText().length() > 0) {
                    tvStatus.setVisibility(View.INVISIBLE);
                }
            }
        });
        etLastName.addTextChangedListener(new AfterTextChangedWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (etLastName.getText().length() > 0) {
                    tvStatus.setVisibility(View.INVISIBLE);
                }
            }
        });
        etPhoneNumber.addTextChangedListener(new AfterTextChangedWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (etPhoneNumber.getText().length() > 0) {
                    tvStatus.setVisibility(View.INVISIBLE);
                }
            }
        });
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
        etConfirmPass.addTextChangedListener(new AfterTextChangedWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (etConfirmPass.getText().length() > 0) {
                    tvStatus.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(value = {R.id.iv_prev, R.id.b_signUp, R.id.fl_profile, R.id.tv_terms})
    public void userClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        switch (view.getId()) {
            case R.id.iv_prev:
                onBackPressed();
                Constants.hideKeyboard(this);
                break;
            case R.id.b_signUp:
                //camera permission
                Constants.hideKeyboard(this);
                String phoneNumber = etPhoneNumber.getText().toString().trim();
                if (!TextUtils.isEmpty(phoneNumber)) {
                    etPhoneNumber.setText(PhoneNumberUtils.formatNumber(phoneNumber, "US"));
                }
                if (checkDataInputs()) {
                    if (!cbTerms.isChecked()) {
                        tvStatus.setText(R.string.error_terms_and_conditions);
                        tvStatus.setTextColor(ContextCompat.getColor(this, R.color.error_red));
                    } else {
                        tvStatus.setText(R.string.success_sign_up);
                        tvStatus.setTextColor(ContextCompat.getColor(this, R.color.purple_200));
                        goToHome();
                        clearAllFields();
                    }
                    tvStatus.setVisibility(View.VISIBLE);
                } else tvStatus.setVisibility(View.INVISIBLE);

                break;
            case R.id.fl_profile:
                verifyStoragePermissions(this);
                break;
            case R.id.tv_terms:
                Constants.showInputDialog(this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.OPEN_GALLERY_INTENT_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                String filePath = getPath(uri);
                File imgFile = new File(filePath);
                if (imgFile.exists()) {
                    Glide.with(SignUpActivity.this)
                            .asBitmap()
                            .transition(withCrossFade(factory))
                            .load(imgFile.getAbsolutePath())
                            .apply(options)
                            .into(ivProfile);
                }
                Log.e(TAG, "onActivityResult: " + filePath + " | " + imgFile.getAbsolutePath());
            }
        }
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        } else {
            Intent openGallery = new Intent(Intent.ACTION_PICK);
            openGallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(openGallery, Constants.OPEN_GALLERY_INTENT_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent openGallery = new Intent(Intent.ACTION_PICK);
                openGallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(openGallery, Constants.OPEN_GALLERY_INTENT_CODE);
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, uri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void goToHome() {
        Intent loginIntent = new Intent(SignUpActivity.this, HomeActivity.class);
        loginIntent.putExtra(Constants.LOGIN_MODE, Constants.DEFAULT);
        loginIntent.putExtra("first_name", etFirstName.getText().toString().trim());
        loginIntent.putExtra("last_name", etLastName.getText().toString().trim());
        loginIntent.putExtra("email", etEmail.getText().toString().trim());
        loginIntent.putExtra("phone_number", etPhoneNumber.getText().toString().trim());
        startActivity(loginIntent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void clearAllFields() {
        etFirstName.setText("");
        etLastName.setText("");
        etEmail.setText("");
        etPhoneNumber.setText("");
        etPassword.setText("");
        etConfirmPass.setText("");
        cbTerms.setChecked(false);
    }

    private boolean checkDataInputs() {
        boolean checked;
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPass = etConfirmPass.getText().toString().trim();

        if (TextUtils.isEmpty(firstName)) {
            etFirstName.setError(getString(R.string.error_first_name));
            return false;
        }

        if (TextUtils.isEmpty(lastName)) {
            etLastName.setError(getString(R.string.error_last_name));
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError(getString(R.string.error_email_address));
            return false;
        } else if (Constants.isValidEmail(email)) {
            return true;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError(getString(R.string.error_password));
            checked = false;
        } else if (password.length() < 6) {
            etPassword.setError(getString(R.string.error_password_limit));
            checked = false;
        } else if (TextUtils.isEmpty(confirmPass)) {
            etConfirmPass.setError(getString(R.string.error_confirm_password));
            checked = false;
        } else if (!password.equals(confirmPass)) {
            etConfirmPass.setError(getString(R.string.error_password_match));
            checked = false;
        } else {
            checked = true;
        }

        return checked;
    }
}