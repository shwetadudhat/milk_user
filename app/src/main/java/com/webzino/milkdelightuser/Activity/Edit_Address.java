package com.webzino.milkdelightuser.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.webzino.milkdelightuser.R;
import com.webzino.milkdelightuser.Model.Address_Model;
import com.webzino.milkdelightuser.Model.State_Model;
import com.webzino.milkdelightuser.utils.AppController;
import com.webzino.milkdelightuser.utils.BaseActivity;
import com.webzino.milkdelightuser.utils.BaseURL;
import com.webzino.milkdelightuser.utils.CustomVolleyJsonRequest;
import com.webzino.milkdelightuser.utils.Global;
import com.webzino.milkdelightuser.utils.Session_management;
import com.webzino.milkdelightuser.utils.Spinner1;
import com.webzino.milkdelightuser.utils.SpinnerCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Edit_Address extends BaseActivity implements View.OnClickListener {

    Button btnAdrsSave;
    ImageView ivBack;
    Toolbar toolbar;


    EditText edName,edHouse,edArea,edLandmark,edNumber,edPincode;
    String txtName,txtHouse,txtArea,txtLandmark,txtNumber,txtCity,txtState,txtPincode;
    TextView SpiCity,SpiState;

    String position_name, postionid;
    TextView toolTitle;
    String action;
    List<Address_Model> addressModelList;

    ArrayList<State_Model> StateName;
    String selectedStateId = "-1";
    ArrayList<Spinner1> stateItems =new ArrayList<>();
    String selectedCityId = "-1";
    ArrayList<Spinner1> cityItems= new ArrayList<>();

    Session_management session_management;
    String user_id,user_name,user_phn;

    String city_id,state_id;
    int id;



    @SuppressLint("SetTextI18n")
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_address_addedit);

        toolTitle=findViewById(R.id.title);
        action=getIntent().getStringExtra("action");
        if (action.equals("edit")){
            id= Integer.parseInt(getIntent().getStringExtra("id"));
            toolTitle.setText(R.string.edit_address);

        }


        ivBack=findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnAdrsSave = findViewById(R.id.btnAdrsSave);
        edName = findViewById(R.id.edName);
        edHouse = findViewById(R.id.edHouse);
        edArea = findViewById(R.id.edArea);
        edLandmark = findViewById(R.id.edLandmark);
        edNumber = findViewById(R.id.edNumber);
        SpiState = findViewById(R.id.SpiState);
        SpiCity = findViewById(R.id.SpiCity);
        edPincode = findViewById(R.id.edPincode);
        SpiState.setOnClickListener(this);
        SpiCity.setOnClickListener(this);

        session_management=new Session_management(getApplicationContext());
        user_id = session_management.getUserDetails().get(BaseURL.KEY_ID);
        user_name = session_management.getUserDetails().get(BaseURL.KEY_NAME);
        user_phn = session_management.getUserDetails().get(BaseURL.KEY_MOBILE);

        //  StateName=new ArrayList<>();

        edName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                edName.setBackground(getResources().getDrawable(R.drawable.bg_edit));
            }
        });

        edHouse.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                edHouse.setBackground(getResources().getDrawable(R.drawable.bg_edit));
            }
        });


        edArea.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                edArea.setBackground(getResources().getDrawable(R.drawable.bg_edit));
            }
        });


        edLandmark.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                edLandmark.setBackground(getResources().getDrawable(R.drawable.bg_edit));
            }
        });

        edNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                edNumber.setBackground(getResources().getDrawable(R.drawable.bg_edit));
            }
        });

        edPincode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                edPincode.setBackground(getResources().getDrawable(R.drawable.bg_edit));
            }
        });

       /* SpiState.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                SpiState.setBackground(getResources().getDrawable(R.drawable.bg_edit));
            }
        });

        SpiCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                SpiCity.setBackground(getResources().getDrawable(R.drawable.bg_edit));
            }
        });*/




        if (isInternetConnected()) {
            showDialog("");
            editAddressData(String.valueOf(id));

        }

        btnAdrsSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtName=edName.getText().toString();
                txtHouse=edHouse.getText().toString();
                txtArea=edArea.getText().toString();
                txtLandmark=edLandmark.getText().toString();
                txtNumber=edNumber.getText().toString();
                txtCity=SpiCity.getText().toString();
                txtState=SpiState.getText().toString();
                txtPincode=edPincode.getText().toString();

                Log.e("txtCity123",txtCity);
                Log.e("txtState123",txtState);


                if(ValidateDetail(txtName,txtHouse,txtArea,txtLandmark,txtNumber,txtCity,txtState,txtPincode)){
                    if (isInternetConnected()) {
                        if (action.equals("edit")){
                            Edit_address(user_id,txtName,txtHouse,txtArea,selectedStateId,selectedCityId,txtLandmark,txtNumber,txtPincode);
                        }
                    }

                }

            }

        });
    }

    private void editAddressData(String id) {

        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("address_id",id);
        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.address_details, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("editaddress", response.toString());
                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    Toast.makeText(Edit_Address.this, message, Toast.LENGTH_SHORT).show();

                    if (status.equals("1")){
                        JSONArray jsonArray = response.getJSONArray("data");
                        if (jsonArray.length()==0){
                            Toast.makeText(Edit_Address.this, "0 data found", Toast.LENGTH_SHORT).show();
                        }else{
                            for (int i=0;i<jsonArray.length();i++){

                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                String id=jsonObject.getString("id");
                                String user_id=jsonObject.getString("user_id");
                                String user_name=jsonObject.getString("user_name");
                                String user_number=jsonObject.getString("user_number");
                                city_id=jsonObject.getString("city_id");
                                state_id=jsonObject.getString("state_id");
                                String house_no=jsonObject.getString("house_no");
                                String landmark=jsonObject.getString("landmark");
                                String address=jsonObject.getString("address");
                                String full_address=jsonObject.getString("full_address");
                                String pincode=jsonObject.getString("pincode");

                                //  String address=jsonObject.getString("user_number");

                                loadSpiState1();
                                selectedStateId=state_id;
                                loadSpiCity1(state_id);
                                selectedCityId=city_id;
                                Log.e("statelist", String.valueOf(stateItems));

                                edName.setText(user_name);
                                edNumber.setText(user_number);


                                edHouse.setText(house_no);
                                edLandmark.setText(landmark);
                                edArea.setText(address);

                                edPincode.setText(pincode);

                            }

                            Log.e("cityItems",cityItems.toString());
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
              //  Toast.makeText(Edit_Address.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest,tag_json_obj);


    }

    private void loadSpiState1() {
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest( BaseURL.state, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("state", response.toString());
                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    //  Toast.makeText(Edit_Address.this, message, Toast.LENGTH_SHORT).show();

                    if (status.equals("1")){
                        JSONArray jsonArray = response.getJSONArray("data");
                        if (jsonArray.length()==0){
                            Toast.makeText(Edit_Address.this, "0 data found", Toast.LENGTH_SHORT).show();

                        }else{
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                String id=jsonObject1.getString("id");
                                String state_name=jsonObject1.getString("state_name");

                                Log.e("state_name",state_name);
                                // StateName.add(new State_Model(id,state_name));

                                Spinner1 spinner1=new Spinner1(id,state_name);
                                stateItems.add(spinner1);

                            }
                            Log.e("stateItems123444",stateItems.toString());
                            Log.e("state_id12",state_id);
                            if (stateItems.size() > 0) {
                                for (int j=0;j<stateItems.size();j++){
                                    if (stateItems.get(j).ID.equals(state_id)){
                                        SpiState.setText(stateItems.get(j).title);
                                        SpiState.setBackground(getResources().getDrawable(R.drawable.bg_edit));

                                    }
                                }

                            }

                            // SpiState.setAdapter(new ArrayAdapter<String>(Edit_Address.this, android.R.layout.simple_spinner_dropdown_item, stateItems);

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
             //   Toast.makeText(Edit_Address.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private void loadSpiCity1(String selectedStateId){
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("state_id",selectedStateId);
        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.city, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("city1233", response.toString());
                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    Toast.makeText(Edit_Address.this, message, Toast.LENGTH_SHORT).show();

                    if (status.equals("1")){
                        JSONArray jsonArray = response.getJSONArray("data");
                        if (jsonArray.length()==0){
                            Toast.makeText(Edit_Address.this, "0 data found", Toast.LENGTH_SHORT).show();
                        }else{
                            for (int i=0;i<jsonArray.length();i++){

                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                String city_id=jsonObject.getString("id");
                                String state_id=jsonObject.getString("state_id");
                                String city_name=jsonObject.getString("city_name");

                                Spinner1 spinner1=new Spinner1(city_id,city_name);
                                cityItems.add(spinner1);

                             //   Log.e("cityItems",cityItems.toString());

                            }

                            Log.e("cityItems12344",cityItems.toString());

                            if (cityItems.size() > 0) {
                                for (int k=0;k<cityItems.size();k++){
                                    if (cityItems.get(k).ID.equals(city_id)){
                                        SpiCity.setText(cityItems.get(k).title);

                                        SpiCity.setBackground(getResources().getDrawable(R.drawable.bg_edit));
                                    }
                                }

                            }
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
              //  Toast.makeText(Edit_Address.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest,tag_json_obj);

    }

    private boolean ValidateDetail(String txtName, String txtHouse, String txtArea, String txtLandmark, String txtNumber, String txtCity, String txtState, String txtPincode) {
        /*if (postionid.contains("false")) {
            Toast.makeText(Edit_Address.this, "Select Locality", Toast.LENGTH_SHORT).show();
        } else*/ if (txtName.length() == 0) {
            edName.setError("Enter Name");
            Global.showKeyBoard(Edit_Address.this,edName);
            //    Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (txtHouse.length() == 0) {
            edHouse.setError("Enter House No, Building name");
            Global.showKeyBoard(Edit_Address.this,edHouse);
            //   Toast.makeText(this, "Enter House No, Building name", Toast.LENGTH_SHORT).show();
            return false;
        }else if (txtArea.length()==0){
            edArea.setError("Enter Road Name,Area ,Colony");
            Global.showKeyBoard(Edit_Address.this,edArea);
            //  Toast.makeText(this, "Enter Road Name,Area ,Colony", Toast.LENGTH_SHORT).show();
            return false;
        }else if (selectedStateId .equals("-1")) {
            SpiState.setError("Please select the state");
            Global.showKeyBoard(Edit_Address.this,SpiState);
            return false;
        }
        else   if (selectedCityId .equals( "-1")) {
            SpiCity.setError("Please select the city");
            Global.showKeyBoard(Edit_Address.this,SpiCity);
            return false;
        }else if (txtLandmark.length()==0){
            edLandmark.setError("Enter Landmark");
            Global.showKeyBoard(Edit_Address.this,edLandmark);
            //Toast.makeText(this, "Enter Landmark", Toast.LENGTH_SHORT).show();
            return false;
        }else if (txtNumber.length()==0){
            edNumber.setError("Enter Phone Number");
            Global.showKeyBoard(Edit_Address.this,edNumber);
            //  Toast.makeText(this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
            return false;
        }else if (txtNumber.length()!=10){
            edNumber.setError("Please Enter valid Phone Number");
            Global.showKeyBoard(Edit_Address.this,edNumber);
            //  Toast.makeText(this, "Please Enter valid Phone Number", Toast.LENGTH_SHORT).show();
            return false;
        }else if (txtPincode.length()==0){
            edPincode.setError("Please Enter Pincode");
            Global.showKeyBoard(Edit_Address.this,edPincode);
            //  Toast.makeText(this, "Please Enter Pincode", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!Global.isValidPinCode(txtPincode)){
            edPincode.setError("Please Enter valid Pincode");
            Global.showKeyBoard(Edit_Address.this,edPincode);
            //  Toast.makeText(this, "Please Enter valid Pincode", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }

    }


    private void Edit_address(String user_id, String txtName, String txtHouse, String txtArea, String txtState, String txtCity, String txtLandmark, String txtNumber, String txtPincode) {
        /*api call on success finish*/
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id",user_id);
        params.put("user_name",txtName);
        params.put("house_no",txtHouse);
        params.put("address",txtArea);
        params.put("city",txtCity);
        params.put("state",txtState);
        params.put("landmark",txtLandmark);
        params.put("user_number",txtNumber);
        params.put("address_id", String.valueOf(id));
        params.put("pincode", txtPincode);

        Log.e("paramAddress",params.toString());

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.address_edit, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("address_edit", response.toString());
                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    if (status.equals("1")){
                        Toast.makeText(Edit_Address.this, message, Toast.LENGTH_SHORT).show();

                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("user_id",user_id);
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();

                    }else{
                        Toast.makeText(Edit_Address.this, ""+message, Toast.LENGTH_SHORT).show();
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
               // Toast.makeText(Edit_Address.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest,tag_json_obj);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.SpiState:
                if (stateItems.size() > 0) {
                    /* showSpinner("Select Your State", SpiState, stateItems, true,*/

                    showSpinnerSel("Select Your State",SpiState, stateItems, true, new SpinnerCallback() {
                        @Override
                        public void onDone(ArrayList<Spinner1> list) {
                            selectedStateId = list.get(0).ID;
                            SpiState.setText(list.get(0).title);

                            SpiState.setBackground(getResources().getDrawable(R.drawable.bg_edit));
                            cityItems =new ArrayList();
                            selectedCityId = "-1";
                            SpiCity.setText(" ");

                            loadSpiCity1(selectedStateId);
                        }
                    });

                } else {
                    //  showToast("State Not Found!")
                    Toast.makeText(this, "State not found!", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.SpiCity:

                if (cityItems.size() > 0) {

                    showSpinnerSel("Select Your City",SpiCity, cityItems, true, new SpinnerCallback() {
                        @Override
                        public void onDone(ArrayList<Spinner1> list) {
                            selectedCityId = list.get(0).ID;
                            SpiCity.setText(list.get(0).title);

                            SpiCity.setBackground(getResources().getDrawable(R.drawable.bg_edit));

                        }
                    });

                } else {
                    //  showToast("State Not Found!")
                    Toast.makeText(this, "City not found!", Toast.LENGTH_SHORT).show();
                }
                break;

            // Do something
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(Edit_Address.this, Addresslist.class);
        intent.putExtra("user_id",user_id);
        startActivity(intent);
        finish();
    }
}
