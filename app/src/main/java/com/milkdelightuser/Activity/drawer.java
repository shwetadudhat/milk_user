package com.milkdelightuser.Activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.milkdelightuser.Adapter.Adapter_menu1;
import com.milkdelightuser.Fragments.MainContainer_Fragment;
import com.milkdelightuser.Fragments.MainFragment.About_Fragment;
import com.milkdelightuser.Fragments.MainFragment.Contact_Fragment;
import com.milkdelightuser.Fragments.MainFragment.Faq_Fragment;
import com.milkdelightuser.Fragments.MainFragment.MyOrder_Fragment;
import com.milkdelightuser.Fragments.MainFragment.Notification_Fragment;
import com.milkdelightuser.Fragments.MainFragment.Offer_Fragment;
import com.milkdelightuser.Fragments.MainFragment.Profile_Fragment;
import com.milkdelightuser.Fragments.MainFragment.ReferNEarn_Fragment;
import com.milkdelightuser.Fragments.Wallet_Fragment;
import com.milkdelightuser.Model.Menu_Model;
import com.milkdelightuser.R;
import com.milkdelightuser.utils.AppController;
import com.milkdelightuser.utils.BaseActivity;
import com.milkdelightuser.utils.BaseURL;
import com.milkdelightuser.utils.CustomVolleyJsonRequest;
import com.milkdelightuser.utils.Session_management;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import de.hdodenhof.circleimageview.CircleImageView;


public class drawer extends BaseActivity implements PaymentResultListener {

    Session_management session_management;
    String user_id,user_name,user_nmbr,user_email,user_image;
   public static NavigationView navigationView;
   // private RecyclerView menuList;
   public static ListView menuList;
    public static Adapter_menu1 adapterMenu1;

    String notification=null;
    String home=null;
    static int row_index=0;

    String token;

    TextView user_name1,user_nmbr1;
    CircleImageView user_dp;
    public static DrawerLayout drawer1;

    LinearLayout logout;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar_00);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      //  getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_noun_menu);

        Checkout.preload(getActivity());

        drawer1 = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer1, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer1.addDrawerListener(toggle);

        toggle.syncState();


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_noun_menu);
        getSupportActionBar().setTitle("Milk Delight");

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        getPreferenceData();




        user_name1=findViewById(R.id.user_name1);
        user_nmbr1=findViewById(R.id.user_nmbr1);
        user_dp=findViewById(R.id.user_dp);


        menuList =  findViewById(R.id.list);
        //Data
        ArrayList<Menu_Model> nav_item = new ArrayList<>();
        nav_item.add(new Menu_Model(R.drawable.ic_home_1,"Home"));
        nav_item.add(new Menu_Model(R.drawable.ic_account_circle_black_24dp,"My Profile"));
        nav_item.add(new Menu_Model(R.drawable.ic_noun_order,"My Order"));
        nav_item.add(new Menu_Model(R.drawable.ic_sale,"Offer"));
        nav_item.add(new Menu_Model(R.drawable.ic_noun_subscribe,"Subscription"));
        nav_item.add(new Menu_Model(R.drawable.ic_noun_menu,"Shop By Category"));
        nav_item.add(new Menu_Model(R.drawable.ic_star_black_24dp,"Rate Our App"));
        nav_item.add(new Menu_Model(R.drawable.ic_noun_contact_us_1,"Contact us"));
        nav_item.add(new Menu_Model(R.drawable.ic_noun_share,"Refer a Friend"));
        nav_item.add(new Menu_Model(R.drawable.ic_noun_help,"About us"));
        nav_item.add(new Menu_Model(R.drawable.ic_question,"Faq"));

        adapterMenu1 = new Adapter_menu1( drawer.this,  android.R.layout.simple_list_item_activated_1,
                nav_item);
        menuList.setAdapter(adapterMenu1);




        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                selectedItem(i);

            }
        });



        getNavViewData();


        ImageView iVBackNavigation=findViewById(R.id.iVBackNavigation);

        iVBackNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index=1;
                drawer1.closeDrawer(GravityCompat.START);
                getSupportActionBar().setTitle("My Profile");
                MyProfile();
                adapterMenu1.setSelectedItem(row_index);
                adapterMenu1.notifyDataSetChanged();
            }
        });

        logout=findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("logouttttt","logouttttt");
                if (isInternetConnected()) {
                    showDialog("");
                    if (session_management.isLoggedIn()){
                        session_management.logoutSession();
                        Intent intent=new Intent(drawer.this, Login.class);
                        startActivity(intent);
                    }
                    //  LogOut(user_id,token);
                }
            }
        });




        FirebaseApp.initializeApp(this);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("TAG", "getInstanceId failed", task.getException());
                        return;
                    }
                    // Get new Instance ID token
                    token = task.getResult().getToken();
                    Log.e("token123",token);
//                    token = token;
//                    SharePreferenceManager.saveString(ActivityLoginType.this, SharePreferenceManager.DEVICE_TOKEN, token);
                });



        notification=getIntent().getStringExtra("notification");
       //
        if( notification!= null)
        {
            OpenMainFragment(new Notification_Fragment());
        }else{
            selectedItem(0);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.mainFrame, new MainContainer_Fragment("Home"));
            transaction.commit();

        }


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void selectedItem(int i) {
        if (i==0){
            drawer1.closeDrawer(GravityCompat.START);
            getSupportActionBar().setTitle("Milk Delight");
            Home();
        }else if (i==1){
            drawer1.closeDrawer(GravityCompat.START);
            getSupportActionBar().setTitle("My Profile");
            MyProfile();
        }else if (i==2){
            drawer1.closeDrawer(GravityCompat.START);
            getSupportActionBar().setTitle("My Order");
            MyOrder();

        }else if (i==3){
            drawer1.closeDrawer(GravityCompat.START);
            getSupportActionBar().setTitle("Offer");
            offer();

        }else if (i==4){
            drawer1.closeDrawer(GravityCompat.START);
            getSupportActionBar().setTitle("Subscription");
            Subscription();

        }else if (i==5){
            drawer1.closeDrawer(GravityCompat.START);
            getSupportActionBar().setTitle("Shop By Category");
            ShopByCat();

        }else if (i==6){
            drawer1.closeDrawer(GravityCompat.START);
            getSupportActionBar().setTitle("Rate Our App");
            rate();

        }else if (i==7){
            drawer1.closeDrawer(GravityCompat.START);
            getSupportActionBar().setTitle("Contact us");
            contact();

        }else if (i==8){
            drawer1.closeDrawer(GravityCompat.START);
            getSupportActionBar().setTitle("Refer a Friend");
            referFriend();

        }else if (i==9){
            drawer1.closeDrawer(GravityCompat.START);
            getSupportActionBar().setTitle("About us");
            about();

        }else if (i==10){
            drawer1.closeDrawer(GravityCompat.START);
            getSupportActionBar().setTitle("FAQ");
            faq();

        }

        adapterMenu1.setSelectedItem(i);
        adapterMenu1.notifyDataSetChanged();

    }

    private void getPreferenceData() {
        session_management=new Session_management(getApplicationContext());
        user_id = session_management.getUserDetails().get(BaseURL.KEY_ID);
        user_name = session_management.getUserDetails().get(BaseURL.KEY_NAME);
        user_nmbr = session_management.getUserDetails().get(BaseURL.KEY_MOBILE);
        user_email = session_management.getUserDetails().get(BaseURL.KEY_EMAIL);
        user_image = session_management.getUserDetails().get(BaseURL.KEY_IMAGE);
    }

    public void getNavViewData() {

        if (!user_image.equals("null")){
            Glide.with(drawer.this)
                    .load(BaseURL.profile_url+user_image)
                    .into(user_dp);
        }else{
            user_dp.setImageResource(R.mipmap.ic_launcher);
        }


        user_name1.setText(user_name);
        if (user_nmbr.equals("null")){
            user_nmbr1.setText(user_email);
        }else{
            user_nmbr1.setText(user_nmbr);
        }


    }


    @Override
    public void onBackPressed() {
         drawer1 = findViewById(R.id.drawer_layout);
        if (drawer1.isDrawerOpen(GravityCompat.START)) {
            drawer1.closeDrawer(GravityCompat.START);
        } else {
          //  rel_homefrag.setVisibility(View.VISIBLE);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.action_notification:
                getSupportActionBar().setTitle("Notification");
                OpenMainFragment(new Notification_Fragment());
                break;
        }
        return true;

    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void Home(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFrame, new MainContainer_Fragment("Home"));
       // transaction.addToBackStack(null);
        transaction.commit();
       // OpenMainFragment(new MainContainer_Fragment("Home"));
    }

    public void MyProfile(){
        OpenMainFragment(new Profile_Fragment());
    }

    public void MyOrder(){

        OpenMainFragment(new MyOrder_Fragment());
    }
    public void offer(){
        OpenMainFragment(new Offer_Fragment());

    }
    public void Subscription(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFrame, new MainContainer_Fragment("Subscription"));
      //  transaction.addToBackStack(null);
        transaction.commit();
      //  OpenMainFragment(new MainContainer_Fragment("Subscription"));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void ShopByCat(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFrame, new MainContainer_Fragment("ShopByCat"));
        //  transaction.addToBackStack(null);
        transaction.commit();
      //  OpenMainFragment(new MainContainer_Fragment("ShopByCat"));
    }

    public void rate() {
        Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
        }

    }

    public void contact(){
        OpenMainFragment(new Contact_Fragment());
    }
    public void referFriend(){
        OpenMainFragment(new ReferNEarn_Fragment());
    }
    public void about(){
        OpenMainFragment(new About_Fragment());
    }
    public void faq(){
        OpenMainFragment(new Faq_Fragment());
    }

    public void LogOut(String user_id, String token){
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("device_id", token);
        params.put("user_id", user_id);

        Log.e("params",params.toString());

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.logout, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.d("LoginTag", response.toString());

                try {

                    String status = response.getString("status");
                    String message = response.getString("message");

                    if (status.contains("1")) {
                        if (session_management.isLoggedIn()){
                            session_management.logoutSession();
                            Intent intent=new Intent(drawer.this, Login.class);
                            startActivity(intent);
                        }

                    } else {

                        Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissDialog();
                Log.e("error",error.toString());
              //  Toast.makeText(getApplicationContext(), ""+error, Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);

    }

    public Fragment getCurrentFragment() {
        return this.getSupportFragmentManager().findFragmentById(R.id.mainFrame);
    }

    private void OpenMainFragment(Fragment target_frag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFrame, target_frag);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    public void startpayment(String pay) {
        Checkout checkout = new Checkout();

//        checkout.setImage(R.mipmap.ic_launcher);
        // final Activity activity = this;
        final Activity activity = drawer.this;
        int price_rs = Integer.parseInt(pay);
        try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name",user_name);
            options.put("description","Add Amount in Wallet");
            //YOU CAN OMIT THE IMAGE OPTION TO FETCH THE IMAGE FROM DASHBOARD
            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency","INR");
            /**
             * Amount is always passed in currency subunits
             * Eg: "500" = INR 5.00
             */
            options.put("amount",price_rs*100);

            JSONObject prefill = new JSONObject();
            prefill.put("email", user_email);
            prefill.put("contact",user_nmbr);

            options.put("prefill",prefill);


            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e("cds", "Error in starting Razorpay Checkout", e);
        }


    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        Wallet_Fragment fragment = (Wallet_Fragment) getSupportFragmentManager().findFragmentById(R.id.container_12);
        fragment.successResponse(razorpayPaymentID);


    }



    @Override
    public void onPaymentError(int i, String s) {
        Log.e("payment_error",s);
        Wallet_Fragment fragment = (Wallet_Fragment) getSupportFragmentManager().findFragmentById(R.id.container_12);
        fragment.FailResponse(s);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getNavViewData();
        Log.e("navigationView","onResume");
    }

}
