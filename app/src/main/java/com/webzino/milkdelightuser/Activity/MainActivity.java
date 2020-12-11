package com.webzino.milkdelightuser.Activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.webzino.milkdelightuser.R;
import com.webzino.milkdelightuser.Fragments.Home_Fragment1;
import com.webzino.milkdelightuser.Fragments.Subscription_Fragment;
import com.webzino.milkdelightuser.Fragments.Wallet_Fragment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    LinearLayout ll_home,ll_subscription,ll_wallet,ll_cart;
    TextView tvHome,tvSubscription,tvWallet,tvCart;
    Button btnHome,btnSubscription,btnWallet,btnCart;

    String value ;

    public  static String currency_sign="₹";


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
      /*  setContentView(R.layout.activity_main);*/
        value = getIntent().getStringExtra("value");

         if (value==null){
             value= "";
         }

        ll_home = findViewById(R.id.ll_home);
        ll_subscription = findViewById(R.id.ll_subscription);
        ll_wallet = findViewById(R.id.ll_wallet);
        ll_cart = findViewById(R.id.ll_cart);
        btnHome = findViewById(R.id.btnHome);
        btnSubscription = findViewById(R.id.btnSubscription);
        btnWallet = findViewById(R.id.btnWallet);
        btnCart = findViewById(R.id.btnCart);
        tvHome = findViewById(R.id.tvHome);
        tvSubscription = findViewById(R.id.tvSubscription);
        tvWallet = findViewById(R.id.tvWallet);
        tvCart = findViewById(R.id.tvCart);


        if (value.contains("home")){
            Log.d("dfas",value);

            btnSubscription.setEnabled(true);
            btnWallet.setEnabled(true);
            btnCart.setEnabled(true);
            btnHome.setEnabled(false);

            btnHome.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.main_clr)));
            btnSubscription.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
            btnWallet.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
            btnCart.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));


            tvCart.setTextColor(getResources().getColor(R.color.grey));
            tvSubscription.setTextColor(getResources().getColor(R.color.grey));
            tvWallet.setTextColor(getResources().getColor(R.color.grey));
            tvHome.setTextColor(getResources().getColor(R.color.main_clr));

            Home_Fragment1 home_fragment=new Home_Fragment1();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container_12, home_fragment);
            transaction.commit();
        }
        else if (value.contains("subscription")) {
            Log.d("dfas", value);

            btnSubscription.setEnabled(false);
            btnWallet.setEnabled(true);
            btnCart.setEnabled(true);
            btnHome.setEnabled(true);

            btnHome.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
            btnSubscription.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.main_clr)));
            btnWallet.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
            btnCart.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));


            tvCart.setTextColor(getResources().getColor(R.color.grey));
            tvSubscription.setTextColor(getResources().getColor(R.color.main_clr));
            tvWallet.setTextColor(getResources().getColor(R.color.grey));
            tvHome.setTextColor(getResources().getColor(R.color.grey));

            Subscription_Fragment subscription_fragment=new Subscription_Fragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container_12, subscription_fragment);
            transaction.commit();

        }
        else {
            Home_Fragment1 fragmenthome = new Home_Fragment1();
            // load fragment
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container_12, fragmenthome);
            transaction.commit();
            btnHome.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.main_clr)));

            tvHome.setTextColor(getResources().getColor(R.color.main_clr));
        }

        ll_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnHome.callOnClick();
            }
        });


        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSubscription.setEnabled(true);
                btnWallet.setEnabled(true);
                btnCart.setEnabled(true);
                btnHome.setEnabled(false);

                btnHome.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.main_clr)));
                btnSubscription.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
                btnWallet.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
                btnCart.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));


                tvCart.setTextColor(getResources().getColor(R.color.grey));
                tvSubscription.setTextColor(getResources().getColor(R.color.grey));
                tvWallet.setTextColor(getResources().getColor(R.color.grey));
                tvHome.setTextColor(getResources().getColor(R.color.main_clr));

                Home_Fragment1 home_fragment=new Home_Fragment1();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container_12, home_fragment);
                transaction.commit();

            }
        });

        ll_subscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSubscription.callOnClick();
            }
        });

        btnSubscription.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                btnSubscription.setEnabled(false);
                btnWallet.setEnabled(true);
                btnCart.setEnabled(true);
                btnHome.setEnabled(true);

                btnHome.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
                btnSubscription.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.main_clr)));
                btnWallet.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
                btnCart.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));


                tvCart.setTextColor(getResources().getColor(R.color.grey));
                tvSubscription.setTextColor(getResources().getColor(R.color.main_clr));
                tvWallet.setTextColor(getResources().getColor(R.color.grey));
                tvHome.setTextColor(getResources().getColor(R.color.grey));

                Subscription_Fragment subscription_fragment=new Subscription_Fragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container_12, subscription_fragment);
                transaction.commit();
            }
        });

        ll_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnWallet.callOnClick();
            }
        });


        btnWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSubscription.setEnabled(true);
                btnWallet.setEnabled(false);
                btnCart.setEnabled(true);
                btnHome.setEnabled(true);

                btnHome.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
                btnSubscription.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
                btnWallet.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.main_clr)));
                btnCart.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));


                tvCart.setTextColor(getResources().getColor(R.color.grey));
                tvSubscription.setTextColor(getResources().getColor(R.color.grey));
                tvWallet.setTextColor(getResources().getColor(R.color.main_clr));
                tvHome.setTextColor(getResources().getColor(R.color.grey));

                Wallet_Fragment wallet_fragment=new Wallet_Fragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container_12, wallet_fragment);
                transaction.commit();
            }
        });

        ll_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCart.callOnClick();
            }
        });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                btnSubscription.setEnabled(true);
                btnWallet.setEnabled(true);
                btnCart.setEnabled(false);
                btnHome.setEnabled(true);

                btnHome.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
                btnSubscription.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
                btnWallet.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
                btnCart.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.main_clr)));


                tvCart.setTextColor(getResources().getColor(R.color.main_clr));
                tvSubscription.setTextColor(getResources().getColor(R.color.grey));
                tvWallet.setTextColor(getResources().getColor(R.color.grey));
                tvHome.setTextColor(getResources().getColor(R.color.grey));

                /*Cart_Fragment cart_fragment=new Cart_Fragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container_12, cart_fragment);
                transaction.commit();*/
            }
        });

    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

        // set title

        alertDialogBuilder.setTitle(R.string.app_name);

        alertDialogBuilder.setIcon(R.drawable.logo);

        // set dialog message
        alertDialogBuilder
                .setMessage("Are you sure you want to exit")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();

    }
}
