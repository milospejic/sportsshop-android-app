package com.example.sportsshop.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sportsshop.R;
import com.example.sportsshop.adapters.MyCartAdapter;
import com.example.sportsshop.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    public  String overallTotalAmount="0";
    TextView overallAmount;
    Toolbar toolbar;
    RecyclerView recyclerView;
    List<MyCartModel> cartModelList;
    MyCartAdapter cartAdapter;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    Button buyNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        toolbar = findViewById(R.id.cart_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        /*LocalBroadcastManager.getInstance(this)
                .registerReceiver(mMessageReceiver, new IntentFilter("MyTotalAmount"));
*/
        recyclerView = findViewById(R.id.cart_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartModelList = new ArrayList<>();
        //cartAdapter = new MyCartAdapter(this, cartModelList);
        cartAdapter = new MyCartAdapter(this, cartModelList, this);
        recyclerView.setAdapter(cartAdapter);
        overallAmount = findViewById(R.id.textView3);
        buyNow= findViewById(R.id.buy_now);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("User").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult().getDocuments()) {

                                String documentId = document.getId();
                                MyCartModel myCartModel = document.toObject(MyCartModel.class);

                                if(myCartModel != null){
                                    myCartModel.setDocumentId(documentId);
                                }
                                cartModelList.add(myCartModel);
                                cartAdapter.notifyDataSetChanged();
                            }
                            calculateTotalAmount(cartModelList);

                        }
                    }
                });

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this,AddressActivity.class);
                intent.putExtra("item", overallTotalAmount);
                startActivity(intent);
            }
        });

    }

    public void calculateTotalAmount(List<MyCartModel> cartModelList) {
        double totalAmount = 0.0;
        for(MyCartModel myCartModel : cartModelList){
            totalAmount += myCartModel.getTotalPrice();
        }
        overallTotalAmount = String.valueOf(totalAmount);
        overallAmount.setText("Total Amount: " + totalAmount + "rsd");
    }

/*
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int totalBill = intent.getIntExtra("totalAmount", 0);
            overallTotalAmount = String.valueOf(totalBill);
            overallAmount.setText("Total Amount: " + totalBill + "rsd");
        }
    };*/
}