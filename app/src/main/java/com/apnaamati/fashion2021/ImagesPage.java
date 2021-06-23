package com.apnaamati.fashion2021;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.apnaamati.fashion2021.adapters.recyclerAdapter;
import com.apnaamati.fashion2021.models.image;
import com.facebook.ads.AdSettings;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public class ImagesPage extends AppCompatActivity {

    public RecyclerView recyclerView;
    DatabaseReference databaseReference;
    recyclerAdapter myAdapter;
    ArrayList<image> images;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_page);

//        AdSettings.turnOnSDKDebugger(this);
//        AdSettings.setTestMode(true);

        MobileAds.initialize(this);

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Bundle bundle = getIntent().getExtras();
        Object men = bundle.get("Men");
        Object women = bundle.get("Women");
        Object kids = bundle.get("Kids");



        recyclerView = findViewById(R.id.imageRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.hasFixedSize();

//        ProgressDialog progressDialog = new ProgressDialog(ImagesPage.this);
//        progressDialog.setMax(100); // Progress Dialog Max Value
//        progressDialog.setMessage("Loading..."); // Setting Message
//        progressDialog.setTitle("Getting Images"); // Setting Title
//        progressDialog.show(); // Display Progress Dialog
//        progressDialog.setCancelable(false);




        if (men != null){
            getSupportActionBar().setTitle(men.toString());
            databaseReference = FirebaseDatabase.getInstance().getReference("mens");
            images = new ArrayList<>();
            myAdapter = new recyclerAdapter(this,images);
            recyclerView.setAdapter(myAdapter);


            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        image image1 = snapshot1.getValue(image.class);
                        images.add(image1);
                    }
                    myAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

        }
        if (women!= null){
            getSupportActionBar().setTitle(women.toString());
            databaseReference = FirebaseDatabase.getInstance().getReference("women");
            images = new ArrayList<>();
            myAdapter = new recyclerAdapter(this,images);
            recyclerView.setAdapter(myAdapter);


            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        image image1 = snapshot1.getValue(image.class);
                        images.add(image1);
                    }
                    myAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
        if (kids != null){
            getSupportActionBar().setTitle(kids.toString());
            databaseReference = FirebaseDatabase.getInstance().getReference("kids");
            images = new ArrayList<>();
            myAdapter = new recyclerAdapter(this,images);
            recyclerView.setAdapter(myAdapter);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        image image1 = snapshot1.getValue(image.class);
                        images.add(image1);
                    }
                    myAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

        }
    }
}