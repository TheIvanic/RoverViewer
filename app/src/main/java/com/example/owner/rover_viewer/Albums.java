package com.example.owner.rover_viewer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Albums extends AppCompatActivity {

    String date = null;
    String sol = null;
    String rover_name = "";
    Map<String, List<ImageSource>> urls = new ArrayMap<String, List<ImageSource>>();
    ArrayList<String> image_pairs = new ArrayList<String>();
    //String[] test = {"this", "is", "a", "test"};
    boolean nav_found = false;
    String[] cameras;
    static URL url = null;
    static String apiKey = "dgjl169Wvkn8PNtu47va40iMLT5iNGBmm0uXTaTj";

    {
        try {
            url = new URL("https://api.nasa.gov/mars-photos/api/v1/");
        } catch (MalformedURLException e) {
        }
    }

    View.OnClickListener clicks = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (v.getId() == 999) {

                //do stereo thingy
                Intent load_image = new Intent(getApplicationContext(), AppImageLoader.class);


                load_image.putStringArrayListExtra("urls", image_pairs);

                startActivity(load_image);

            }
            else {

                String camera = cameras[v.getId()];
                Intent load_image = new Intent(getApplicationContext(), AppImageLoader.class);

                List<ImageSource> images = urls.get(camera);
                ArrayList<String> pass_this = new ArrayList<String>();
                pass_this.add(camera);
                for (ImageSource imgsrc : images)
                    pass_this.add(imgsrc.url);
                load_image.putStringArrayListExtra("urls", pass_this);

                startActivity(load_image);

            }
            }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // creating LinearLayout

        Intent intent = getIntent();
        if (intent != null) {
            rover_name = intent.getExtras().getString("roverID");
            sol = null;
            date = null;

            if (intent.hasExtra("sol")) {
                sol = intent.getExtras().getString("sol");
            } else {
                date = "";
            }

            if (intent.hasExtra("year")) {
                date = intent.getExtras().getString("year");

            }
            if (intent.hasExtra("month")) {
                date = date.concat("-");
                date = date.concat(intent.getExtras().getString("month"));

            }
            if (intent.hasExtra("day")) {
                date = date.concat("-");
                date = date.concat(intent.getExtras().getString("day"));

            }

            new RetrieveJsonTask().execute();
//            AsyncTask.Status status;
//            while ((status = task.getStatus()) == AsyncTask.Status.PENDING || status == AsyncTask.Status.RUNNING) {
//
//            }

        }


//        createButtons();

    }

    private void createButtons() {
        LinearLayout linLayout = new LinearLayout(this);
        // specifying vertical orientation
        linLayout.setOrientation(LinearLayout.VERTICAL);
        // creating LayoutParams
        LayoutParams linLayoutParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        // set LinearLayout as a root element of the screen
        setContentView(linLayout, linLayoutParam);

        Set<String> c_temp = urls.keySet();
        cameras = c_temp.toArray(new String[0]);

        LayoutParams lpView = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        nav_found = image_pairs.size() > 1;

        for (int i = 0; i < cameras.length; i++) {

            Button btn = new Button(this);
            btn.setId(i);
            btn.setLayoutParams(lpView);
            btn.setTextSize(40);
            btn.setText(cameras[i]);
            btn.setOnClickListener(clicks);
            linLayout.addView(btn);
        }

        if (nav_found) {
            Button stereo_btn = new Button(this);
            stereo_btn.setId(999);
            stereo_btn.setLayoutParams(lpView);
            stereo_btn.setTextSize(40);
            stereo_btn.setText("Stereo Mode");
            stereo_btn.setOnClickListener(clicks);
            linLayout.addView(stereo_btn);
        }

        if (cameras.length == 0) {

            ImageView woops = new ImageView(this);
            woops.setImageResource(R.drawable.nope);
            linLayout.addView(woops);

        }
    }


    // http://stackoverflow.com/questions/6343166/how-to-fix-android-os-networkonmainthreadexception
    class RetrieveJsonTask extends AsyncTask<String, Void, Map<String, List<ImageSource>>> {

        @Override
        protected Map<String, List<ImageSource>> doInBackground(String... params) {
            // http://stackoverflow.com/questions/9605913/how-to-parse-json-in-android
            try {
                String query = "api_key=" + apiKey;
                if (sol != null) query += "&sol=" + sol;
                else if (date != null) query += "&earth_date=" + date;

                HttpURLConnection urlConnection = (HttpURLConnection) new URL(url, "rovers/" + rover_name + "/photos?" + query).openConnection();
                BufferedReader in = null;

                try {
                    urlConnection.addRequestProperty("api_key", apiKey);

                    urlConnection.setChunkedStreamingMode(0);

                    in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = in.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    String result = sb.toString();
                    JSONObject jObject = new JSONObject(result);
                    JSONArray photos = jObject.getJSONArray("photos");

                    int length = photos.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject photo = photos.getJSONObject(i);
                        String img = photo.getString("img_src");
                        JSONObject camera = photo.getJSONObject("camera");
                        String cameraName = camera.getString("name");
                        List<ImageSource> list = urls.get(cameraName);
                        if (list == null)
                            urls.put(cameraName, list = new ArrayList<ImageSource>());
                        list.add(new ImageSource(img, cameraName));
                    }

                    if(urls.containsKey("NAVCAM"))
                    {
                        String left_eye = null;
                        String right_eye = null;
                        image_pairs.add(null);

                        List<ImageSource> nav = urls.get("NAVCAM");
                        for(int i = 0; i < nav.size(); i++)
                        {

                            String base_url = nav.get(i).url;
                            String url_snippet = base_url.substring(base_url.length() - 11, base_url.length() - 7);

                            if(url_snippet.startsWith("L"))
                            {
                                if(right_eye != null && left_eye != null)
                                {
                                    image_pairs.add(left_eye);
                                    image_pairs.add(right_eye);
                                    left_eye = null;
                                    right_eye = null;
                                }
                                left_eye = base_url;
                            }

                            if(url_snippet.startsWith("R"))
                            {
                                right_eye = base_url;
                            }

                        }

                    }

                } finally {
                    urlConnection.disconnect();
                    if (in != null)
                        in.close();
                }

            } catch (Exception e) {
                String x = e.toString();
            }
            return urls;
        }

        protected void onPostExecute(Map<String, List<ImageSource>> result) {
            createButtons();
        }
    }

    static class ImageSource {
        String url;
        String camera;

        public ImageSource(String url, String camera) {
            this.url = url;
            this.camera = camera;
        }
    }
}
