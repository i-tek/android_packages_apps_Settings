/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.settings.wifi;

import static com.android.settings.wifi.ConfigureWifiSettings.WIFI_WAKEUP_REQUEST_CODE;

import android.app.Fragment;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.VisibleForTesting;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.text.TextUtils;

import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.utils.AnnotationSpan;
import com.android.settingslib.core.AbstractPreferenceController;

/**
 * {@link PreferenceControllerMixin} that controls whether the requests for Wi-Fi scans should be
 * throttled.
 */
public class ThrottleWifiScanningPreferenceController extends AbstractPreferenceController {

    private static final String TAG = "WifiScanThrottlingController";
    private static final String KEY_ENABLE_WIFI_THROTTLE_SCANNING = "throttle_wifi_scanning";

    private final Fragment mFragment;
    private final Context mContext;

    @VisibleForTesting
    SwitchPreference mPreference;

    public ThrottleWifiScanningPreferenceController(Context context, DashboardFragment fragment) {
        super(context);
        mFragment = fragment;
        mContext = context;
    }

    @Override
    public void displayPreference(PreferenceScreen screen) {
        super.displayPreference(screen);
        mPreference = (SwitchPreference) screen.findPreference(KEY_ENABLE_WIFI_THROTTLE_SCANNING);
        updateState(mPreference);
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!TextUtils.equals(preference.getKey(), KEY_ENABLE_WIFI_THROTTLE_SCANNING)
            || !(preference instanceof SwitchPreference)) {
            return false;
        }

        setWifiScanThrottlingEnabled(!getWifiScanThrottlingEnabled());

        updateState(mPreference);
        return true;
    }

    @Override
    public String getPreferenceKey() {
        return KEY_ENABLE_WIFI_THROTTLE_SCANNING;
    }

    @Override
    public void updateState(Preference preference) {
        if (!(preference instanceof SwitchPreference)) {
            return;
        }
        final SwitchPreference enableWifiThrottle = (SwitchPreference) preference;
        enableWifiThrottle.setChecked(getWifiScanThrottlingEnabled());
    }

    public void onActivityResult(int requestCode, int resultCode) {
//        if (requestCode != WIFI_WAKEUP_REQUEST_CODE) {
//            return;
//        }
//        if (mLocationManager.isLocationEnabled()) {
//            setWifiWakeupEnabled(true);
//        }
//        updateState(mPreference);
    }

    private boolean getWifiScanThrottlingEnabled() {
        return Settings.Global.getInt(mContext.getContentResolver(),
                Settings.Global.WIFI_SCAN_THROTTLING_ENABLED, 0) == 1;
    }

    private void setWifiScanThrottlingEnabled(boolean enabled) {
        Settings.Global.putInt(mContext.getContentResolver(), Settings.Global.WIFI_SCAN_THROTTLING_ENABLED,
                enabled ? 1 : 0);
    }
}
