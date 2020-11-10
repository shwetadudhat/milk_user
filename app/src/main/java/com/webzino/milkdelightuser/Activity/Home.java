package com.webzino.milkdelightuser.Activity;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.webzino.milkdelightuser.R;
import com.webzino.milkdelightuser.Adapter.Adapter_menu1;
import com.webzino.milkdelightuser.Fragments.MainContainer_Fragment;
import com.webzino.milkdelightuser.Fragments.MainFragment.About_Fragment;
import com.webzino.milkdelightuser.Fragments.MainFragment.Contact_Fragment;
import com.webzino.milkdelightuser.Fragments.MainFragment.Faq_Fragment;
import com.webzino.milkdelightuser.Fragments.MainFragment.MyOrder_Fragment;
import com.webzino.milkdelightuser.Fragments.MainFragment.Notification_Fragment;
import com.webzino.milkdelightuser.Fragments.MainFragment.Offer_Fragment;
import com.webzino.milkdelightuser.Fragments.MainFragment.Profile_Fragment;
import com.webzino.milkdelightuser.Fragments.MainFragment.ReferNEarn_Fragment;
import com.webzino.milkdelightuser.Model.Menu_Model;
import com.webzino.milkdelightuser.utils.AppController;
import com.webzino.milkdelightuser.utils.BaseActivity;
import com.webzino.milkdelightuser.utils.BaseURL;
import com.webzino.milkdelightuser.utils.CustomVolleyJsonRequest;
import com.webzino.milkdelightuser.utils.Global;
import com.webzino.milkdelightuser.utils.Session_management;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.webzino.milkdelightuser.utils.AppController.MY_SOCKET_TIMEOUT_MS;
import static com.webzino.milkdelightuser.utils.Global.ProfileUrl;


public class Home extends BaseActivity implements  FragmentManager.OnBackStackChangedListener {

    Session_management session_management;
    String user_id,user_name,user_nmbr,user_email,user_image;
    public static NavigationView navigationView;
    public static ListView menuList;
    public static Adapter_menu1 adapterMenu1;

    String notification=null;
    String home=null;
    static int row_index=0;

    String token,wallet,subsription;

    TextView user_name1,user_nmbr1;
    CircleImageView user_dp;
    public static DrawerLayout drawer1;

    LinearLayout logout,header;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar_00);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawer1 = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer1, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer1.addDrawerListener(toggle);

        toggle.syncState();


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_noun_menu);
        getSupportActionBar().setTitle(getString(R.string.app_name));

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        getPreferenceData();

        user_name1=findViewById(R.id.user_name1);
        user_nmbr1=findViewById(R.id.user_nmbr1);
        user_dp=findViewById(R.id.user_dp);
        header=findViewById(R.id.header);

        menuList =  findViewById(R.id.list);
        //Data
        ArrayList<Menu_Model> nav_item = new ArrayList<>();
        nav_item.add(new Menu_Model(R.drawable.ic_home_1,getString(R.string.home)));
        nav_item.add(new Menu_Model(R.drawable.ic_account_circle_black_24dp,getString(R.string.myprofile)));
        nav_item.add(new Menu_Model(R.drawable.ic_noun_order,getString(R.string.myorder)));
        nav_item.add(new Menu_Model(R.drawable.ic_sale,getString(R.string.Offer)));
        nav_item.add(new Menu_Model(R.drawable.ic_noun_subscribe,getString(R.string.subscription)));
        nav_item.add(new Menu_Model(R.drawable.ic_account_balance_wallet_black_24dp,getString(R.string.wallet)));
        nav_item.add(new Menu_Model(R.drawable.ic_noun_menu,getString(R.string.shopByCat)));
        nav_item.add(new Menu_Model(R.drawable.ic_star_black_24dp,getString(R.string.rate)));
        nav_item.add(new Menu_Model(R.drawable.ic_noun_contact_us_1,getString(R.string.contact)));
        nav_item.add(new Menu_Model(R.drawable.ic_noun_share,getString(R.string.referFriend)));
        nav_item.add(new Menu_Model(R.drawable.ic_noun_help,getString(R.string.about)));
        nav_item.add(new Menu_Model(R.drawable.ic_question,getString(R.string.faq)));

        adapterMenu1 = new Adapter_menu1( Home.this,  android.R.layout.simple_list_item_activated_1,
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

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iVBackNavigation.callOnClick();
            }
        });

        iVBackNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index=1;
                drawer1.closeDrawer(GravityCompat.START);
                getSupportActionBar().setTitle(getString(R.string.myprofile));
                MyProfile(1);
                adapterMenu1.setSelectedItem(row_index);
                adapterMenu1.notifyDataSetChanged();
            }
        });

        logout=findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmLogout();

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
        wallet=getIntent().getStringExtra("wallet");
        subsription=getIntent().getStringExtra("subsription");
       //
        if( notification!= null)
        {
//            notificationCntent();
            OpenMainFragment(new Notification_Fragment());
        }else  if( wallet!= null) {
            Wallet(12);
        }else  if( subsription!= null) {
            Subscription(4);
        }else{
            selectedItem(0);
        }

    }

    private void notificationCntent() {
        Intent intent=new Intent(Home.this, NotificationActivity.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void selectedItem(int i) {
        if (i==0){
            getSupportActionBar().setTitle(getString(R.string.app_name));
            Home(0);
        }else if (i==1){
            getSupportActionBar().setTitle(getString(R.string.myprofile));
            MyProfile(1);
        }else if (i==2){
            getSupportActionBar().setTitle(getString(R.string.myorder));
            MyOrder(2);

        }else if (i==3){
            getSupportActionBar().setTitle(getString(R.string.Offer));
            offer(3);

        }else if (i==4){
            getSupportActionBar().setTitle(getString(R.string.subscription));
            Subscription(4);

        }else if (i==5){
            getSupportActionBar().setTitle(getString(R.string.wallet));
            /*ShopByCat(5);*/
            Wallet(5);

        }else if (i==6){
            getSupportActionBar().setTitle(getString(R.string.shopByCat));
            ShopByCat(6);

        }else if (i==7){
            rate();

        }else if (i==8){
            getSupportActionBar().setTitle(getString(R.string.contact));
            contact(8);

        }else if (i==9){
            getSupportActionBar().setTitle(getString(R.string.referFriend));
            referFriend(9);

        }else if (i==10){
            getSupportActionBar().setTitle(getString(R.string.about));
            about(10);

        }else if (i==11){
            getSupportActionBar().setTitle(getString(R.string.faq1));
            faq(11);

        }

        drawer1.closeDrawer(GravityCompat.START);
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

        getPreferenceData();

        if (!user_image.equals("null")){
            String profileurl=ProfileUrl(Home.this);
            Global.loadGlideImage(Home.this,user_image,profileurl/*BaseURL.profile_url*/+user_image,user_dp);
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
                getSupportActionBar().setTitle(getString(R.string.notification));
//                notificationCntent();
                OpenMainFragment(new Notification_Fragment(),11);
                break;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void Home(int i){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFrame, new MainContainer_Fragment(getString(R.string.home)));
        transaction.addToBackStack(String.valueOf(i));
        transaction.commit();
       // OpenMainFragment(new MainContainer_Fragment("Home"));
    }

    public void MyProfile(int i){
        OpenMainFragment(new Profile_Fragment(),i);
    }

    public void MyOrder(int i){

        OpenMainFragment(new MyOrder_Fragment(),i);
    }
    public void offer(int i){
        OpenMainFragment(new Offer_Fragment(),i);

    }

    public void Wallet(int i){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFrame, new MainContainer_Fragment(getString(R.string.wallet)));
        transaction.addToBackStack(String.valueOf(i));
        transaction.commit();
        //  OpenMainFragment(new MainContainer_Fragment("Subscription"));
    }
    public void Subscription(int i){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFrame, new MainContainer_Fragment(getString(R.string.subscription)));
        transaction.addToBackStack(String.valueOf(i));
        transaction.commit();
      //  OpenMainFragment(new MainContainer_Fragment("Subscription"));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void ShopByCat(int i){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFrame, new MainContainer_Fragment(getString(R.string.shopByCat)));
        transaction.addToBackStack(String.valueOf(i));
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

    public void contact(int i){
        OpenMainFragment(new Contact_Fragment(),i);
    }
    public void referFriend(int i){
        OpenMainFragment(new ReferNEarn_Fragment(),i);
    }
    public void about(int i){
        OpenMainFragment(new About_Fragment(),i);
    }
    public void faq(int i){
        OpenMainFragment(new Faq_Fragment(),i);
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
                            Intent intent=new Intent(Home.this, Login.class);
                            intent.putExtra("action","logout");
                            startActivity(intent);
                            finish();
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
                Toast.makeText(getApplicationContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);

    }

    private void OpenMainFragment(Fragment target_frag, int i) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        getSupportFragmentManager().addOnBackStackChangedListener(Home.this);
        transaction.replace(R.id.mainFrame, target_frag);
        Log.e("fragmentTagiiii",String.valueOf(i));
        transaction.addToBackStack(String.valueOf(i));
        transaction.commit();

    }

    private void OpenMainFragment(Fragment target_frag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        getSupportFragmentManager().addOnBackStackChangedListener(Home.this);
        transaction.replace(R.id.mainFrame, target_frag);
//        Log.e("fragmentTagiiii",String.valueOf(i));
//        transaction.addToBackStack(null);
        transaction.commit();

    }




    @Override
    protected void onResume() {
        super.onResume();
        getNavViewData();
    }

    @Override
    public void onBackStackChanged() {

        Log.e("getSupportFragmentMa",String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));

        String fragmentTag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        adapterMenu1.setSelectedItem(Integer.parseInt(fragmentTag));
        adapterMenu1.notifyDataSetChanged();
        Log.e("fragmentTag",fragmentTag);

        if (Integer.parseInt(fragmentTag)==0){
            getSupportActionBar().setTitle(getString(R.string.app_name));

        }else if (Integer.parseInt(fragmentTag)==1){
            getSupportActionBar().setTitle(getString(R.string.myprofile));

        }else if (Integer.parseInt(fragmentTag)==2){
            getSupportActionBar().setTitle(getString(R.string.myorder));

        }else if (Integer.parseInt(fragmentTag)==3){
            getSupportActionBar().setTitle(getString(R.string.Offer));

        }else if (Integer.parseInt(fragmentTag)==4){
            getSupportActionBar().setTitle(getString(R.string.subscription));

        }else if (Integer.parseInt(fragmentTag)==5){
            getSupportActionBar().setTitle(getString(R.string.wallet));

        }else if (Integer.parseInt(fragmentTag)==6){
            getSupportActionBar().setTitle(getString(R.string.shopByCat));

        }else if (Integer.parseInt(fragmentTag)==8){
            getSupportActionBar().setTitle(getString(R.string.contact));

        }else if (Integer.parseInt(fragmentTag)==9){
            getSupportActionBar().setTitle(getString(R.string.referFriend));

        }else if (Integer.parseInt(fragmentTag)==10){
            getSupportActionBar().setTitle(getString(R.string.about));

        }else if (Integer.parseInt(fragmentTag)==11){
            getSupportActionBar().setTitle(getString(R.string.faq1));

        }else if (Integer.parseInt(fragmentTag)==12){
            getSupportActionBar().setTitle(getString(R.string.notification));

        }else if (Integer.parseInt(fragmentTag)==13){
            getSupportActionBar().setTitle(getString(R.string.wallet));

        }
    }

    @Override
    public void onBackPressed() {
        Log.e("satckpoint",String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));
        if (drawer1.isDrawerOpen(GravityCompat.START)) {
                drawer1.closeDrawer(GravityCompat.START);
        }else{
            if (getSupportFragmentManager().getBackStackEntryCount() > 1){
                Log.e("iffff","iffff");
                getFragmentManager().popBackStack();
                super.onBackPressed();

            }
            else {
                Log.e("elseeee","elsee");
//                 finish();

                Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.mainFrame);
                if (f instanceof MainContainer_Fragment) {
                    Log.e("truuuurrr","truuuurr");
                    selectedItem(0);
                    finish();
                }else{
                    Log.e("truuuurrr11","truuuurr11");
                   super.onBackPressed();
                }
            }

        }

    }

    public void confirmLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.logout_title)
                .setMessage(R.string.logout_msg)
                .setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     //   showToast("Logged Out", Toast.LENGTH_SHORT);
                        Toast.makeText(Home.this, "Logged Out", Toast.LENGTH_SHORT).show();
                        if (session_management.isLoggedIn()){
                            session_management.logoutSession();
                            Intent intent=new Intent(Home.this, Login.class);
                            startActivity(intent);
                            finish();
                        }

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


}
