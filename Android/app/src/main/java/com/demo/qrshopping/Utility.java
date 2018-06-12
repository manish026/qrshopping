package com.demo.qrshopping;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by manish on 25/04/18.
 */

public class Utility {

    static String shopId = "2";
    static float total = 0;
    static int type = 0;


    static String customer_name = "customer_name";
    static String customer_pass = "customer_pass";
    static String customer_email = "customer_email";
    static String customer_address = "customer_address";
    static String customer_dob = "customer_dob";
    static String customer_contact = "customer_contact";

    static void showToast(String message,Context context){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static Boolean validateEmail(EditText editText){
        if((!TextUtils.isEmpty(editText.getText().toString()) && Patterns.EMAIL_ADDRESS.matcher(editText.getText().toString()).matches())){
            return true;
        }else{
            editText.setError("Invalid email address");
            return false;
        }
    }

    public static Boolean validateNumber(EditText editText){

        if( editText.getText().toString().length() == 10 ){
            return true;
        }else{
            editText.setError("Invalid Phone Number");
            return false;
        }

    }

    static String getString(EditText editText){
        return editText.getText().toString();
    }

    // Use to validate text fields

    public static Boolean validateTextFields(EditText... editTexts){


        for (EditText et: editTexts) {

            if(et.getText().toString().isEmpty() && et.getVisibility() != View.GONE){
                et.setError(et.getHint() + " is Required");
                return false;
            }

        }

        return true;

    }

}
