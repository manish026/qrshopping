package com.demo.qrshopping;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class StockListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    List<Product> input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_list);

        recyclerView = findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        input = new ArrayList<>();
        mAdapter = new ProductAdapter(input,this,false);
        recyclerView.setAdapter(mAdapter);

        findViewById(R.id.imageView1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StockListActivity.this,MainActivity.class );
                startActivity(i);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getProducts();
    }

    void getProducts(){

        JSONObject json = new JSONObject();
        try {
            json.put("shopid",Utility.shopId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = getString(R.string.baseURL)+"product.php?AllProduct="+json;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {
                    String s = new String(responseBody,"UTF-8");
                    JSONObject json = new JSONObject(s);
                    JSONArray array = new JSONArray(json.getString("result"));
                    input.clear();
                    if(array.length() > 0) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject js = new JSONObject(String.valueOf(array.get(i)));
                            Product product = new Product(js.getString("product_name"), js.getString("product_price"), js.getString("product_quantity"));
                            input.add(product);
                        }
                        mAdapter.notifyDataSetChanged();
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

}
