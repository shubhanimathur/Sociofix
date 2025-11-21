package com.example.project31.post;
import android.content.Context;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.project31.R;
//import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//import com.nostra13.universalimageloader.core.assist.FailReason;
//import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
//import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
/**
 * Created by User on 6/4/2017.
 */

//public class GridImageAdapter extends ArrayAdapter<String>{
//
//    private Context mContext;
//    private LayoutInflater mInflater;
//    private int layoutResource;
//    private String mAppend;
//    private ArrayList<String> imgURLs;
//
//    public GridImageAdapter(Context context, int layoutResource, String append, ArrayList<String> imgURLs) {
//        super(context, layoutResource, imgURLs);
//        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        mContext = context;
//        this.layoutResource = layoutResource;
//        mAppend = append;
//        this.imgURLs = imgURLs;
//    }
//
//    private static class ViewHolder{
//        SqaureImageView image;
//        ProgressBar mProgressBar;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//
//        /*
//        Viewholder build pattern (Similar to recyclerview)
//         */
//        final ViewHolder holder;
//        if(convertView == null){
//            convertView = mInflater.inflate(layoutResource, parent, false);
//            holder = new ViewHolder();
//            holder.mProgressBar = (ProgressBar) convertView.findViewById(R.id.gridImageProgressbar);
//            holder.image = (SqaureImageView) convertView.findViewById(R.id.gridImageView);
//
//            convertView.setTag(holder);
//        }
//        else{
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//        String imgURL = getItem(position);
//
//        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(mContext);
//        config.threadPriority(Thread.NORM_PRIORITY - 2);
//        config.denyCacheImageMultipleSizesInMemory();
//        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
//        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
//        config.tasksProcessingOrder(QueueProcessingType.LIFO);
//        config.writeDebugLogs(); // Remove for release app
//
//        // Initialize ImageLoader with configuration.
//       ImageLoader.getInstance().init(config.build());
//      //  ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mContext));
//
//        ImageLoader imageLoader = ImageLoader.getInstance();
//try{
//    imageLoader.displayImage(mAppend + imgURL, holder.image, new ImageLoadingListener() {
//        @Override
//        public void onLoadingStarted(String imageUri, View view) {
//            if(holder.mProgressBar != null){
//                holder.mProgressBar.setVisibility(View.VISIBLE);
//            }
//        }
//
//        @Override
//        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//            if(holder.mProgressBar != null){
//                holder.mProgressBar.setVisibility(View.GONE);
//            }
//        }
//
//        @Override
//        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//            if(holder.mProgressBar != null){
//                holder.mProgressBar.setVisibility(View.GONE);
//            }
//        }
//
//        @Override
//        public void onLoadingCancelled(String imageUri, View view) {
//            if(holder.mProgressBar != null){
//                holder.mProgressBar.setVisibility(View.GONE);
//            }
//        }
//    });
//}catch(Exception e) {
//        }
//
//
//
//        return convertView;
//    }
//}

public class GridImageAdapter extends ArrayAdapter<String>{

    private Context mContext;
    private LayoutInflater mInflater;
    private int layoutResource;
    private String mAppend;
    private ArrayList<String> imgURLs;

    public GridImageAdapter(Context context, int layoutResource, String append, ArrayList<String> imgURLs) {
        super(context, layoutResource, imgURLs);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        this.layoutResource = layoutResource;
        mAppend = append;
        this.imgURLs = imgURLs;
    }

    private static class ViewHolder{
        SqaureImageView image;
        ProgressBar mProgressBar;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        /*
        Viewholder build pattern (Similar to recyclerview)
         */
        final ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder();
            holder.mProgressBar = (ProgressBar) convertView.findViewById(R.id.gridImageProgressbar);
            holder.image = (SqaureImageView) convertView.findViewById(R.id.gridImageView);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        String imgURL = getItem(position);

        holder.mProgressBar.setVisibility(View.VISIBLE);

        Glide.with(mContext)
                .load(imgURL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.mProgressBar.setVisibility(View.GONE);
                 //       Toast.makeText(mContext,"h "+"Failed",Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.mProgressBar.setVisibility(View.GONE);
                  //      Toast.makeText(mContext,"h "+"Success",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                })
                .into(holder.image);

        return convertView;
    }
}


