package com.example.copwatch.adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.copwatch.R;
import com.example.copwatch.interfaces.PreferencesCheckListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

public class PreferencesAdapter extends PagerAdapter {

    private final LayoutInflater mLayoutInflater;
    private final PreferencesCheckListener activity;
    private final int[] screens = {R.layout.activity_select_storage, R.layout.activity_preferences, R.layout.activity_mode_selection,
            R.layout.activity_permissions, R.layout.activity_advance_permissions, R.layout.activity_legal_disclaimer};

    public PreferencesAdapter(Context context) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.activity = (PreferencesCheckListener) context;
    }

    @Override
    public int getCount() {
        return screens.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((ConstraintLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View screenOne = mLayoutInflater.inflate(screens[0], container, false);
        View screenTwo = mLayoutInflater.inflate(screens[1], container, false);
        View screenThree = mLayoutInflater.inflate(screens[2], container, false);
        View screenFour = mLayoutInflater.inflate(screens[3], container, false);
        View screenFive = mLayoutInflater.inflate(screens[4], container, false);
        View screenSix = mLayoutInflater.inflate(screens[5], container, false);

        switch (position) {
            case 0:
                RadioGroup rgStorage = screenOne.findViewById(R.id.rg_storage);
                rgStorage.setOnCheckedChangeListener((group, checkedId) -> {
                    RadioButton rbStorage = group.findViewById(checkedId);
                    rbStorage.performClick();
                    activity.onRadioButtonChecked(rbStorage.getText().toString());
                });
                break;
            case 1:
                SwitchCompat swRecord = screenTwo.findViewById(R.id.sc_record);
                SwitchCompat swDim = screenTwo.findViewById(R.id.sc_dim);
                SwitchCompat swDisturb = screenTwo.findViewById(R.id.sc_disturb);
                SwitchCompat swAudio = screenTwo.findViewById(R.id.sc_audio);
                SwitchCompat swVoice = screenTwo.findViewById(R.id.sc_voice);
                SwitchCompat swCamera = screenTwo.findViewById(R.id.sc_camera);
                TextView tvCamera = screenTwo.findViewById(R.id.tv_camera);
                swCamera.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        tvCamera.setText("Camera Selection (Rear)");
                    } else {
                        tvCamera.setText("Camera Selection (Front)");
                    }
                });
                break;
            case 2:
                CheckBox cbStandard = screenThree.findViewById(R.id.cb_standard);
                cbStandard.setChecked(true);
                cbStandard.setOnCheckedChangeListener((buttonView, isChecked) -> activity.onModeSelected(isChecked));
                break;
            case 3:
                CheckBox cbCamera = screenFour.findViewById(R.id.cb_camera);
                CheckBox cbCloud = screenFour.findViewById(R.id.cb_cloud);
                CheckBox cbLock = screenFour.findViewById(R.id.cb_lock);
                CheckBox cbInterface = screenFour.findViewById(R.id.cb_interface);

                cbCamera.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    }
                });
                break;
            case 4:
                RadioGroup rgSelect = screenFive.findViewById(R.id.rg_select);
                break;
        }

        View[] screenViews = {screenOne, screenTwo, screenThree, screenFour, screenFive, screenSix};

        container.addView(screenViews[position]);

        return screenViews[position];
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
