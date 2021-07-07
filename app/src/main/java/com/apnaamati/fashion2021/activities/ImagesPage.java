package com.apnaamati.fashion2021.activities;

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
import android.widget.AbsListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.apnaamati.fashion2021.R;
import com.apnaamati.fashion2021.adapters.fashionAdapter;
import com.apnaamati.fashion2021.adapters.productAdapter;
import com.apnaamati.fashion2021.adapters.recyclerAdapter;
import com.apnaamati.fashion2021.adapters.videoAdapter;
import com.apnaamati.fashion2021.models.fashionModel;
import com.apnaamati.fashion2021.models.image;
import com.apnaamati.fashion2021.models.product;
import com.apnaamati.fashion2021.models.video;
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
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImagesPage extends AppCompatActivity {

    public RecyclerView recyclerView;
    DatabaseReference databaseReference;
    fashionAdapter mFashionAdapter;
    List<fashionModel> fashionModels;
    recyclerAdapter myAdapter;
    ArrayList<image> images;
    videoAdapter videoAdapter;
    ArrayList<video> videos;
    productAdapter productAdapters;
    ArrayList<product> products;
    private AdView mAdView;
    int pageNumber = 1;
    Boolean isScrolling = false;
    int currentItem, totalItem,scrollOutItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_page);

//        AdSettings.turnOnSDKDebugger(this);
//        AdSettings.setTestMode(true);

        MobileAds.initialize(this);

        AdView adView = new AdView(this);
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

        Bundle bundle = getIntent().getExtras();
        Object men = bundle.get("Men");
        Object women = bundle.get("Women");
        Object kids = bundle.get("Kids");
        Object product = bundle.get("product");
        Object video = bundle.get("video");



        recyclerView = findViewById(R.id.imageRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setStackFromEnd(true);
//        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItem = linearLayoutManager.getChildCount();
                totalItem = linearLayoutManager.getItemCount();
                scrollOutItem = linearLayoutManager.findFirstVisibleItemPosition();
                if(isScrolling && (currentItem+scrollOutItem==totalItem)){
                    isScrolling = false;
                    fetchMen();
                }
            }
        });
//        recyclerView.hasFixedSize();




        if (men != null){
            getSupportActionBar().setTitle(men.toString());
            fetchMen();
//            databaseReference = FirebaseDatabase.getInstance().getReference("mens");
//            databaseReference.keepSynced(true);
//            images = new ArrayList<>();
//            myAdapter = new recyclerAdapter(this,images);
//            recyclerView.setAdapter(myAdapter);
//
//
//            databaseReference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                    for (DataSnapshot snapshot1: snapshot.getChildren()){
//                        image image1 = snapshot1.getValue(image.class);
//                        images.add(image1);
//                    }
//                    myAdapter.notifyDataSetChanged();
//                }
//
//                @Override
//                public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//                }
//            });

        }
        if (women!= null){
            getSupportActionBar().setTitle(women.toString());
            databaseReference = FirebaseDatabase.getInstance().getReference("women");
            databaseReference.keepSynced(true);
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
            databaseReference.keepSynced(true);
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

        if (product != null){
            getSupportActionBar().setTitle(product.toString());
            databaseReference = FirebaseDatabase.getInstance().getReference("product");
            databaseReference.keepSynced(true);
            products = new ArrayList<>();
            productAdapters = new productAdapter(this,products);
            recyclerView.setAdapter(productAdapters);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        product image1 = snapshot1.getValue(product.class);
                        products.add(image1);
                    }
                    productAdapters.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

        }
        if (video != null){
            getSupportActionBar().setTitle(video.toString());
            databaseReference = FirebaseDatabase.getInstance().getReference("youtube");
            databaseReference.keepSynced(true);
            videos = new ArrayList<>();
            videoAdapter = new videoAdapter(this,videos);
            recyclerView.setAdapter(videoAdapter);


            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        video video1 = snapshot1.getValue(video.class);
                        videos.add(video1);
                    }
                    videoAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

        }
    }

    private void fetchMen() {
        fashionModels = new ArrayList<>();
        mFashionAdapter = new fashionAdapter(this, fashionModels);
        recyclerView.setAdapter(mFashionAdapter);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.pexels.com/v1/search?query=fashion&page="+pageNumber+"&per_page=80",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("photos");

                            int length = jsonArray.length();

                            for (int i=0;i<length;i++){

                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                int ids = jsonObject1.getInt("id");

                                JSONObject objectImage = jsonObject1.getJSONObject("src");
                                String originalUrl = objectImage.getString("original");
                                String mediumUrl = objectImage.getString("medium");

                                fashionModel mFashionModel = new fashionModel(ids,originalUrl,mediumUrl);
                                fashionModels.add(mFashionModel);

                            }
                            mFashionAdapter.notifyDataSetChanged();
                            pageNumber++;

                        }catch (JSONException e){

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> hashMap = new HashMap<>();
                hashMap.put("Authorization","563492ad6f917000010000010370968f125a4dce8133ff72c53d5b27");
                return hashMap;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }
}