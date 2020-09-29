package com.milkdelightuser.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;


import com.google.android.youtube.player.YouTubePlayerView;
import com.milkdelightuser.Activity.ProductListing;
import com.milkdelightuser.Adapter.Adapter_BestProduct;
import com.milkdelightuser.Adapter.Adapter_HomeCat;
import com.milkdelightuser.Adapter.Adapter_SellingProduct;
import com.milkdelightuser.Adapter.Cat_banner_Adapter;
import com.milkdelightuser.Model.AppCategory_Model;
import com.milkdelightuser.Model.App_Product_Model;
import com.milkdelightuser.Model.Cat_Banner_Model;
import com.milkdelightuser.R;
import com.milkdelightuser.utils.AppController;
import com.milkdelightuser.utils.BaseFragment;
import com.milkdelightuser.utils.BaseURL;
import com.milkdelightuser.utils.CirclePageIndicator;
import com.milkdelightuser.utils.ConnectivityReceiver;
import com.milkdelightuser.utils.CustomVolleyJsonRequest;
import com.milkdelightuser.utils.DatabaseHandler;
import com.milkdelightuser.utils.Global;
import com.milkdelightuser.utils.MemberItemDecoration;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.milkdelightuser.utils.AppController.MY_SOCKET_TIMEOUT_MS;
import static com.milkdelightuser.utils.Global.GST_DATA;
import static com.milkdelightuser.utils.Global.MY_GST_PREFS_NAME;



public class Home_Fragment1 extends BaseFragment  {

    private DatabaseHandler db;

    LinearLayout llSearch;
    EditText editSearch;
    SearchView search;
    TextView /*search,*/ seeAllSelling, seeAllProduct;
    RecyclerView recycler_SellingProduct, recycler_cat, recycler_BestProduct;

    VideoView videoview;
    WebView webView;
    ImageView ivYouTube;
    public boolean isPlaying1=false;

    ViewPager view_pager_banner;
    CirclePageIndicator indicator_banner;

    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    public static String category_url;

    Adapter_BestProduct adapterBestProduct;
    Adapter_SellingProduct adapterSellingProduct;
    Adapter_HomeCat adapterHomeCat;

    List<App_Product_Model> productModelList;
    List<AppCategory_Model> appCategoryModelList;

    RelativeLayout RlbestSelling,rlCatttt,Rlbestproduct,RlbannerView,RlVideo;

    RelativeLayout rv_container,rv_null;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor myEdit;


    YouTubePlayerView youTubePlayerView;

    public Home_Fragment1() {
        // Required empty public -constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        llSearch=view.findViewById(R.id.llSearch);
        search=view.findViewById(R.id.search);
        editSearch=view.findViewById(R.id.editSearch);

        RlbannerView=view.findViewById(R.id.RlbannerView);
        RlVideo=view.findViewById(R.id.RlVideo);
        RlbestSelling=view.findViewById(R.id.RlbestSelling);
        rlCatttt=view.findViewById(R.id.rlCatttt);
        Rlbestproduct=view.findViewById(R.id.Rlbestproduct);

        videoview = (VideoView)view.findViewById(R.id.videoview);

//        youTubePlayerView = view.findViewById(R.id.youtube_player_view);




       /* youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = "S0Q4gqBUs7c";
                youTubePlayer.loadVideo(videoId, 0);
            }
        });*/

        ivYouTube = view.findViewById(R.id.ivYouTube);
        rv_container = view.findViewById(R.id.rv_container);
        rv_null = view.findViewById(R.id.rv_null);



        recycler_SellingProduct=view.findViewById(R.id.recycler_SellingProduct);
        recycler_cat=view.findViewById(R.id.recycler_cat);
        recycler_BestProduct=view.findViewById(R.id.recycler_BestProduct);
        seeAllSelling=view.findViewById(R.id.seeAllSelling);
        seeAllProduct=view.findViewById(R.id.seeAllProduct);
        view_pager_banner=view.findViewById(R.id.view_pager_banner);
        indicator_banner=view.findViewById(R.id.indicator_banner);

        sharedPreferences = getApplicationContext().getSharedPreferences(MY_GST_PREFS_NAME, Context.MODE_PRIVATE);
        myEdit = sharedPreferences.edit();

        db = new DatabaseHandler(getActivity());

        if (ConnectivityReceiver.isConnected()) {
            showDialog("");
            LoadHomeData();
            getGSTCharge();
        } else {
            Global.showInternetConnectionDialog(getContext());

        }


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
                    adapterBestProduct.filter(editable.toString().trim());
                    adapterHomeCat.filter(editable.toString().trim());
                    adapterSellingProduct.filter(editable.toString().trim());
                } else {

                    adapterBestProduct.filter("");
                    adapterHomeCat.filter("");
                    adapterSellingProduct.filter("");
                }
            }
        });

    }

    private void getGSTCharge() {
        Map<String, String> params = new HashMap<String, String>();

        Log.e("params",params.toString());

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest( BaseURL.gst_charge, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.e("gstTag", response.toString());
                try {
                    String status=response.getString("status");

                    if(status.equals("1")){
                        JSONObject jsonObject=response.getJSONObject("data");
                        myEdit.putString(GST_DATA, jsonObject.toString());
                        myEdit.commit();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error1234",error.toString());
                dismissDialog();
                showToast("" + error);

            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    private void LoadHomeData() {

        Map<String, String> params = new HashMap<String, String>();

        Log.e("params",params.toString());

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest( BaseURL.home, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.e("homeTag", response.toString());
                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    if (status.equals("1")){
                        JSONObject jsonObject=response.getJSONObject("data");
                        JSONArray home_banner=jsonObject.getJSONArray("home_banner");
                        JSONArray category=jsonObject.getJSONArray("category");
                        JSONArray selling_product=jsonObject.getJSONArray("selling_product");
                        JSONArray base_product=jsonObject.getJSONArray("base_product");
                        JSONObject video=jsonObject.getJSONObject("video_link");
                        String video_link=video.getString("video_link");
                        JSONObject url=jsonObject.getJSONObject("url");
                        String banner_url=url.getString("banner_url");
                        String product_url=url.getString("product_url");
                        category_url=url.getString("category_url");

                        dismissDialog();

                        if (home_banner.length()>0){
                            setUpBannerSlider1(view_pager_banner,indicator_banner,home_banner,banner_url);
                        }else{
                            RlbannerView.setVisibility(View.GONE);
                        }


                        if (selling_product.length()>0){
                            setUpSellingProduct(selling_product,product_url);
                        }else{
                            RlbestSelling.setVisibility(View.GONE);
                        }

                        if (category.length()>0){
                            setUpCategoryData(category,category_url);
                        }else{
                            rlCatttt.setVisibility(View.GONE);
                        }

                        if (base_product.length()>0){
                            setUpBestProduct(base_product,product_url);
                        }else{
                            Rlbestproduct.setVisibility(View.GONE);
                        }

                        if (video_link.equals("") || video_link.equals("null") ){
                            RlVideo.setVisibility(View.GONE);
                        }else{
                            VideoViewFun(video_link);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error1234",error.toString());
                dismissDialog();
                showToast("" + error);
              //  rv_null.setVisibility(View.VISIBLE);
              //  rv_container.setVisibility(View.GONE);

            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private void setUpCategoryData(JSONArray category, String category_url) {

        appCategoryModelList=new ArrayList<>();
        AppCategory_Model appCategoryModel;

        for (int i=0;i<category.length();i++){

            JSONObject jsonObject = null;
            try {
                jsonObject = category.getJSONObject(i);
                String category_id = jsonObject.getString("id");
                String category_name = jsonObject.getString("category_name");
                String category_image = jsonObject.getString("category_image");

                appCategoryModel=new AppCategory_Model();
                appCategoryModel.setCategory_id(category_id);
                appCategoryModel.setCategory_name(category_name);
                appCategoryModel.setCategory_image(category_url+category_image);


                appCategoryModelList.add(appCategoryModel);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        adapterHomeCat=new Adapter_HomeCat(getContext(),appCategoryModelList);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_cat.setLayoutManager(gridLayoutManager);
      //  recycler_cat.addItemDecoration(new MemberItemDecoration());
        recycler_cat.setAdapter(adapterHomeCat);

    }

    private void setUpBestProduct(JSONArray base_product, String product_url) {
        productModelList=new ArrayList<>();

        Log.e("base_productres",base_product.toString());

        App_Product_Model appProductModel;

        for (int i=0;i<base_product.length();i++){

            JSONObject jsonObject = null;
            try {
                jsonObject = base_product.getJSONObject(i);
                String product_id = jsonObject.getString("product_id");
                String category_id = jsonObject.getString("category_id");
                String product_name = jsonObject.getString("product_name");
                String price = jsonObject.getString("price");
                String subscription_price = jsonObject.getString("subscription_price");
                String qty = jsonObject.getString("qty");
                String product_image = jsonObject.getString("product_image");
                String description = jsonObject.getString("description");
                String stock = jsonObject.getString("stock");
                String unit = jsonObject.getString("unit");
                String total = jsonObject.getString("total");
                String mrp = jsonObject.getString("mrp");
                String gst = jsonObject.getString("gst");

                appProductModel=new App_Product_Model();
                appProductModel.setGst(gst);
                appProductModel.setProduct_id(product_id);
                appProductModel.setCategory_id(category_id);
                appProductModel.setProduct_name(product_name);
                appProductModel.setPrice(price);
                appProductModel.setSubscription_price(subscription_price);
                appProductModel.setQty(qty);
                appProductModel.setProduct_image(product_url+product_image);
                appProductModel.setDescription(description);
                appProductModel.setStock(stock);
                appProductModel.setUnit(unit);
                appProductModel.setTotal(total);
                appProductModel.setMrp(mrp);

                productModelList.add(appProductModel);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        seeAllProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), ProductListing.class);
                intent.putExtra("seeAll","bestProduct");
                getContext().startActivity(intent);
            }
        });
        recycler_BestProduct.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recycler_BestProduct.addItemDecoration(new MemberItemDecoration());
        adapterBestProduct=new Adapter_BestProduct(getContext(),productModelList);
        recycler_BestProduct.setAdapter(adapterBestProduct);
     //   adapterBestProduct.setFilterable(isFilterable);
      //  adapterBestProduct.addAll(productModelList);

    }

    private void setUpSellingProduct(JSONArray selling_product, String product_url) {

        productModelList=new ArrayList<>();

        App_Product_Model appProductModel;

        for (int i=0;i<selling_product.length();i++){

            JSONObject jsonObject = null;
            try {
                jsonObject = selling_product.getJSONObject(i);
                String product_id = jsonObject.getString("product_id");
                String category_id = jsonObject.getString("category_id");
                String product_name = jsonObject.getString("product_name");
                String price = jsonObject.getString("price");
                String subscription_price = jsonObject.getString("subscription_price");
                String qty = jsonObject.getString("qty");
                String product_image = jsonObject.getString("product_image");
                String description = jsonObject.getString("description");
                String stock = jsonObject.getString("stock");
                String unit = jsonObject.getString("unit");
                String total = jsonObject.getString("total");
                String mrp = jsonObject.getString("mrp");
                String gst = jsonObject.getString("gst");

                appProductModel=new App_Product_Model();
                appProductModel.setGst(gst);
                appProductModel.setProduct_id(product_id);
                appProductModel.setCategory_id(category_id);
                appProductModel.setProduct_name(product_name);
                appProductModel.setPrice(price);
                appProductModel.setSubscription_price(subscription_price);
                appProductModel.setQty(qty);
                appProductModel.setProduct_image(product_url+product_image);
                appProductModel.setDescription(description);
                appProductModel.setStock(stock);
                appProductModel.setUnit(unit);
                appProductModel.setTotal(total);
                appProductModel.setMrp(mrp);

                productModelList.add(appProductModel);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        seeAllSelling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), ProductListing.class);
                intent.putExtra("seeAll","bestSellingProduct");
                getContext().startActivity(intent);
            }
        });
        recycler_SellingProduct.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapterSellingProduct=new Adapter_SellingProduct(getContext(),productModelList);
        recycler_SellingProduct.setAdapter(adapterSellingProduct);

    }

    private void setUpBannerSlider1(ViewPager pager, CirclePageIndicator indicator, JSONArray home_banner, String banner_url) {
        List<Cat_Banner_Model> catBannerModelList =new ArrayList<>();
        Cat_Banner_Model catBannerModel;

        for (int i=0;i<home_banner.length();i++){

            JSONObject jsonObject = null;
            try {
                jsonObject = home_banner.getJSONObject(i);
                String Banner_image = jsonObject.getString("banner_image");
                String banner_id = jsonObject.getString("id");

                catBannerModel=new Cat_Banner_Model();
                catBannerModel.setBanner_id(banner_id);
                catBannerModel.setBanner_image(banner_url+Banner_image);

                catBannerModelList.add(catBannerModel);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        NUM_PAGES = catBannerModelList.size();

        Cat_banner_Adapter adapter_banner = new Cat_banner_Adapter( getApplicationContext(), catBannerModelList);
        pager.setAdapter(adapter_banner);

        if (getContext()!=null){
            final float density = getContext().getResources().getDisplayMetrics().density;

            indicator.setRadius(3 * density);
            indicator.setSpacing(12);

            indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {
                    currentPage = position;

                }

                @Override
                public void onPageScrolled(int pos, float arg1, int arg2) {

                }

                @Override
                public void onPageScrollStateChanged(int pos) {

                }
            });


            // Auto start of viewpager
            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (currentPage == NUM_PAGES) {
                        currentPage = 0;
                    }
                    pager.setCurrentItem(currentPage++, true);
                }
            };
            Timer swipeTimer = new Timer();
            swipeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, 3000, 3000);


            Cat_banner_Adapter adapter_banner1 = new Cat_banner_Adapter(getContext(),catBannerModelList);

            pager.setAdapter(adapter_banner1);

            indicator.setViewPager(pager);
            indicator.setSnap(true);
            indicator_banner.setVisibility(View.VISIBLE);
        }
    }


    private void VideoViewFun(String link) {
     //   getLifecycle().addObserver(youTubePlayerView);


       /* youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = "zxQXEyMMC0E";
                youTubePlayer.loadVideo(videoId, 0);
            }
        });*/


        /*Uri uri = Uri.parse(link);
        videoview.setMediaController(new MediaController(getContext()));
        videoview.setVideoURI(uri);
        videoview.requestFocus();
        videoview.start();*/


        videoview.setVideoPath("http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4");

        Uri video = Uri.parse(link);
    //    videoview.setVideoURI(video);
        Uri uri = Uri.parse("https://www.youtube.com/watch?v=h1Xp7p1taW0&ab_channel=AmulTheTasteofIndia");
        /*https://www.youtube.com/watch?v=OfscxuLO_GI&t=4s*/


        Log.e("linkkkkkk",link);
        ivYouTube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlaying1){
                    videoview.pause();
                    isPlaying1=false;
                }else{
                    videoview.start();
                    ivYouTube.setVisibility(View.GONE);
                    isPlaying1=true;
                }


            }
        });

        videoview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivYouTube.setVisibility(View.VISIBLE);
            }
        });

        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoview.stopPlayback();
                ivYouTube.setVisibility(View.VISIBLE);
                isPlaying1=false;
                Uri video = Uri.parse(link);
               // videoview.setVideoURI(video);
              //  videoview.setVideoPath(link);
                videoview.setVideoPath("http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4");
            }
        });


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        youTubePlayerView.release();
    }



}










