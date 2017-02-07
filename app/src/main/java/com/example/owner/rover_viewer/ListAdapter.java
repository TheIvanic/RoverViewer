package com.example.owner.rover_viewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListHolder> {

    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private LayoutInflater inflater;
    private Context context;
    String[] images = {};

    public ListAdapter(Context context, String[] input_images) {

        this.images = new String[input_images.length];
        this.images = input_images;

        this.context = context;
        inflater = LayoutInflater.from(context);

        imageLoader = ImageLoader.getInstance();

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();


    }

    /*public ListAdapter(Context context) {

        this.context = context;
        inflater = LayoutInflater.from(context);

        imageLoader = ImageLoader.getInstance();

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();


    }*/

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.single_row, parent, false);
        ListHolder holder = new ListHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ListHolder holder, int position) {
        imageLoader.displayImage(images[position], holder.imageView, options);
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

class ListHolder extends RecyclerView.ViewHolder {
    ImageView imageView;

    public ListHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.image);
    }
}
}
