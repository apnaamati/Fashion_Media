package com.apnaamati.fashion2021.adapters;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
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
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


import org.jetbrains.annotations.NotNull;
import java.io.File;

import java.util.ArrayList;

import java.util.List;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.ViewHolder> {

    Context context;
    ArrayList<image> images;


    public recyclerAdapter(Context context, ArrayList<image> images) {
        this.context = context;
        this.images = images;

    }

    @NonNull
    @NotNull
    @Override
    public recyclerAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_post,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull recyclerAdapter.ViewHolder holder, int position) {

        MobileAds.initialize(context);
        if(position%4 == 0) {
            final AdLoader adLoader = new AdLoader.Builder(context, "ca-app-pub-3490951880662543/5100260507")
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(@NonNull @NotNull NativeAd nativeAd) {
                            holder.Adtemplate.setNativeAd(nativeAd);
                            holder.Adtemplate.setVisibility(View.VISIBLE);

                        }
                    }).build();

            adLoader.loadAd(new AdRequest.Builder().build());

        }else{
            holder.Adtemplate.setVisibility(View.GONE);
        }
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

        final boolean[] doubleClick = {false};
        Handler handler=new Handler();

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Runnable r = new Runnable() {
                    @Override
                    public void run() {

                        doubleClick[0] = false;
                    }
                };

                if (doubleClick[0]) {
                    holder.like.setVisibility(View.VISIBLE);
                    AnimationSet animation = new AnimationSet(true);
                    animation.addAnimation(new AlphaAnimation(0.0f, 1.0f));
                    animation.addAnimation(new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f));
                    animation.setDuration(700);
                    animation.setRepeatMode(Animation.REVERSE);
                    holder.btnFav.startAnimation(animation);
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
                    holder.like.startAnimation(animation);
                    holder.like.setVisibility(View.GONE);
                    doubleClick[0] = false;
                    handler.removeCallbacks(r);

                }else {
                    doubleClick[0] =true;
                    handler.postDelayed(r, 500);
                }
            }
        });


        holder.btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animation = new AnimationSet(true);
                animation.addAnimation(new AlphaAnimation(0.0f, 1.0f));
                animation.addAnimation(new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f));
                animation.setDuration(700);
                animation.setRepeatMode(Animation.REVERSE);
                holder.btnFav.startAnimation(animation);
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
        holder.btnShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(image1.getLink()));
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, btnFav, btnDownload, btnShare,like;
        TemplateView Adtemplate;
        Button btnShop;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img);
            btnFav = itemView.findViewById(R.id.btnFav);
            btnDownload = itemView.findViewById(R.id.btnDownload);
            btnShare = itemView.findViewById(R.id.btnShare);
            Adtemplate = itemView.findViewById(R.id.my_template);
            like = itemView.findViewById(R.id.like);
            btnShop = itemView.findViewById(R.id.btnShop);
        }
    }
}
