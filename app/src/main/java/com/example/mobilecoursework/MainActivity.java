package com.example.mobilecoursework;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


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
            Intent map = new Intent(getApplicationContext(), MapsActivity.class);
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
