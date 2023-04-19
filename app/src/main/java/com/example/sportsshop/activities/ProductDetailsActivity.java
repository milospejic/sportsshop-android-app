package com.example.sportsshop.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sportsshop.R;
import com.example.sportsshop.models.NewProductsModel;
import com.example.sportsshop.models.PopularProductsModel;
import com.example.sportsshop.models.ShowAllModel;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProductDetailsActivity extends AppCompatActivity {


    ImageView detailsImg;
    TextView description, name, rating, price;
    Button addToCart, buyNow;
    ImageView addItems, removeItems;

    NewProductsModel newProductsModel = null;

    PopularProductsModel popularProductsModel = null;

    ShowAllModel showAllModel = null;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        firestore = FirebaseFirestore.getInstance();

        final Object object = getIntent().getSerializableExtra("details");

        if (object instanceof NewProductsModel){
            newProductsModel = (NewProductsModel) object;
        } else if (object instanceof PopularProductsModel){
            popularProductsModel= (PopularProductsModel) object;
        }else if (object instanceof ShowAllModel){
            showAllModel= (ShowAllModel) object;
        }


        detailsImg = findViewById(R.id.details_img);
        description = findViewById(R.id.details_description);
        name = findViewById(R.id.details_name);
        rating = findViewById(R.id.details_rating);
        price = findViewById(R.id.details_price);
        addToCart = findViewById(R.id.add_to_cart);
        buyNow = findViewById(R.id.buy_now);
        addItems = findViewById(R.id.add_item);
        removeItems = findViewById(R.id.remove_item);


        if(newProductsModel != null){
            Glide.with(getApplicationContext()).load(newProductsModel.getImg_url()).into(detailsImg);
            name.setText(newProductsModel.getName());
            description.setText(newProductsModel.getDescription());
            rating.setText(newProductsModel.getRating());
            price.setText(String.valueOf(newProductsModel.getPrice()));

        }

        if(popularProductsModel != null){
            Glide.with(getApplicationContext()).load(popularProductsModel.getImg_url()).into(detailsImg);
            name.setText(popularProductsModel.getName());
            description.setText(popularProductsModel.getDescription());
            rating.setText(popularProductsModel.getRating());
            price.setText(String.valueOf(popularProductsModel.getPrice()));

        }

        if(showAllModel != null){
            Glide.with(getApplicationContext()).load(showAllModel.getImg_url()).into(detailsImg);
            name.setText(showAllModel.getName());
            description.setText(showAllModel.getDescription());
            rating.setText(showAllModel.getRating());
            price.setText(String.valueOf(showAllModel.getPrice()));

        }
    }
}