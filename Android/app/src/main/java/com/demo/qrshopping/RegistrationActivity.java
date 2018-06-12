package com.demo.qrshopping;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class RegistrationActivity extends AppCompatActivity {

    EditText fnameET,uidET,passwordET,cpasswordET,emailET,dobET,addressET;
    Calendar myCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initViews();
        setViews();
        myCalendar = Calendar.getInstance();
        setTitle(R.string.registration);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dobET.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(RegistrationActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        findViewById(R.id.submitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateET() && Utility.validateEmail(emailET) && Utility.validateNumber(uidET) && validatePassword()) {

                    final JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put(Utility.customer_name, fnameET.getText());
                        jsonObject.put(Utility.customer_pass, passwordET.getText());
                        jsonObject.put(Utility.customer_email, emailET.getText());
                        jsonObject.put(Utility.customer_dob, dobET.getText());
                        jsonObject.put(Utility.customer_address, addressET.getText());
                        jsonObject.put(Utility.customer_contact, uidET.getText());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String url = getString(R.string.baseURL);
                    if(Utility.type == 0){
                        url += "Customer.php?addcustomer="+jsonObject;
                    }else{
                        url += "shop.php?addshop="+jsonObject;
                    }


                    AsyncHttpClient client = new AsyncHttpClient();
                    client.get(url, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            try {
                                String s = new String(responseBody,"UTF-8");
                                JSONObject response = new JSONObject(s);
                                if(response.getBoolean("status")){

                                    Intent i;
                                    if(Utility.type == 0) {
                                        i = new Intent(getBaseContext(), CustomerActivity.class);
                                    }else{
                                        i = new Intent(getBaseContext(), ShopDashboardActivity.class);
                                    }
//                                    JSONArray array = new JSONArray(response.getString("result"));
                                    i.putExtra("data",jsonObject.toString());
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


    }

    void initViews(){

        fnameET = findViewById(R.id.fnameET);
        uidET = findViewById(R.id.uidET);
        passwordET = findViewById(R.id.passwordET);
        cpasswordET = findViewById(R.id.cpasswordET);
        emailET = findViewById(R.id.emailET);
        dobET = findViewById(R.id.dobET);
        addressET = findViewById(R.id.addressET);

    }

    void setViews(){

        switch (Utility.type){
            case 0 : setShopView();    break;
            case 1 : setCustomerView();  break;
        }
    }

    void setShopView(){

    }

    void setCustomerView(){



    }

    Boolean validateET(){

       return Utility.validateTextFields(fnameET,uidET,passwordET,cpasswordET,emailET,dobET,addressET);

    }

    Boolean validatePassword(){
      if( !Utility.getString(passwordET).equals(Utility.getString(cpasswordET))){
          cpasswordET.setError("Password not matched");
          return false;
      }
      return true;
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dobET.setText(sdf.format(myCalendar.getTime()));
    }
}
