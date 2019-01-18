package com.br.gridviewmovie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GridViewActivity extends AppCompatActivity {
    private static final String TAG = GridViewActivity.class.getSimpleName();

    private GridView mGridView;
    private ProgressBar mProgressBar;

    private GridViewAdapter mGridAdapter;
    private ArrayList<GridItem> mGridData;
    private String FEED_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridview);


        mGridView = (GridView) findViewById(R.id.gridView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        //Initialize with empty data
        mGridData = new ArrayList<>();
        mGridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, mGridData);
        mGridView.setAdapter(mGridAdapter);

        //Grid view click event
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                GridItem item = (GridItem) parent.getItemAtPosition(position);
                Log.d(TAG, item.toString());

                Intent intent = new Intent(GridViewActivity.this, DetailsActivity.class);
               //put data into intent passing to DetailsActivity.class
                intent.putExtra("title", item.getTitle()).
                       putExtra("image", item.getImage()).
                        putExtra("averrate", item.getAverrate()).
                        putExtra("overview", item.getOverview()).
                        putExtra("releasedate", item.getReleasedate());

                //Log.d(TAG, "intent input title"+item.getTitle());
                // Log.d(TAG, "intent input imageurl"+item.getImage());

                //Start details activity
                startActivity(intent);
            }
        });

        //input discover URL

        Uri FEED_URI= Uri.parse(getString(R.string.MOVIE_DIS)).buildUpon()
                .appendPath(getString(R.string.MOVIE_SORT_popular))
                .appendQueryParameter("api_key", BuildConfig.API_KEY)
                .build();
        FEED_URL= FEED_URI.toString();


      // Log.d(TAG, "begin url"+FEED_URL);
        //Start download
         new AsyncHttpTask().execute(FEED_URL);

        mProgressBar.setVisibility(View.VISIBLE);
    }

    //sort_menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //choose sort_menu to get different FEED_URL
        if (id == R.id.sort_popular ) {


            Uri FEED_URI= Uri.parse(getString(R.string.MOVIE_DIS)).buildUpon()
                    .appendPath(getString(R.string.MOVIE_SORT_popular))
                    .appendQueryParameter("api_key", BuildConfig.API_KEY)
                    .build();

            FEED_URL= FEED_URI.toString();
           // Log.d(TAG, "popular url"+FEED_URL);


            mGridAdapter.clear();
            new AsyncHttpTask().execute(FEED_URL);

            return true;
        }


        else if(id ==R.id.sort_rate){


            Uri FEED_URI= Uri.parse(getString(R.string.MOVIE_DIS)).buildUpon()
                    .appendPath(getString(R.string.MOVIE_SORT_rate))
                    .appendQueryParameter("api_key", BuildConfig.API_KEY)
                    .build();
            FEED_URL= FEED_URI.toString();

           // Toast.makeText(getApplicationContext(), FEED_URL, Toast.LENGTH_LONG).show();
           // Log.d(TAG, "rate url"+FEED_URL);
            mGridAdapter.clear();
        new AsyncHttpTask().execute(FEED_URL);

        return true;
    }

    else return super.onOptionsItemSelected(item);
}


    //Downloading data asynchronously
    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            try {
                // Create Apache HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse httpResponse = httpclient.execute(new HttpGet(params[0]));
                int statusCode = httpResponse.getStatusLine().getStatusCode();

                // 200 represents HTTP, this can check the internet connection.
                //if no net, statuscode!==200, the int result=0,leading onPostExecute show Toast.
                if (statusCode == 200) {
                    String response = streamToString(httpResponse.getEntity().getContent());
                    parseResult(response);
                    result = 1; // Successful
                } else {
                    result = 0; //Failed
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }

            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. update UI

            if (result == 1) {
                mGridAdapter.setGridData(mGridData);
            } else {
                Toast.makeText(GridViewActivity.this, R.string.Net_unconnect, Toast.LENGTH_SHORT).show();

            }

            //Hide progressbar
            mProgressBar.setVisibility(View.GONE);
        }
    }


    String streamToString(InputStream stream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

        // Close stream
        if (null != stream) {
            stream.close();
        }
        Log.d(TAG, result);
        return result;
    }

    // Parsing the feed results and get the movie list

    private void parseResult(String result) {


        //parsing json from API. movie information .
        try{
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("results");
            GridItem item;
            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                String title = post.optString("title");//2
                String averrate = post.optString("vote_average");
                String overview=post.optString("overview");
                String releasedate=post.optString("release_date");



                item = new GridItem();
                String poster_path=post.optString("poster_path");
                //Log.d(TAG,title);
                //String base_url1="http://image.tmdb.org/t/p/w185/ ";
               // String url1=base_url1+poster_path;
                //Log.d(TAG,url1);


                //item.set values;
                item.setImage(poster_path);
                item.setTitle(title);
                item.setAverrate(averrate);
                item.setOverview(overview);
                item.setReleasedate(releasedate);


                mGridData.add(item);
            }
        }


        catch (JSONException e) {
            e.printStackTrace();
        }
    }



}