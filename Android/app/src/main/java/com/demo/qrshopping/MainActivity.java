package com.demo.qrshopping;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    String productId = "";
    String TAG = getClass().getName();
    Button submit;
    EditText name,price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.addproduct);

        submit = findViewById(R.id.submit);
        submit.setVisibility(View.GONE);
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);

        findViewById(R.id.scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IntentIntegrator(MainActivity.this).initiateScan();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!price.getText().toString().isEmpty() && !name.getText().toString().isEmpty()){

                    JSONObject json = new JSONObject();
                    try {
                        json.put("id",productId);
                        json.put("price",price.getText().toString());
                        json.put("name",name.getText().toString());
                        json.put("shopid",Utility.shopId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String url = getString(R.string.baseURL)+"product.php?AddProduct="+json;
                    Log.e(TAG, "onClick: "+url );
                    AsyncHttpClient client = new AsyncHttpClient();
                    client.get(url, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            Utility.showToast("Product added",MainActivity.this);
                            try {
                                String s = new String(responseBody,"UTF-8");
                                name.setText("");
                                price.setText("");
                                Log.e(TAG, "onSuccess: "+s );
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Utility.showToast("Please try again",MainActivity.this);
                        }
                    });

                }else{
                    Utility.showToast("Please enter product name & price",MainActivity.this);
                }

            }
        });

    }

    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                productId = result.getContents();
                submit.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
