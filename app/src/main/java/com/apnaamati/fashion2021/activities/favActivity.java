package com.apnaamati.fashion2021.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.apnaamati.fashion2021.OfflineStorage;
import com.apnaamati.fashion2021.R;
import com.apnaamati.fashion2021.adapters.favAdapter;
import com.facebook.ads.AdSettings;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.Objects;

public class favActivity extends AppCompatActivity {

    ArrayList<String> favlist = new ArrayList<>();
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);
//        Objects.requireNonNull(getSupportActionBar()).setTitle("Favourite");
        getSupportActionBar().setTitle("Favourite");

        MobileAds.initialize(this);

        com.google.android.gms.ads.AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3490951880662543/8847933822");

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        OfflineStorage offlineStorage = new OfflineStorage(getApplicationContext());
        favlist = offlineStorage.getListString("fav");

        if (!favlist.isEmpty()){
           RecyclerView recyclerView = findViewById(R.id.imageRecyler);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setStackFromEnd(true);
            linearLayoutManager.setReverseLayout(true);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.hasFixedSize();

            favAdapter favAdapter = new favAdapter(this, favlist);
            recyclerView.setAdapter(favAdapter);

        }else{
            Toast.makeText(getApplicationContext(), "No Image Added", Toast.LENGTH_LONG).show();
        }
    }

}