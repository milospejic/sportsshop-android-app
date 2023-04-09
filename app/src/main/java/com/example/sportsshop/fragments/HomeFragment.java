package com.example.sportsshop.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.sportsshop.R;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {



    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ImageSlider imageSlider = root.findViewById(R.id.image_slider);
        List<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.tf, "New nike clothes", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.messi, "Messi shoes", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.air, "Air Force 1", ScaleTypes.CENTER_CROP));

        imageSlider.setImageList(slideModels);
        return root;
    }
}