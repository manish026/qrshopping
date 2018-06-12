package com.demo.qrshopping;

import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

interface DataChanged{
    void totalChanged();
}

public class CustomerActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    static  List<Product> input;
    static List<JSONObject> shops;
    List<String> names;
    String productId = "";
    String TAG = getClass().getName();
    Button submit;
    TextView textView;
    ArrayAdapter arrayAdapter;
    Spinner spinner;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
//        getSupportActionBar().setElevation(0);
        setContentView(R.layout.activity_customer);

        setTitle(R.string.customerdashboard);
        Intent intent = getIntent();
        textView = findViewById(R.id.textView);///₹
        spinner = findViewById(R.id.spinner);
        names = new ArrayList<>();
        shops = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item,names);
        spinner.setAdapter(arrayAdapter);
        String data = intent.getStringExtra("data");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                JSONObject jsonObject = shops.get(i);
                try {
                    Utility.shopId = jsonObject.getString("shop_email");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        getShops();
        try {
            JSONObject jsonObject = new JSONObject(data);
            textView.setText("Welcome " + jsonObject.get(Utility.customer_name));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        setupRecyclerView();

        findViewById(R.id.addproduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IntentIntegrator(CustomerActivity.this).initiateScan();
            }
        });

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(input.size() > 0) {
                    Intent i = new Intent(getBaseContext(), TotalActivity.class);
                    i.putExtra("products", (Serializable) input);
                    startActivity(i);
                }else{
                    Utility.showToast("Please add some product first",getBaseContext());
                }
            }
        });
        setUpView();
    }

    void setUpView(){

    }

    void setupRecyclerView(){
        recyclerView = findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        input = new ArrayList<>();

        mAdapter = new ProductAdapter(input, this, true, new DataChanged() {
            @Override
            public void totalChanged() {
//                textView.setText("Total: ₹"+Utility.total);
                if(input.isEmpty()){
                    spinner.setEnabled(true);
                }
            }
        });
        recyclerView.setAdapter(mAdapter);
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
                getProduct(productId);

//                submit.setVisibility(View.VISIBLE);
//                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    void getProduct(String id){

        JSONObject json = new JSONObject();
        try {
            json.put("shopid",Utility.shopId);
            Log.e(TAG, "getProduct: "+Utility.shopId );
            json.put("id",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = getString(R.string.baseURL)+"product.php?GetProduct="+json;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {
                    String s = new String(responseBody,"UTF-8");
                    JSONObject json = new JSONObject(s);
                    JSONArray array = new JSONArray(json.getString("result"));
                    if(array.length() > 0) {
                        spinner.setEnabled(false);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject js = new JSONObject(String.valueOf(array.get(i)));
                            Product product = new Product(js.getString("product_name"), js.getString("product_price"), js.getString("product_quantity"));
                            product.id = js.getInt("product_id");
                            Utility.total += Float.parseFloat(product.price);
//                            textView.setText("Total: ₹"+Utility.total);
                            input.add(product);
                        }
                        mAdapter.notifyDataSetChanged();
                    }else{
                        Utility.showToast("Product not available in shop",getBaseContext());
                    }
                    Log.e("TAG", "onSuccess: "+json );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });



    }

    void getShops(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(getString(R.string.baseURL)+"shop.php?getshops", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {
                    String s = new String(responseBody,"UTF-8");

                    JSONArray array = new JSONArray(s);
                    if(array.length() > 0) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject js = new JSONObject(String.valueOf(array.get(i)));
                            shops.add(js);
                            names.add(js.getString("shop_name"));
                            arrayAdapter.notifyDataSetChanged();
                        }
                        mAdapter.notifyDataSetChanged();
                    }else{
                        Utility.showToast("No shops found",getBaseContext());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Utility.showToast("You cannot go back from here",getBaseContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utility.total = 0;
    }
}
