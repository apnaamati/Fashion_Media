package com.apnaamati.fashion2021.adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apnaamati.fashion2021.OfflineStorage;
import com.apnaamati.fashion2021.R;
import com.apnaamati.fashion2021.models.image;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.ViewHolder> {

    Context context;
    ArrayList<image> images;

    private LinearLayout adView;
    private NativeAd nativeAd;

    View containerView;

    public recyclerAdapter(Context context, ArrayList<image> images) {
        this.context = context;
        this.images = images;

    }

    @NonNull
    @NotNull
    @Override
    public recyclerAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        containerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.nativead_layout,parent,false);

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_post,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull recyclerAdapter.ViewHolder holder, int position) {

        AudienceNetworkAds.initialize(context);
        nativeAd = new NativeAd(context, "814661372765928_814665296098869");



            NativeAdListener nativeAdListener = new NativeAdListener() {
                @Override
                public void onMediaDownloaded(Ad ad) {
                    // Native ad finished downloading all assets
                    Log.e("ADSNATIVE", "Native ad finished downloading all assets.");
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    // Native ad failed to load
                    Log.e("ADSNATIVE", "Native ad failed to load: " + adError.getErrorMessage());
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    // Native ad is loaded and ready to be displayed
                    Log.d("ADSNATIVE", "Native ad is loaded and ready to be displayed!");


                    if (nativeAd == null || nativeAd != ad || position%3 ==0 ) {
                        return;
                    }
                    nativeAd.unregisterView();

                    // Add the Ad view into the ad container.
                    LayoutInflater inflater = LayoutInflater.from(context);
                    // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
                    adView = (LinearLayout) inflater.inflate(R.layout.nativead_layout, holder.nativeAdLayout, false);
                    holder.nativeAdLayout.addView(adView);

                    // Add the AdOptionsView
                    LinearLayout adChoicesContainer = containerView.findViewById(R.id.ad_choices_container);
                    AdOptionsView adOptionsView = new AdOptionsView(context, nativeAd, holder.nativeAdLayout);
                    adChoicesContainer.removeAllViews();
                    adChoicesContainer.addView(adOptionsView, 0);

                    // Create native UI using the ad metadata.
                    MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
                    TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
                    MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
                    TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
                    TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
                    TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
                    Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

                    // Set the Text.
                    nativeAdTitle.setText(nativeAd.getAdvertiserName());
                    nativeAdBody.setText(nativeAd.getAdBodyText());
                    nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
                    nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
                    nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
                    sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

                    // Create a list of clickable views
                    List<View> clickableViews = new ArrayList<>();
                    clickableViews.add(nativeAdTitle);
                    clickableViews.add(nativeAdCallToAction);

                    // Register the Title and CTA button to listen for clicks.
                    nativeAd.registerViewForInteraction(
                            adView, nativeAdMedia, nativeAdIcon, clickableViews);


                    holder.nativeAdLayout.setVisibility(View.VISIBLE);

                }

                @Override
                public void onAdClicked(Ad ad) {
                    // Native ad clicked
                    Log.d("ADSNATIVE", "Native ad clicked!");
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    // Native ad impression
                    Log.d("ADSNATIVE", "Native ad impression logged!");
                }
            };

            // Request an ad
            nativeAd.loadAd(
                    nativeAd.buildLoadAdConfig()
                            .withAdListener(nativeAdListener)
                            .build());


        image image1 =  images.get(position);


        Picasso.get().load(image1.getUrl()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.imageView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                Picasso.get().load(image1.getUrl()).placeholder(R.drawable.bga).into(holder.imageView);
            }
        });




        final OfflineStorage offlineStorage = new OfflineStorage(context);
        if (offlineStorage.getString("fav").contains(images.get(position).getUrl())){
            holder.btnFav.setImageResource(R.drawable.ic_baseline_favorite_24);
        }


        holder.btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> favImage = offlineStorage.getListString("fav");
                if (favImage.isEmpty()){
                    favImage.add(images.get(position).getUrl());
                    offlineStorage.putListString("fav",favImage);
                    holder.btnFav.setImageResource(R.drawable.ic_baseline_favorite_24);
                }if (!favImage.isEmpty()){

                    if (favImage.contains(images.get(position).getUrl())){
//                        favImage.remove(images.get(position).getUrl());
//                        offlineStorage.putListString("fav", favImage);
                        Toast.makeText(context, "Image is already in Fav!", Toast.LENGTH_SHORT).show();

                    }else {
                        favImage.add(images.get(position).getUrl());
                        offlineStorage.putListString("fav",favImage);
                        holder.btnFav.setImageResource(R.drawable.ic_baseline_favorite_24);
                    }
                }
            }
        });

        holder.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri downloadUri = Uri.parse(images.get(position).getUrl());
                    DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedOverRoaming(false)
                            .setTitle("Fashion Media")
                            .setMimeType("image/jpeg") // Your file type. You can use this code to download other file types also.
                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                            .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, File.separator + "fashionImage" + ".jpg");
                    dm.enqueue(request);
                    Toast.makeText(context, "Image download started.", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(context, "Image download failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Download The Amazing Fashion App. Which Have more then 1000+ images and videos to download.The New Fashion Sale is here! \n"
                        + "Hey please check this application " + "https://play.google.com/store/apps/details?id=" +context.getPackageName());
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, "Share with...");
                context.startActivity(shareIntent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, btnFav, btnDownload, btnShare;
        NativeAdLayout nativeAdLayout;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img);
            btnFav = itemView.findViewById(R.id.btnFav);
            btnDownload = itemView.findViewById(R.id.btnDownload);
            nativeAdLayout = itemView.findViewById(R.id.native_ad_container);
            btnShare = itemView.findViewById(R.id.btnShare);

        }
    }
}
