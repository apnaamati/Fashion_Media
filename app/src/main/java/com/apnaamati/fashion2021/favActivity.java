package com.apnaamati.fashion2021;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.apnaamati.fashion2021.adapters.favAdapter;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;

import java.util.ArrayList;
import java.util.Objects;

public class favActivity extends AppCompatActivity {

    ArrayList<String> favlist = new ArrayList<>();
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Favourite");

//        AdSettings.turnOnSDKDebugger(this);
//        AdSettings.setTestMode(true);

        adView = new AdView(this, "814661372765928_814665022765563", AdSize.BANNER_HEIGHT_50);
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);


        adContainer.addView(adView);

        adView.loadAd();

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

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
}