package com.example.sportsshop.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportsshop.R;
import com.example.sportsshop.activities.CartActivity;
import com.example.sportsshop.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.ViewHolder> {

    private Context context;
    private List<MyCartModel> list;
    private CartActivity cartActivity;
    int totalAmount = 0;

    FirebaseAuth auth;
    FirebaseFirestore firestore;

    public MyCartAdapter(Context context, List<MyCartModel> list) {
        this.context = context;
        this.list = list;
        auth= FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    public MyCartAdapter(Context context, List<MyCartModel> list, CartActivity cartActivity) {
        this.context = context;
        this.list = list;
        this.cartActivity=cartActivity;
        auth= FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

    }

    @NonNull
    @Override
    public MyCartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyCartAdapter.ViewHolder(LayoutInflater.from((parent.getContext())).inflate(R.layout.my_cart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyCartAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.date.setText(list.get(position).getCurrentDate());
        holder.time.setText(list.get(position).getCurrentTime());
        holder.price.setText(list.get(position).getProductPrice());
        holder.name.setText(list.get(position).getProductName());
        holder.totalPrice.setText(String.valueOf(list.get(position).getTotalPrice()));
        holder.totalQuantity.setText(list.get(position).getTotalQuantity());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                        .collection("User").document(list.get(position).getDocumentId())
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    cartActivity.overallTotalAmount = String.valueOf(Double.parseDouble(cartActivity.overallTotalAmount) - list.get(position).getTotalPrice());
                                    list.remove(list.get(position));
                                    notifyDataSetChanged();
cartActivity.calculateTotalAmount(list);
                                    Toast.makeText(context, "Successfully deleted!", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });


    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView date,time,name, price, totalPrice, totalQuantity;
        ImageView delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.current_date);
            time = itemView.findViewById(R.id.current_time);
            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
            totalPrice = itemView.findViewById(R.id.total_price);
            totalQuantity = itemView.findViewById(R.id.total_quantity);
            delete = itemView.findViewById(R.id.delete);


        }
    }
}