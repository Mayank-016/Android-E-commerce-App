package com.app.shopping;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.app.shopping.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity implements PaymentResultListener {
    private EditText nameEditText,phoneEditText,addressEditText,cityEditText;
    private Button confirmOrderBtn;
    private String totalAmount = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Checkout.preload(getApplicationContext());
        setContentView(R.layout.activity_confirm_final_order);

        totalAmount = getIntent().getStringExtra("Total Price");
        Toast.makeText(this, "Total Price = Rs. "+totalAmount,Toast.LENGTH_SHORT).show();
        confirmOrderBtn = (Button) findViewById(R.id.confirm_final_order_btn);
        nameEditText =(EditText) findViewById(R.id.shippment_name);
        phoneEditText =(EditText) findViewById(R.id.shippment_phone_number);
        addressEditText =(EditText) findViewById(R.id.shippment_address);
        cityEditText =(EditText) findViewById(R.id.shippment_city);
        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Float amount = Float.parseFloat(String.valueOf(totalAmount))*100;
//                Checkout checkout = new Checkout();
//                checkout.setKeyID("rzp_test_JBpeloxQIIo8Ba");
//
//                JSONObject options = new JSONObject();
//                try {
//
//                    options.put("name", "Merchant Name");
//                    options.put("description", "Reference No. #123456");
//                    options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//                    options.put("theme.color", "#3399cc");
//                    options.put("currency", "INR");
//                    options.put("amount",amount);//pass amount in currency subunits
//                    options.put("prefill.email", "gaurav.kumar@example.com");
//                    options.put("prefill.contact","9988776655");
//                    checkout.open(ConfirmFinalOrderActivity.this, options);
//
//                } catch(Exception e) {
//                    e.printStackTrace();
//
//                }
                Check();
                //startPayment();
            }
        });
    }

    private void Check() {
        if(TextUtils.isEmpty(nameEditText.getText().toString())){
            Toast.makeText(this,"Please Provide Your Full Name",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phoneEditText.getText().toString())){
            Toast.makeText(this,"Please Provide Your Phone Number",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(addressEditText.getText().toString())){
            Toast.makeText(this,"Please Provide Your Valid Address.",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(cityEditText.getText().toString())){
            Toast.makeText(this,"Please Provide Your City Name",Toast.LENGTH_SHORT).show();
        }
        else {
            ConfirmOrder();
        }
    }

    private void ConfirmOrder() {
        final String saveCurrentTime,saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd. yyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());
        final DatabaseReference ordersRef= FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalent.currentOnlineUser.getPhone());
        HashMap<String, Object> ordersMap = new HashMap<>();
        ordersMap.put("totalAmount",totalAmount);
        ordersMap.put("name",nameEditText.getText().toString());
        ordersMap.put("phone",phoneEditText.getText().toString());
        ordersMap.put("address",addressEditText.getText().toString());
        ordersMap.put("city",cityEditText.getText().toString());
        ordersMap.put("date",saveCurrentDate);
        ordersMap.put("time",saveCurrentTime);
        ordersMap.put("state", "Not Shipped");
        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart List")
                            .child("User view")
                            .child(Prevalent.currentOnlineUser.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){

                                        Toast.makeText(ConfirmFinalOrderActivity.this,"Your final Order has been placed successfully.",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ConfirmFinalOrderActivity.this,HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });


    }

    @Override
    public void onPaymentSuccess(String s) {

    }

    @Override
    public void onPaymentError(int i, String s) {

    }
}
//    public void startPayment() {
//        /**
//         * Instantiate Checkout
//         */
//        Checkout checkout = new Checkout();
//        checkout.setKeyID("rzp_test_CDsJoQNxXtK50X");
//        /**
//         * Set your logo here
//         */
//
//        /**
//         * Reference to current activity
//         */
//        final Activity activity = this;
//
//        /**
//         * Pass your payment options to the Razorpay Checkout as a JSONObject
//         */
//        try {
//            JSONObject options = new JSONObject();
//
//            options.put("name", "Merchant Name");
//            options.put("description", "Reference No. #123456");
//            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
//            options.put("theme.color", "#3399cc");
//            options.put("currency", "INR");
//            options.put("amount", "50000");//pass amount in currency subunits
//            options.put("prefill.email", "gaurav.kumar@example.com");
//            options.put("prefill.contact","9988776655");
//            JSONObject retryObj = new JSONObject();
//            retryObj.put("enabled", true);
//            retryObj.put("max_count", 4);
//            options.put("retry", retryObj);
//
//            checkout.open(activity, options);
//
//        } catch(Exception e) {
//            Toast.makeText(activity, "Error in starting Razorpay Checkout", Toast.LENGTH_SHORT).show();
//
//        }
//    }
//    public void startPayment(){
//        Checkout co = new Checkout();
//        Activity activity = this;
//        try {
//            JSONObject option = new JSONObject();
//            option.put("name", "My Cart");
//            option.put("description","App Payment");
//            option.put("image","https://upload.wikimedia.org/wikipedia/en/8/89/Razorpay_logo.png");
//            option.put("currency","INR");
//            option.put("key","rzp_test_CDsJoQNxXtK50X");
//            double totalAmount1 = Double.parseDouble(totalAmount);
//            totalAmount1*=100;  //converting from paise to ruppe
//            option.put("amount","totalAMount1");
//            co.open (activity,option);
//        }catch (Exception e){
//            Log.e("error", "error"+e.toString());
//        }
//    }
//@Override
//public void onPaymentSuccess(String s) {
//    AlertDialog.Builder builder = new AlertDialog.Builder(this);
//    builder.setTitle("Payment ID");
//    builder.setMessage(s);
//    builder.show();
//}
//
//    @Override
//    public void onPaymentError(int i, String s) {
//        Toast.makeText(getApplicationContext(),s, Toast.LENGTH_SHORT).show();
//    }
//}
