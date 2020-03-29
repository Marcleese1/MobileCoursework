package com.example.mobilecoursework;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<String> rssLinks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnRoadworks = findViewById(R.id.roadworks);
        Button btnPlanned = findViewById(R.id.planned);
        Button btnCurrent = findViewById(R.id.current);

        btnRoadworks.setOnClickListener(this);
        btnPlanned.setOnClickListener(this);
        btnCurrent.setOnClickListener(this);

        rssLinks.add("https://trafficscotland.org/rss/feeds/plannedroadworks.aspx");
        rssLinks.add("https://trafficscotland.org/rss/feeds/roadworks.aspx");
        rssLinks.add("https://trafficscotland.org/rss/feeds/currentincidents.aspx");
    }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), RSSFeedActivity.class);
            switch (view.getId()) {
                case R.id.roadworks:
                    intent.putExtra("rssLink", rssLinks.get(1));
                    startActivity(intent);
                    break;
                case R.id.planned:
                    intent.putExtra("rssLink", rssLinks.get(0));
                    startActivity(intent);
                    break;
                case R.id.current:
                    intent.putExtra("rssLink", rssLinks.get(2));
                    startActivity(intent);
                    break;





            }



        }


    }
