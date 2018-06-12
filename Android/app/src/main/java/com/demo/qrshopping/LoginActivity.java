package com.demo.qrshopping;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class LoginActivity extends AppCompatActivity {

    private final String TAG = getClass().getName();
    EditText uidET,passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(R.string.loginTitle);



        uidET = findViewById(R.id.uidET);
        passwordET = findViewById(R.id.passwordET);


        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utility.validateTextFields(uidET,passwordET)){

                    String url = getString(R.string.baseURL);

                    JSONObject json = new JSONObject();
                    try {
                        json.put(Utility.customer_email,uidET.getText());
                        json.put(Utility.customer_pass,passwordET.getText());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(Utility.type == 0){
                        url = url + "customer.php?getCustomer="+json;
                    }else{
                        url = url + "shop.php?getShop="+json;
                    }

                    AsyncHttpClient client = new AsyncHttpClient();
                    client.get(url, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            try {
                                String s = new String(responseBody,"UTF-8");
                                Log.e(TAG, "onSuccess: "+s );
                                JSONObject response = new JSONObject(s);
                                if(response.getBoolean("status")){
                                        Intent i;
                                        if(Utility.type == 0) {
                                            i = new Intent(getBaseContext(), CustomerActivity.class);
                                        }else{
                                            i = new Intent(getBaseContext(), ShopDashboardActivity.class);
                                        }
                                        JSONArray array = new JSONArray(response.getString("result"));
                                        i.putExtra("data",array.get(0).toString());
                                        startActivity(i);

                                }
                                Utility.showToast(response.getString("message"),getBaseContext());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    });
                }

            }
        });

        findViewById(R.id.sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(getBaseContext(),RegistrationActivity.class);
                startActivity(registerIntent);
            }
        });

    }


}
