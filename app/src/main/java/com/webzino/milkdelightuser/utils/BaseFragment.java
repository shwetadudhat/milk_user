package com.webzino.milkdelightuser.utils;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


public class BaseFragment extends Fragment {
    AsyncProgressDialog ad;
    Toast toast;
    public void showDialog(String msg) {
        try {
            if (ad != null && ad.isShowing()) {
                return;
            }
            assert ad != null;
            ad.show(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            ad = AsyncProgressDialog.getInstant(getActivity());
            toast = Toast.makeText(getActivity(), "", Toast.LENGTH_LONG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void dismissDialog() {
        try {
            if (ad != null) {
                ad.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showToast(final String text) {
        if(getActivity() == null)
            return;
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                toast.setText(text);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    public void showToast(final String text, final int duration) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                toast.setText(text);
                toast.setDuration(duration);
                toast.show();
            }
        });
    }

    public void setTypeFaceBold(TextView txt){
        Typeface face = Typeface.createFromAsset(getContext().getAssets(), "fonts/ubuntu_bold.ttf");
        txt.setTypeface(face);
    }

    public void setTypeFaceRegular(TextView txt){
        Typeface face = Typeface.createFromAsset(getContext().getAssets(), "fonts/ubuntu_regular.ttf");
        txt.setTypeface(face);
    }

    private void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                textView.setTextColor(color);
                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(textView.getContext(), color), PorterDuff.Mode.SRC_IN));
            }
        }
    }
}
