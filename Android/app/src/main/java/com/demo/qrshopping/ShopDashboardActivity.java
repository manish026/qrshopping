package com.demo.qrshopping;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.journeyapps.barcodescanner.Util;

import org.json.JSONException;
import org.json.JSONObject;

public class ShopDashboardActivity extends AppCompatActivity {

    TextView textView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_dashboard);
        setTitle(R.string.shopdashboard);

        Intent intent = getIntent();
        textView = findViewById(R.id.shopTV);
        String data = intent.getStringExtra("data");
        try {
            JSONObject jsonObject = new JSONObject(data);
            if(jsonObject.has(Utility.customer_name)) {
                Utility.shopId = jsonObject.getString(Utility.customer_email);
                textView.setText("Welcome " + jsonObject.get(Utility.customer_name));
            }else{
                Utility.shopId = jsonObject.getString("shop_email");
                textView.setText("Welcome " + jsonObject.get("shop_name"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        findViewById(R.id.show_stock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(),StockListActivity.class);
                startActivity(i);
            }
        });

        findViewById(R.id.shopaddproduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(),MainActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Utility.showToast("You cannot go back from here",getBaseContext());
    }
}
