package com.example.copwatch.service;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.copwatch.R;
import com.example.copwatch.dialogs.InputDialog;

public class Constants {

    public static final int OPEN_GALLERY_INTENT_CODE = 1000;
    public static final int GOOGLE_SIGN_IN_INTENT_CODE = 1001;
    public static final int VIDEO_CAMERA_INTENT_CODE = 1002;
    public static final int ICLOUD_WEB_INTENT_CODE = 1003;
    public static final int NOTIFICATION_ALARMS_INTENT_CODE = 1004;

    public static final int PERMISSION_REQUEST_CAMERA = 2000;

    public static final String LOGIN_MODE = "Login";
    public static final String FACEBOOK = "Facebook";
    public static final String GOOGLE = "Google";
    public static final String DEFAULT = "Default";

    public static String USERDATA = "user";
    public static String FIRST_NAME = "first_name";
    public static String LAST_NAME = "last_name";
    public static String EMAIL_ADDRESS = "email_address";
    public static String PHONE_NUMBER = "phone_number";

    public static final String CAMERA_FRONT = "1";
    public static final String CAMERA_BACK = "0";

    private static InputDialog visitedDialog;

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static void showInputDialog(Context mCon, int statusCode) {
        visitedDialog = new InputDialog(mCon, R.style.CustomDialog, statusCode);
        if (visitedDialog.getWindow() != null) {
            visitedDialog.getWindow().
                    setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        visitedDialog.show();
    }

    public static void closeInputDialog(Context context) {
        if (visitedDialog != null) {
            visitedDialog.dismiss();
            visitedDialog = null;
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
