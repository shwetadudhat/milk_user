package com.webzino.milkdelightuser.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class InstallReferrerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent == null) {
            return;
        }

        String referrerId = intent.getStringExtra("referrer");
        Log.e("referal123",referrerId);

        if (referrerId == null) {
            return;
        }
    }
}
