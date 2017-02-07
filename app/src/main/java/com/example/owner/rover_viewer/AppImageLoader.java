package com.example.owner.rover_viewer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

import static android.support.v7.appcompat.R.styleable.View;

public class AppImageLoader extends AppCompatActivity {
    RecyclerView recyclerView;
    String[] URL_Array = new String[0];
    ArrayList<String> urlList;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();
        if (intent != null) {
            urlList = intent.getStringArrayListExtra("urls");
            if (urlList.get(0) != null)
                setContentView(R.layout.activity_app_image_loader);
            else {
                setContentView(R.layout.fullscreen_duo);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }


            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
            ImageLoader.getInstance().init(config);

            if (urlList.get(0) == null) {
                for (int i = 1; i < urlList.size(); i += 2) {
                }
            } else {
                URL_Array = new String[urlList.size() - 1];

                for (int i = 0; i < URL_Array.length; i++) {
                    URL_Array[i] = urlList.get(i + 1);
                }
            }
        }
        String message = "";

        for (int i = 0; i < URL_Array.length; i++) {
            message = message + URL_Array[i] + " ";
        }

        /*TextView textView = new TextView(this);
        textView.setTextSize(15);
        textView.setText(message);

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_app_image_loader);
        layout.addView(textView);*/
        if (urlList.get(0) != null) {
            recyclerView = (RecyclerView) findViewById(R.id.rv_imagelist);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new ListAdapter(this, URL_Array));
        } else {
            showStereo();
        }

        ImageView llayout = (ImageView) findViewById(R.id.imageL);
        if (llayout != null)
        llayout.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showStereo();
            }

        });


    }

    int index = 1;
    private void showStereo() {
        LayoutInflater inflater;
        inflater = LayoutInflater.from(this);

        ImageView imageViewL = (ImageView) findViewById(R.id.imageL);
        ImageView imageViewR = (ImageView) findViewById(R.id.imageR);


        ImageLoader imageLoader = ImageLoader.getInstance();

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();


        imageLoader.displayImage(urlList.get(index), imageViewL, options);
        //imageLoader.destroy();
        imageLoader.displayImage(urlList.get(index+1), imageViewR, options);


        index += 2;
        if (index == urlList.size())
            index = 1;
    }


}
