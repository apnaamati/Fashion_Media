package com.apnaamati.fashion2021.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.apnaamati.fashion2021.R;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;

import com.google.android.ads.mediationtestsuite.MediationTestSuite;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public CardView cardMan, cardWomen, cardKids, cardProducts, cardVideo;
    DatabaseReference databaseReference;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        MediationTestSuite.launch(MainActivity.this);

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

        cardMan = findViewById(R.id.cardMen);
        cardWomen = findViewById(R.id.cardWomen);
        cardKids = findViewById(R.id.cardKids);
        cardProducts = findViewById(R.id.cardProduct);
        cardVideo = findViewById(R.id.cardVideo);

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
        cardProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImagesPage.class);
                intent.putExtra("product", "Products Collection");
                startActivity(intent);
            }
        });

        cardVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImagesPage.class);
                intent.putExtra("video", "Video's Collection");
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
        databaseReference =FirebaseDatabase.getInstance().getReference().child("slider");
        databaseReference.keepSynced(true);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data:snapshot.getChildren()) {
                            remoteImage.add(new SlideModel(data.child("url").getValue().toString(), ScaleTypes.FIT));

                        }
                        imageSlider.setImageList(remoteImage,ScaleTypes.FIT);
                        progressDialog.dismiss();

                        imageSlider.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onItemSelected(int i) {
                                Intent intent = new Intent(MainActivity.this, ShotoutPage.class);
                                intent.putExtra("imageB", remoteImage.get(i).getImageUrl().toString());
                                intent.putExtra("position", i);
                                startActivity(intent);
                            }
                        });

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

            case R.id.feedBack:
                Intent feedback = new Intent(Intent.ACTION_VIEW);
                feedback.setData(Uri.parse("https://forms.gle/RJ56Jd2aoJ77xPJr9"));
                startActivity(feedback);
                return true;

            case R.id.joinUs:
                Intent join = new Intent(Intent.ACTION_VIEW);
                join.setData(Uri.parse("https://forms.gle/m1n6hSPDLxhHfonh8"));
                startActivity(join);
                return true;

            case R.id.rateUs:
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://play.google.com/store/apps/details?id=" +getPackageName()));
                startActivity(i);
                return true;

            case R.id.FMinsta:
                Intent insta = new Intent(Intent.ACTION_VIEW);
                insta.setData(Uri.parse("https://www.instagram.com/fashionmedia.amati/"));
                startActivity(insta);
                return true;

            case R.id.share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Download The Amazing Fashion App. Which Have more then 1000+ images and videos to download.The New Fashion Sale is here! \n"
                        + "Hey please check this application " + "https://play.google.com/store/apps/details?id=" +getPackageName());
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, "Share with...");
                startActivity(shareIntent);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}