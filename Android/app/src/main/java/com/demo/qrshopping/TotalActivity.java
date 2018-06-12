package com.demo.qrshopping;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TotalActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    List<Product> input;
    private final int Loading_SECONDS = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total);


        setTitle(R.string.payment);
        recyclerView = findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        input = new ArrayList<>();
        input = CustomerActivity.input;
        final TextView textView = findViewById(R.id.finalTextView);
        mAdapter = new ProductAdapter(input, this, false, new DataChanged() {
            @Override
            public void totalChanged() {
                textView.setText("You will be charged ₹ "+Utility.total);
                if(Utility.total == 0){
                    finish();
                }
            }
        });
        recyclerView.setAdapter(mAdapter);


        textView.setText("You will be charged ₹ "+Utility.total);

        findViewById(R.id.buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog pd = new ProgressDialog(TotalActivity.this);
                pd.show(TotalActivity.this, "Payment in progress", "Processing payment please be patient.");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        pd.hide();
                        Intent i = new Intent(TotalActivity.this,PaymentActivity.class);
                        startActivity(i);
                    }
                }, Loading_SECONDS);
            }
        });
    }
}
