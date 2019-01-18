package com.br.gridviewmovie;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    static final String TAG = DetailsActivity.class.getSimpleName();
    private TextView titleTextView;
    private TextView averrateTextView;
    private TextView overviewTextView;
    private TextView releasedateTextView;
    private ImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setting details screen layout
        setContentView(R.layout.activity_details_view);
        titleTextView = (TextView) findViewById(R.id.grid_item_title);
        averrateTextView = (TextView) findViewById(R.id.grid_item_averrate);
        overviewTextView = (TextView) findViewById(R.id.grid_item_overview);
        releasedateTextView = (TextView) findViewById(R.id.grid_item_releasedate);
        imageView = (ImageView) findViewById(R.id.grid_item_image);


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //retrieves the moviedetails data    Get the data from intent if there are any
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            //text data
             if(bundle.containsKey("title")){
            String title = bundle.getString("title");
            titleTextView.setText(title);
              }else { titleTextView.setText(R.string.no_data); }


             if(bundle.containsKey("averrate")){
            String averrate = bundle.getString("averrate");
            averrateTextView.setText(averrate);
             }else { averrateTextView.setText(R.string.no_data); }

            if (bundle.containsKey("overview")){
            String overview = bundle.getString("overview");
            overviewTextView.setText(overview);
            }else { overviewTextView.setText(R.string.no_data); }


            if(bundle.containsKey("releasedate")){
            String releasedate = bundle.getString("releasedate");
            releasedateTextView.setText(releasedate);
            }else { releasedateTextView.setText(R.string.no_data); }

            //image URL data
             if(bundle.containsKey("image")){
            String image = bundle.getString("image");
            String URL1 = getString(R.string.MOVIE_PATH) + image;
            Log.d(TAG, image);
            Log.d(TAG, URL1);


            Picasso.with(this).load(URL1)
                    .placeholder(R.mipmap.placeholder)
                    .into(imageView);
             }else { imageView.setImageResource(R.mipmap.placeholder); }


        }else {
            titleTextView.setText(R.string.no_data);
            averrateTextView.setText(R.string.no_data);
            overviewTextView.setText(R.string.no_data);
            releasedateTextView.setText(R.string.no_data);
            imageView.setImageResource(R.mipmap.placeholder);}
        
    }

}
