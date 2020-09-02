package com.milkdelightuser.Fragments.MainFragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.milkdelightuser.Activity.MainActivity;
import com.milkdelightuser.R;
import com.milkdelightuser.utils.AppController;
import com.milkdelightuser.utils.BaseFragment;
import com.milkdelightuser.utils.BaseURL;
import com.milkdelightuser.utils.ConnectivityReceiver;
import com.milkdelightuser.utils.CustomVolleyJsonRequest;
import com.milkdelightuser.utils.Global;
import com.milkdelightuser.utils.Session_management;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReferNEarn_Fragment extends BaseFragment {

    TextView edCode;
    TextView tvCopy,tv_refer;
   // FloatingActionButton btnShare;
    ImageView imgShare;

    Session_management session_management;
    String u_id;
    String invitationId;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor myEdit;


    public ReferNEarn_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_refernearn, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edCode=view.findViewById(R.id.edCode);
        tvCopy=view.findViewById(R.id.tvCopy);
        imgShare=view.findViewById(R.id.imgShare);
       // btnShare=view.findViewById(R.id.btnShare);
        tv_refer=view.findViewById(R.id.tv_refer);

        session_management=new Session_management(getContext());
        u_id = session_management.getUserDetails().get(BaseURL.KEY_ID);

        if (ConnectivityReceiver.isConnected()) {
            try {
                showDialog("");
                RefereEarn(u_id);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Global.isInternetConnected(getContext());
        }


        tvCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager= (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData= ClipData.newPlainText("Earn Code",edCode.getText().toString());
                clipboardManager.setPrimaryClip(clipData);
            }
        });

        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //sendReferral(getContext());
                String shareUrl = String.format("https://play.google.com/store/apps/details?id=%s&referrer=%s", getContext().getPackageName(), edCode.getText().toString());
                String shareData = String.format("Hi,  please try  Milk Delight App %s", shareUrl);
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareData);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
    }

    private void sendReferral(Context context) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);


       // sendIntent.putExtra(Intent.EXTRA_TEXT, getInvitationMessage()), PreferencesManager.getInstance().getKeyReferrerUrl()));
        sendIntent.putExtra(Intent.EXTRA_TEXT, getInvitationMessage());

        sendIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.invitation_subject));
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, context.getResources().getText(R.string.invitation_extended_title)));
    }

    private String getInvitationMessage(){
        String playStoreLink = "https://play.google.com/store/apps/details?id=app.package.com&referrer=utm_source=";
        invitationId = playStoreLink + edCode.getText().toString()/*getReferralId()*/;
        Log.e("invitationId",invitationId);
        myEdit.putString("referLink",invitationId);
        return invitationId ;
    }


    private void RefereEarn(String u_id) {
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", u_id);

        Log.e("params",params.toString());

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.refer_earn, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("referNearn", response.toString());

                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    if (status.equals("1")){
                      //  Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        JSONObject jsonObject=response.getJSONObject("data");
                        String amount=jsonObject.getString("amount");
                        String refer_code=jsonObject.getString("refer_code");

                        String str = "<b>"+ MainActivity.currency_sign+amount+"</b>";
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                            tv_refer.setText(Html.fromHtml(getString(R.string.invite_txtt)+ str+getString(R.string.invite_wallet_txt), Html.FROM_HTML_MODE_LEGACY));
                          //  tv_refer.setText(getString(R.string.invite_txtt)+ str +getString(R.string.invite_wallet_txt));
                        } else {
                            tv_refer.setText(Html.fromHtml(getString(R.string.invite_txtt)+ str+getString(R.string.invite_wallet_txt)));

                        }

                        edCode.setText(refer_code);


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
              //  Toast.makeText(getContext(), "" + error, Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }


    /* public void sendReferral(Context context) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, getInvitationMessage()), PreferencesManager.getInstance().getKeyReferrerUrl()));
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.invitation_subject));
            sendIntent.setType("text/plain");
            context.startActivity(Intent.createChooser(sendIntent, context.getResources().getText(R.string.invitation_extended_title)));
        }

private String getInvitationMessage(){
  String playStoreLink = "https://play.google.com/store/apps/details?id=app.package.com&referrer=utm_source=";
  return invitationId = playStoreLink + getReferralId();
}*/


}
