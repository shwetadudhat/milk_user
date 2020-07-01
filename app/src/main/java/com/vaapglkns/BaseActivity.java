package com.vaapglkns;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
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

import com.vaapglkns.adapter.SpinnerAdapter;
import com.vaapglkns.adapter.SpinnerSelAdapter;
import com.vaapglkns.objects.Spinner;
import com.vaapglkns.utils.AsyncProgressDialog;
import com.vaapglkns.utils.Constant;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.vaapglkns.utils.SpinnerCallback;
import com.vaapglkns.utils.Utils;

import java.util.ArrayList;


public class BaseActivity extends AppCompatActivity {
    AsyncProgressDialog ad;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        toast = Toast.makeText(getActivity(), "", Toast.LENGTH_LONG);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.FINISH_ACTIVITY);

        commonReciever = new MyEventServiceReciever();
        LocalBroadcastManager.getInstance(this).registerReceiver(commonReciever, intentFilter);
    }

    Drawer result;

    public void initDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//create the drawer and remember the `Drawer` result object
        result = new DrawerBuilder()
                .withActivity(this).withCloseOnClick(true).withSelectedItemByPosition(-1)
                .withDrawerGravity(Gravity.LEFT)
                .withHeader(R.layout.nav_header_main)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Home").withSelectable(false).withIdentifier(0L),
                        new PrimaryDrawerItem().withName("Dummy Activity").withSelectable(false).withIdentifier(1L),
                        new PrimaryDrawerItem().withName("Logout").withSelectable(false).withIdentifier(2L)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.getIdentifier() == 0L) {
                            if (getActivity() instanceof HomeActivity) {
                                hideMenu(true);
                            } else {
                                Intent intent = new Intent(getActivity(),
                                        HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startNewActivity(intent);
                                hideMenu(false);
                                finishActivity();
                            }
                        } else if (drawerItem.getIdentifier() == 1L) {
                            if (getActivity() instanceof DummyActivity) {
                                hideMenu(true);
                            } else {
                                Intent intent = new Intent(getActivity(),
                                        DummyActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startNewActivity(intent);
                                hideMenu(false);
                                finishActivity();
                            }
                        }
//                        } else if (position == 3) {
//                            if (getActivity() instanceof YourWalletActivity) {
//                                hideMenu(true);
//                            } else {
//                                Intent intent = new Intent(getActivity(),
//                                        YourWalletActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                startNewActivity(intent);
//                                hideMenu(false);
//                                finishActivity();
//                            }
//                        } else if (position == 4) {
//                            if (getActivity() instanceof TransferAmountActivity) {
//                                hideMenu(true);
//                            } else {
//                                Intent intent = new Intent(getActivity(),
//                                        TransferAmountActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                startNewActivity(intent);
//                                hideMenu(false);
//                                finishActivity();
//                            }
//                        } else if (position == 5) {
//                            if (getActivity() instanceof MyBussinessActivity) {
//                                hideMenu(true);
//                            } else {
//                                Intent intent = new Intent(getActivity(),
//                                        MyBussinessActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                startNewActivity(intent);
//                                hideMenu(false);
//                                finishActivity();
//                            }
//                        } else if (position == 6) {
//                            if (getActivity() instanceof OurShopActivity) {
//                                hideMenu(true);
//                            } else {
//                                Intent intent = new Intent(getActivity(),
//                                        OurShopActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                startNewActivity(intent);
//                                hideMenu(false);
//                                finishActivity();
//                            }
//                        } else if (position == 7) {
//                            if (getActivity() instanceof TransactionActivity) {
//                                hideMenu(true);
//                            } else {
//                                Intent intent = new Intent(getActivity(),
//                                        TransactionActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                startNewActivity(intent);
//                                hideMenu(false);
//                                finishActivity();
//                            }
//                        } else if (position == 8) {
//                            confirmLogout();
//                        }
                        return true;
                    }
                })
                .build();

        ImageView ivMenu = (ImageView) findViewById(R.id.ivMenu);
        ivMenu.setVisibility(View.VISIBLE);
        if (ivMenu != null) {
            ivMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (result.isDrawerOpen()) {
                        result.closeDrawer();
                    } else {
                        result.openDrawer();
                    }
                }
            });
        }

//        initMenuItems();
//        fillProfileData();
    }

    //region FOR START NEW ACTIVITY
    public void startNewActivity(Intent i) {
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    //endregion

    //region FOR START NEW ACTIVITY
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void startNewActivity(Intent i, Bundle bundle) {
        i.putExtras(bundle);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    //endregion

    //region START NEW ACTIVITY FOR RESULT
    public void startNewActivityForResult(Intent i, int requestCode) {
        startActivityForResult(i, requestCode);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    //endregion

    //region FOR START NEW ACTIVITY
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void startNewActivity(Intent i, Bundle bundle, int requestCode) {
        i.putExtras(bundle);
        startActivityForResult(i, requestCode);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    //endregion

    public void confirmLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.logout_title)
                .setMessage(R.string.logout_msg)
                .setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showToast("Logged Out", Toast.LENGTH_SHORT);
                    }
                })
                .setNegativeButton(R.string.btn_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void hideMenu(boolean b) {
        try {
//            if (b)
            result.closeDrawer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void finishActivity() {
        if ((getActivity() instanceof HomeActivity)) {

        } else {
            getActivity().finish();
        }
    }

    MyEventServiceReciever commonReciever;

    class MyEventServiceReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent.getAction().equalsIgnoreCase(Constant.FINISH_ACTIVITY)) {
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public BaseActivity getActivity() {
        return this;
    }

    private TextView tvTitleText;
    private ImageView ivBack;

    public void setTitleText(String text) {
        try {
            if (tvTitleText == null)
                tvTitleText = (TextView) findViewById(R.id.tvTitleText);
            tvTitleText.setText(text);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //region FOR INIT BACK PRESS
    public void initBackPress(Boolean b) {
        if (ivBack == null)
            ivBack = findViewById(R.id.ivBack);
        if (b) {
            ivBack.setVisibility(View.VISIBLE);
            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            ivBack.setVisibility(View.GONE);
        }
    }
    //endregion

    //region FOR HIDE DRAWER MENU
    public void hideDrawerMenu(Boolean b) {
        try {
            if (b)
                result.closeDrawer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion

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

    @Override
    protected void onDestroy() {
        try {
            LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(commonReciever);
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        try {
            if (result.isDrawerOpen()) {
                result.closeDrawer();
            } else {
                super.onBackPressed();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        } catch (Exception e) {
            super.onBackPressed();
        }
    }

    BaseCallback baseCallback;

    public void setBaseCallback(BaseCallback baseCallback) {
        this.baseCallback = baseCallback;
    }

    interface BaseCallback {
        void onMasterDataLoad();
    }

    //region FOR SHOW SPINNER SELECTION
//    ArrayList<Spinner> array = new ArrayList<>();
//                array.add(new Spinner("0", "FITS IN HAND LUGGAGE"));
//                array.add(new Spinner("1", "FITS IN CARRY ON"));
//                array.add(new Spinner("2", "SMALL"));
//                array.add(new Spinner("3", "MEDIUM"));
//                array.add(new Spinner("4", "LARGE"));
//    showSpinnerSel("Choose Size", tvDeliveryTitle, array, true, new SpinnerCallback() {
//        @Override
//        public void onDone(ArrayList<Spinner> list) {
//
//        }
//    });

    public void showSpinnerSel(String title, TextView tv, ArrayList<Spinner> data, boolean isFilterable, SpinnerCallback callback) {
        Dialog a = new Dialog(getActivity());
        Window w = a.getWindow();
        a.requestWindowFeature(Window.FEATURE_NO_TITLE);
        a.setContentView(R.layout.spinner_sel);
        w.setBackgroundDrawableResource(android.R.color.transparent);

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
            ArrayList<String> selected = Utils.asList(selectedStr.replace("'", ""));

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
    //endregion

    //region FOR SHOW SPINNER
//    ArrayList<Spinner> array = new ArrayList<>();
//                array.add(new Spinner("1", "FITS IN HAND LUGGAGE"));
//                array.add(new Spinner("1", "FITS IN CARRY ON"));
//                array.add(new Spinner("1", "SMALL"));
//                array.add(new Spinner("1", "MEDIUM"));
//                array.add(new Spinner("1", "LARGE"));
//    showSpinner("Choose Size", array, new SpinnerCallback() {
//        @Override
//        public void onDone(ArrayList<Spinner> list) {
//
//        }
//    });
    public void showSpinner(final String title, final ArrayList<Spinner> data, final SpinnerCallback callback) {
        final Dialog a = new Dialog(getActivity());
        Window w = a.getWindow();
        a.requestWindowFeature(Window.FEATURE_NO_TITLE);
        a.setContentView(R.layout.spinner);
        w.setBackgroundDrawableResource(android.R.color.transparent);

        TextView labelSelect = w.findViewById(R.id.dialogtitle);
        labelSelect.setText(title.replace("*", "").trim());

//        TextView dialogClear = (TextView) w.findViewById(R.id.dialogClear);
//        dialogClear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                tv.setText("");
//                tv.setTag(null);
//                a.dismiss();
//            }
//        });

        final SpinnerAdapter adapter = new SpinnerAdapter(getActivity());
        ListView lv = w.findViewById(R.id.lvSpinner);
        lv.setAdapter(adapter);
        adapter.addAll(data);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ArrayList<Spinner> selList = new ArrayList<Spinner>();
                selList.add(adapter.getItem(position));
//                tv.setText(adapter.getItem(position).title);
//                tv.setTag(adapter.getItem(position).ID);
                if (callback != null) {
                    callback.onDone(selList);
                }
                a.dismiss();
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
//                    adapter.getFilter().filter(editable.toString().trim());
//                } else {
//                    adapter.getFilter().filter("");
//                }
//            }
//        });
        a.show();
    }
    //endregion
}