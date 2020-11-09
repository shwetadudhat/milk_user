package com.webzino.milkdelightuser.Fragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.webzino.milkdelightuser.R;
import com.webzino.milkdelightuser.Activity.Cart_activity;
import com.webzino.milkdelightuser.Activity.Home;
import com.webzino.milkdelightuser.utils.BaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainContainer_Fragment extends BaseFragment {

    LinearLayout ll_home,ll_subscription,ll_wallet,ll_cart;
    TextView tvHome,tvSubscription,tvWallet,tvCart;
    Button btnHome,btnSubscription,btnWallet,btnCart;

    RelativeLayout rel_homefrag;
    String targetLayout;

    public MainContainer_Fragment(){

    }

    public MainContainer_Fragment(String targetLayout) {
        this.targetLayout=targetLayout;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ll_home = view.findViewById(R.id.ll_home);
        ll_subscription = view.findViewById(R.id.ll_subscription);
        ll_wallet = view.findViewById(R.id.ll_wallet);
        ll_cart = view.findViewById(R.id.ll_cart);
        btnHome = view.findViewById(R.id.btnHome);
        btnSubscription = view.findViewById(R.id.btnSubscription);
        btnWallet = view.findViewById(R.id.btnWallet);
        btnCart = view.findViewById(R.id.btnCart);
        tvHome = view.findViewById(R.id.tvHome);
        tvSubscription = view.findViewById(R.id.tvSubscription);
        tvWallet = view.findViewById(R.id.tvWallet);
        tvCart = view.findViewById(R.id.tvCart);



        if (targetLayout!=null  && targetLayout.equals(getString(R.string.home))){
            ((Home) getActivity()).getSupportActionBar().setTitle(getString(R.string.app_name));
            openFragment(new Home_Fragment1(),btnHome,tvHome);
        }else if (targetLayout!=null && targetLayout.equals(getString(R.string.subscription))){
            ((Home) getActivity()).getSupportActionBar().setTitle(getString(R.string.mysubscription));
            openFragment(new Subscription_Fragment(),btnSubscription,tvSubscription);
        }else if (targetLayout!=null && targetLayout.equals(getString(R.string.shopByCat))){
            ((Home) getActivity()).getSupportActionBar().setTitle(getString(R.string.shopByCat));
            openFragment(new Cat_Fragment(),btnHome,tvHome);
        }else if (targetLayout!=null && targetLayout.equals(getString(R.string.wallet))){
            ((Home) getActivity()).getSupportActionBar().setTitle(getString(R.string.wallet));
            openFragment(new Wallet_Fragment(),btnWallet,tvWallet);
        }else{
            ((Home) getActivity()).getSupportActionBar().setTitle(getString(R.string.app_name));
            openFragment(new Home_Fragment1(),btnHome,tvHome);
        }

        ll_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnHome.callOnClick();
            }
        });


        btnHome.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                btnSubscription.setEnabled(true);
                btnWallet.setEnabled(true);
                btnCart.setEnabled(true);
                btnHome.setEnabled(false);

                btnHome.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.main_clr)));
                btnSubscription.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.small_title_clr)));
                btnWallet.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.small_title_clr)));
                btnCart.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.small_title_clr)));


                tvCart.setTextColor(getResources().getColor(R.color.small_title_clr));
                tvSubscription.setTextColor(getResources().getColor(R.color.small_title_clr));
                tvWallet.setTextColor(getResources().getColor(R.color.small_title_clr));
                tvHome.setTextColor(getResources().getColor(R.color.main_clr));

                setTypeFaceRegular(tvCart);
                setTypeFaceRegular(tvSubscription);
                setTypeFaceRegular(tvWallet);

                openFragment(new Home_Fragment1(),btnHome,tvHome);
                ((Home) getActivity()).getSupportActionBar().setTitle(getString(R.string.app_name));
                ((Home) getActivity()).selectedItem(0);


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

                btnHome.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.small_title_clr)));
                btnSubscription.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.main_clr)));
                btnWallet.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.small_title_clr)));
                btnCart.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.small_title_clr)));


                tvCart.setTextColor(getResources().getColor(R.color.small_title_clr));
                tvSubscription.setTextColor(getResources().getColor(R.color.main_clr));
                tvWallet.setTextColor(getResources().getColor(R.color.small_title_clr));
                tvHome.setTextColor(getResources().getColor(R.color.small_title_clr));

            /*    setTypeFace(tvCart,tvSubscription,tvWallet,tvHome);*/

                setTypeFaceRegular(tvCart);
                setTypeFaceRegular(tvHome);
                setTypeFaceRegular(tvWallet);


                ((Home) getActivity()).getSupportActionBar().setTitle(getString(R.string.mysubscription));
                openFragment(new Subscription_Fragment(),btnSubscription,tvSubscription);
                  ((Home) getActivity()).selectedItem(4);

            }
        });

        ll_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnWallet.callOnClick();
            }
        });


        btnWallet.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                btnSubscription.setEnabled(true);
                btnWallet.setEnabled(false);
                btnCart.setEnabled(true);
                btnHome.setEnabled(true);

                btnHome.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.small_title_clr)));
                btnSubscription.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.small_title_clr)));
                btnWallet.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.main_clr)));
                btnCart.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.small_title_clr)));


                tvCart.setTextColor(getResources().getColor(R.color.small_title_clr));
                tvSubscription.setTextColor(getResources().getColor(R.color.small_title_clr));
                tvWallet.setTextColor(getResources().getColor(R.color.main_clr));
                tvHome.setTextColor(getResources().getColor(R.color.small_title_clr));

                setTypeFaceRegular(tvCart);
                setTypeFaceRegular(tvSubscription);
                setTypeFaceRegular(tvHome);


                ((Home) getActivity()).getSupportActionBar().setTitle(getString(R.string.mywallet));
                openFragment(new Wallet_Fragment(),btnWallet,tvWallet);
                ((Home) getActivity()).selectedItem(5);

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

                btnHome.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.small_title_clr)));
                btnSubscription.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.small_title_clr)));
                btnWallet.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.small_title_clr)));
                btnCart.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.main_clr)));

                tvCart.setTextColor(getResources().getColor(R.color.main_clr));
                tvSubscription.setTextColor(getResources().getColor(R.color.small_title_clr));
                tvWallet.setTextColor(getResources().getColor(R.color.small_title_clr));
                tvHome.setTextColor(getResources().getColor(R.color.small_title_clr));

                setTypeFaceRegular(tvHome);
                setTypeFaceRegular(tvSubscription);
                setTypeFaceRegular(tvWallet);


                Intent intent=new Intent(getContext(), Cart_activity.class);
                startActivity(intent);
                getActivity().finish();
                ((Home) getActivity()).selectedItem(-1);


            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void openFragment(Fragment targetFarg, Button targetbtn, TextView targettv) {

        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_12, targetFarg);
        transaction.commit();
        targetbtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.main_clr)));
        targettv.setTextColor(getResources().getColor(R.color.main_clr));
        setTypeFaceBold(targettv);
    }

}
