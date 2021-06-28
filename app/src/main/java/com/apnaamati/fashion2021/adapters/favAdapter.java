package com.apnaamati.fashion2021.adapters;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apnaamati.fashion2021.OfflineStorage;
import com.apnaamati.fashion2021.R;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class favAdapter extends RecyclerView.Adapter<favAdapter.ViewHolder> {

    Context context;
    Activity activity;
    ArrayList<String> favlist;

    public favAdapter(Context context, ArrayList<String> favlist) {
        this.context = context;
        this.favlist = favlist;
        this.activity = activity;
    }

    @NonNull
    @Override
    public favAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_post,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  favAdapter.ViewHolder holder, int position) {

        Picasso.get().load(favlist.get(position)).networkPolicy(NetworkPolicy.OFFLINE).into(holder.image, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                Picasso.get().load(favlist.get(position)).placeholder(R.drawable.bga).into(holder.image);
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
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                context.startActivity(shareIntent);
            }
        });

        if (position%3 == 0){
            AdLoader.Builder builder = new AdLoader.Builder(
                    context, "ca-app-pub-3490951880662543/5100260507");

            builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                @Override
                public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                    holder.templateView.setNativeAd(unifiedNativeAd);
                }
            });

            final AdLoader adLoader = builder.build();
            adLoader.loadAd(new AdRequest.Builder().build());

            holder.templateView.setVisibility(View.VISIBLE);
        }

        final OfflineStorage offlineStorage = new OfflineStorage(context);
        if (offlineStorage.getString("fav").contains(favlist.get(position))){
            holder.btnFav.setImageResource(R.drawable.ic_baseline_favorite_24);
        }

        holder.btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favlist.remove(position);
                notifyItemRangeChanged(position,favlist.size());
                notifyItemRemoved(position);

                offlineStorage.putListString("fav", favlist);
                holder.btnFav.setImageResource(R.drawable.ic_baseline_favorite_border_24);

            }
        });

        holder.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri downloadUri = Uri.parse(favlist.get(position));
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
    }

    @Override
    public int getItemCount() {
        return favlist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image,btnFav,btnDownload, btnShare;
        TemplateView templateView;
        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img);
            btnFav = itemView.findViewById(R.id.btnFav);
            btnDownload = itemView.findViewById(R.id.btnDownload);
            btnShare =  itemView.findViewById(R.id.btnShare);

            templateView = itemView.findViewById(R.id.my_template);

        }
    }
}
