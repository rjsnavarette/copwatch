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

    public static final int OPEN_GALLERY_INTENT_CODE = 100;
    public static final int GOOGLE_SIGN_IN_INTENT_CODE = 101;

    public static final String LOGIN_MODE = "Login";
    public static final String FACEBOOK = "Facebook";
    public static final String GOOGLE = "Google";
    public static final String DEFAULT = "Default";

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static void showInputDialog(Context mCon) {
        InputDialog visitedDialog = new InputDialog(mCon, R.style.CustomDialog);
        if (visitedDialog.getWindow() != null) {
            visitedDialog.getWindow().
                    setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        visitedDialog.show();
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
