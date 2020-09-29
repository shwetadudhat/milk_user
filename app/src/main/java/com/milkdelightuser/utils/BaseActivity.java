package com.milkdelightuser.utils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.milkdelightuser.Adapter.SpinnerAdapter;
import com.milkdelightuser.R;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class BaseActivity extends AppCompatActivity {
    AsyncProgressDialog ad;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

    }

    public void showDialog(String msg) {
        try {
            if (ad != null && ad.isShowing()) {
                return;
            }

            ad = AsyncProgressDialog.getInstant(getActivity());
            ad.show(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMessage(String msg) {
        try {
            ad.setMessage(msg);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean handled = super.onKeyDown(keyCode, event);

        //EAT THE LONG PRESS EVENT SO THE KEYBOARD DOESN'T COME UP.
        if (keyCode == KeyEvent.KEYCODE_MENU && event.isLongPress()) {
            return true;
        }

        return handled;
    }

    Toast toast;

    public void showToast(final String text, final int duration) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toast.setText(text);
                toast.setDuration(duration);
                toast.show();
            }
        });
    }

    public void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toast.setText(text);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }


    public void showSpinnerSel(String title, TextView tv, ArrayList<Spinner1> data, boolean isFilterable, SpinnerCallback callback) {
        Dialog a = new Dialog(getActivity());
        Window w = a.getWindow();
        a.requestWindowFeature(Window.FEATURE_NO_TITLE);
        a.setContentView(R.layout.spinner_sel);
        w.setBackgroundDrawableResource(android.R.color.transparent);

        ImageView ivRowClose = w.findViewById(R.id.ivRowClose);
        ivRowClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a.dismiss();
            }
        });

        TextView lblselect = w.findViewById(R.id.dialogtitle);
        lblselect.setText(title);
        lblselect.setText(title.replace("*", "").trim());

        EditText editSearch = w.findViewById(R.id.editSearch);
        if (isFilterable) {
            editSearch.setVisibility(View.VISIBLE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Global.showKeyBoard(getActivity(), editSearch);
                }
            }, 500);
        } else {
            editSearch.setVisibility(View.GONE);
        }

        String selectedStr = "";


        if (!selectedStr.isEmpty()) {
            ArrayList<String> selected = Global.asList(selectedStr.replace("'", ""));


            if (!selected.isEmpty()) {
                for (int i = 0; i < data.size(); i++) {
                    if (selected.contains(data.get(i).ID)) {
                        data.get(i).setSelected(true);
                    } else {
                        data.get(i).setSelected(false);
                    }
                }
            }
        }

        SpinnerAdapter adapter=new SpinnerAdapter(getActivity());
        adapter.setFilterable(isFilterable);
        ListView lv = w.findViewById(R.id.lvSpinner);
        lv.setAdapter(adapter);
        adapter.addAll(data);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.changeSelection(position, false);
                Global.hideKeyBoard(getActivity(), view);

               /* ArrayList<Spinner1> selList = adapter.getSelectedAll();
                Log.e("selList123",selList.toString());
                tv.setText(adapter.getSelectedTitle());
                tv.setTag(adapter.getSelectedIds());

                if (selList.size() > 0) {
                    tv.setTag(adapter.getSelectedIdArray());
                } else {
                    tv.setTag(null);
                }

                callback.onDone(selList);
                a.dismiss();*/
            }
        });

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().length() >= 1) {
                    adapter.getFilter().filter(editable.toString().trim());
                } else {
                    adapter.getFilter().filter("");
                }
            }
        });

        Button btnSpinnerOk = w.findViewById(R.id.btnPositive);
        btnSpinnerOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Spinner1> selList = adapter.getSelectedAll();
                Log.e("selList",selList.toString());
                tv.setText(adapter.getSelectedTitle());
                tv.setTag(adapter.getSelectedIds());

                if (selList.size() > 0) {
                    tv.setTag(adapter.getSelectedIdArray());

                } else {
                    tv.setTag(null);
                }

                callback.onDone(selList);
                a.dismiss();
            }
        });

        Button btnSpinnerCancel = w.findViewById(R.id.btnNegative);
        btnSpinnerCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a.dismiss();
            }
        });
        a.show();
    }
    //endregion


    public Fragment getViewPagerFragment(int viewPagerId, int position) {
        Fragment fragment = (Fragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + viewPagerId + ":" + position);
        return fragment;
    }

    public Fragment getFragment(int frameLayoutId) {
        Fragment fragment = (Fragment) getSupportFragmentManager().findFragmentById(frameLayoutId);
        return fragment;
    }

    //region FOR REPLACE FRAGMENT
    public void replaceFragment(int id, Fragment fragment, boolean isForBackStack) {
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(id, fragment);
        if (isForBackStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }
    //endregion

    //region FOR SHOW BIG IMAGE POPUP
   /* public void showBigImagePopup(int image, String imagePath, String imageURL, String title) {
        final LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.row_big_image, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
//        dialog.window.attributes.windowAnimations = R.style.FadIn_FadOutAnimation
        dialog.setView(alertLayout);

        ImageView ivRowZoomImage = alertLayout.findViewById(R.id.ivRowZoomImage);
        ImageView ivRowClose = alertLayout.findViewById(R.id.ivRowClose);
        TextView tvRowBigImageTitle = alertLayout.findViewById(R.id.tvRowBigImageTitle);

        if (!TextUtils.isEmpty(title)){
            tvRowBigImageTitle.setText(title);
        }

        if (image != -1) {
            ivRowZoomImage.setImageResource(image);
        }

        if (!TextUtils.isEmpty(imagePath)) {
            ivRowZoomImage.setImageURI(Uri.fromFile(new File(imagePath)));
        }

        if (!TextUtils.isEmpty(imageURL)) {
            Glide.with(this)
                    .asBitmap()
                    .load(imageURL)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            ivRowZoomImage.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }

        ivRowClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }*/
    //endregion

    //region FOR CHECK WHETHER INTERNET CONNECTED OR NOT
    public  boolean isInternetConnected() {
        if (Global.isInternetConnected(getActivity())) {
            return true;
        } else {
            showInternetConnectionDialog();
            return false;
        }
    }
    //endregion

    public BaseActivity getActivity() {
        return this;
    }

    public void startNewActivity(Intent i) {
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    //region FOR SHOW INTERNET CONNECTION DIALOG
    private void showInternetConnectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(getActivity().getResources().getString(R.string.no_internet_connection_with_emoji))
                .setMessage(getActivity().getResources().getString(R.string.connection_not_available))
                .setPositiveButton(getActivity().getResources().getString(R.string.btn_enable), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        try {
                            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            startNewActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                })
                .setNegativeButton(getActivity().getResources().getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    //endregion

    /*public void showSpinnerSel(String title, TextView tv, ArrayList<Spinner> data, boolean isFilterable, SpinnerCallback callback) {
        Dialog a = new Dialog(getActivity());
        Window w = a.getWindow();
        a.requestWindowFeature(Window.FEATURE_NO_TITLE);
        a.setContentView(R.layout.spinner_sel);
        w.setBackgroundDrawableResource(android.R.color.transparent);

        ImageView ivRowClose = w.findViewById(R.id.ivRowClose);
        ivRowClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a.dismiss();
            }
        });

        TextView lblselect = w.findViewById(R.id.dialogtitle);
        lblselect.setText(title);
        lblselect.setText(title.replace("*", "").trim());

        //        TextView dialogClear = (TextView) w.findViewById(R.id.dialogClear);
        //        dialogClear.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View view) {
        //                tv.setText("");
        //                tv.setTag(null);
        //                a.dismiss();
        //            }
        //        });

        EditText editSearch = w.findViewById(R.id.editSearch);
        if (isFilterable) {
            editSearch.setVisibility(View.VISIBLE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Global.showKeyBoard(getActivity(), editSearch);
                }
            }, 500);
        } else {
            editSearch.setVisibility(View.GONE);
        }

        //        EditText editSearch = (EditText) w.findViewById(R.id.editSearch);
        //        if (isFilterable) {
        //            editSearch.setVisibility(View.VISIBLE);
        //        } else {
        //            editSearch.setVisibility(View.GONE);
        //        }

        String selectedStr = "";

        //        if (tv == editIntensity) {
        //            selectedStr = stoneParam.FancycolorIntensityList;
        //        }
        //        if (tv == editColor) {
        //            selectedStr = stoneParam.FancyColorList;
        //        }
        //        if (tv == editOvertone) {
        //            selectedStr = stoneParam.FancycolorOvertoneList;
        //        }

        if (!selectedStr.isEmpty()) {
            ArrayList<String> selected = Global.asList(selectedStr.replace("'", ""));

            if (!selected.isEmpty()) {
                for (int i = 0; i < data.size(); i++) {
                    if (selected.contains(data.get(i).ID)) {
                        data.get(i).setSelected(true);
                    } else {
                        data.get(i).setSelected(false);
                    }
                }
            }
        }


        SpinnerSelAdapter adapter = new SpinnerSelAdapter(getActivity());
        adapter.setFilterable(isFilterable);
        ListView lv = w.findViewById(R.id.lvSpinner);
        lv.setAdapter(adapter);
        adapter.addAll(data);

        //        if (tv.getTag().toString().trim()!= null){
        //            for (int i = 0; i <data.size() ; i++) {
        //                if (tv.getTag().toString().trim().equals(data.get(i).ID)){
        //                    Debug.e("Game Id" , data.get(i).ID);
        //                }
        //            }
        //        }


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.changeSelection(position, true);
            }
        });

        //        for (int i = 0; i <data.size() ; i++) {
        //            if (adapter.isSelectedAtleastOne()){
        //                adapter.changeSelection(Integer.valueOf(data.get(i).ID), true);
        //            }else{
        //                adapter.changeSelection(Integer.valueOf(data.get(i).ID), false);
        //            }
        //        }

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().length() >= 1) {
                    adapter.getFilter().filter(editable.toString().trim());
                } else {
                    adapter.getFilter().filter("");
                }
            }
        });

        //        editSearch.addTextChangedListener(new TextWatcher() {
        //            @Override
        //            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //
        //            }
        //
        //            @Override
        //            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //
        //            }
        //
        //            @Override
        //            public void afterTextChanged(Editable editable) {
        //                if (editable.toString().trim().length() >= 1) {
        //                    adapter.getFilter().item_filter(editable.toString().trim());
        //                } else {
        //                    adapter.getFilter().item_filter("");
        //                }
        //
        //            }
        //        });

        Button btnSpinnerOk = w.findViewById(R.id.btnPositive);
        //        final View llGame = (View) w.findViewById(R.id.llGame);
        //        final TextView tvGameName = (TextView) w.findViewById(R.id.tvGameName);
        btnSpinnerOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Spinner> selList = adapter.getSelectedAll();
                tv.setText(adapter.getSelectedTitle());
                tv.setTag(adapter.getSelectedIds());

                if (selList.size() > 0) {
                    tv.setTag(adapter.getSelectedIdArray());

                } else {
                    tv.setTag(null);
                }

                callback.onDone(selList);
                a.dismiss();
            }
        });

        Button btnSpinnerCancel = w.findViewById(R.id.btnNegative);
        btnSpinnerCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a.dismiss();
            }
        });
        a.show();
    }
*/

    public void setTypeFaceBold(TextView txt){
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/ubuntu_bold.ttf");
        txt.setTypeface(face);
    }

    public void setTypeFaceRegular(TextView txt){
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/ubuntu_regular.ttf");
        txt.setTypeface(face);
    }

    public void setTextViewDrawableColor(TextView textView, int color) {

        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                textView.setTextColor(color);
                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(textView.getContext(), color), PorterDuff.Mode.SRC_IN));
            }else{
                Log.e("drawable","drawable not found");
            }
        }
    }


    public void setTextViewDrawableColor1(TextView textView, int drawableRes, int color) {

        textView.setCompoundDrawablesWithIntrinsicBounds(
                null,  // Left
                tintDrawable(ContextCompat.getDrawable(getApplicationContext(), drawableRes),
                        ContextCompat.getColor(getApplicationContext(), color)), // Top
                null, // Right
                null); //Bottom
    }

    public static Drawable tintDrawable(Drawable drawable, int tint) {
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, tint);
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_ATOP);

        return drawable;
    }

}