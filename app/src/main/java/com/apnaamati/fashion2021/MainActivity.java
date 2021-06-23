package com.apnaamati.fashion2021;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public CardView cardMan, cardWomen, cardKids;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        AdSettings.turnOnSDKDebugger(this);
//        AdSettings.setTestMode(true);

        adView = new AdView(this, "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID", AdSize.BANNER_HEIGHT_50);
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);

// Add the ad view to your activity layout
        adContainer.addView(adView);

// Request an ad
        adView.loadAd();

        cardMan = findViewById(R.id.cardMen);
        cardWomen = findViewById(R.id.cardWomen);
        cardKids = findViewById(R.id.cardKids);

        cardMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImagesPage.class);
                intent.putExtra("Men", "Men's Collection");
                startActivity(intent);
            }
        });

        cardWomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImagesPage.class);
                intent.putExtra("Women", "Women's Collection");
                startActivity(intent);
            }
        });

        cardKids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImagesPage.class);
                intent.putExtra("Kids", "Kid's Collection");
                startActivity(intent);
            }
        });

       ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMax(100); // Progress Dialog Max Value
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("Getting Images"); // Setting Title
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);

        //Image Slider
        ImageSlider imageSlider = findViewById(R.id.image_slider);
        final List<SlideModel> remoteImage = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("slider")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data:snapshot.getChildren())
                            remoteImage.add(new SlideModel(data.child("url").getValue().toString(), ScaleTypes.FIT));
                        imageSlider.setImageList(remoteImage,ScaleTypes.FIT);
                        progressDialog.dismiss();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@androidx.annotation.NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fav:
                Intent intent = new Intent(this, favActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
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