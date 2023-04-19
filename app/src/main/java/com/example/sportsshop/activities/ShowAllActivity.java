package com.example.sportsshop.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.sportsshop.R;
import com.example.sportsshop.adapters.ShowAllAdapter;
import com.example.sportsshop.models.PopularProductsModel;
import com.example.sportsshop.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShowAllActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ShowAllAdapter showAllAdapter;
    List<ShowAllModel> showAllModelList;


    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all);

        String category = getIntent().getStringExtra("category");

        firestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.show_all_red);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        showAllModelList = new ArrayList<>();
        showAllAdapter = new ShowAllAdapter(this, showAllModelList);
        recyclerView.setAdapter(showAllAdapter);



        if (category == null || category.isEmpty()){
            firestore.collection("ShowAll")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {


                            if(task.isSuccessful()){
                                for(DocumentSnapshot document : task.getResult().getDocuments()) {
                                    ShowAllModel showAllModel = document.toObject(ShowAllModel.class);
                                    showAllModelList.add(showAllModel);
                                    showAllAdapter.notifyDataSetChanged();
                                }
                            }else{
                                //Toast.makeText(getActivity(), ""+task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }


        if( category != null && category.equalsIgnoreCase("Shirts")){
            firestore.collection("ShowAll").whereEqualTo("category","Shirts")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {


                            if(task.isSuccessful()){
                                for(DocumentSnapshot document : task.getResult().getDocuments()) {
                                    ShowAllModel showAllModel = document.toObject(ShowAllModel.class);
                                    showAllModelList.add(showAllModel);
                                    showAllAdapter.notifyDataSetChanged();
                                }
                            }else{
                                //Toast.makeText(getActivity(), ""+task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        if( category != null && category.equalsIgnoreCase("Shoes")){
            firestore.collection("ShowAll").whereEqualTo("category","Shoes")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {


                            if(task.isSuccessful()){
                                for(DocumentSnapshot document : task.getResult().getDocuments()) {
                                    ShowAllModel showAllModel = document.toObject(ShowAllModel.class);
                                    showAllModelList.add(showAllModel);
                                    showAllAdapter.notifyDataSetChanged();
                                }
                            }else{
                                //Toast.makeText(getActivity(), ""+task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        if( category != null && category.equalsIgnoreCase("Shorts")){
            firestore.collection("ShowAll").whereEqualTo("category","Shorts")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {


                            if(task.isSuccessful()){
                                for(DocumentSnapshot document : task.getResult().getDocuments()) {
                                    ShowAllModel showAllModel = document.toObject(ShowAllModel.class);
                                    showAllModelList.add(showAllModel);
                                    showAllAdapter.notifyDataSetChanged();
                                }
                            }else{
                                //Toast.makeText(getActivity(), ""+task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}