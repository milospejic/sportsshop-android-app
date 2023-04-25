package com.example.sportsshop.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sportsshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class PaymentActivity extends AppCompatActivity {

    public static final String clientKey = "AZtJ_Qcy1Ke8DlBqxQgfO6SZfaxnZVM-CF6nnJTyVHq4_p6RvgoNiU5xCLz6oM_JEs1VEw-XCpT7wnJM";
    public static final int PAYPAL_REQUEST_CODE = 123;

    FirebaseFirestore firestore;
    FirebaseAuth auth;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(clientKey);
    Toolbar toolbar;
    TextView subTotal, discount, shipping, total;
    Button checkOutBtn;
    double amount = 0.0;
    double totalAmount = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        toolbar = findViewById(R.id.payment_toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        amount = getIntent().getDoubleExtra("amount",0.0);

        subTotal = findViewById(R.id.sub_total);
        discount = findViewById(R.id.textView17);
        shipping = findViewById(R.id.textView18);
        total = findViewById(R.id.total_amt);
        checkOutBtn = findViewById(R.id.pay_btn);
        subTotal.setText(String.valueOf(amount));
        totalAmount = amount;
        if(amount >20000){
            discount.setText(String.valueOf(amount*0.2));
            totalAmount -=(amount * 0.2);
        }else{
            shipping.setText(String.valueOf(500));
            totalAmount+=500;
        }
        total.setText(String.valueOf(totalAmount));

        checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPayment();
            }
        });

    }

    private void getPayment() {

        String amount = total.getText().toString();

        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), "USD", "Pay me",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, com.paypal.android.sdk.payments.PaymentActivity.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAYPAL_REQUEST_CODE) {
            String payment = "No";
            Map<String,String> map = new HashMap<>();
            map.put("userId", auth.getCurrentUser().getUid());
            map.put("userEmail", auth.getCurrentUser().getEmail());
            map.put("amount", total.getText().toString());
            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
            map.put("date",  currentDate.format(calForDate.getTime()));
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    payment = "Yes";
                    map.put("paid",  payment);
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                map.put("paid",  payment);
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_EXTRAS_INVALID) {
                map.put("paid",  payment);
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
            String finalPayment = payment;
            firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                    .collection("Orders").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            Toast.makeText(PaymentActivity.this, "Order registered. Paid: " + finalPayment, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(PaymentActivity.this,MainActivity.class));
                        }
                    });
        }
    }
}







