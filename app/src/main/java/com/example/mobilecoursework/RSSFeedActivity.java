package com.example.mobilecoursework;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.LocaleData;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.EventLogTags;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.example.mobilecoursework.R.*;


public class RSSFeedActivity extends ListActivity {

    private ProgressBar pDialog;
    List<HashMap<String, String>> rssItemList = new ArrayList<>();
    RSSparser rssParser = new RSSparser();
    List<RssItems> rssItems = new ArrayList<>();

    private String date;

   private ListAdapter adapter;


    private static String TAG_TITLE = "title";
    private static String TAG_DESCRIPTION = "description";
    private static String TAG_PUB_DATE = "pubDate";
    private static String TAG_LINK = "link";
    private static String TAG_GEORSS = "georss:point";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_rsfeed);

        EditText filter = (EditText) findViewById(id.filter);
        ListView list = findViewById(android.R.id.list);
        final String rss_link = getIntent().getStringExtra("rssLink");
        new LoadRSSFeedItems().execute(rss_link);
        ListView lv = getListView();



        //on click to take you to the map activity when an item in the list is clicked.
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent in = new Intent(getApplicationContext(), MapsActivity.class);
                String georss = ((TextView) view.findViewById(R.id.georss)).getText().toString();
                String title = ((TextView) view.findViewById(R.id.title)).getText().toString();
                String Desc = ((TextView) view.findViewById(R.id.description)).getText().toString();
                String pubDate = ((TextView) view.findViewById(R.id.pubDate)).getText().toString();
                String[] PubDate = pubDate.split(", | - ");
                String pub1 = PubDate[1];
                String[] Description = Desc.split("<br />");
                String[] latLng = georss.split(" ");
                double lat = Double.parseDouble(latLng[0]);
                double lng = Double.parseDouble(latLng[1]);
                assert rss_link != null;
                if(rss_link.equals("https://trafficscotland.org/rss/feeds/currentincidents.aspx")){
                    String desc1 = Description[0];
                    in.putExtra("desc1", desc1);
                }
                else{
                    String desc1 = Description[0];
                    String desc2 = Description[1];
                    String desc3 = Description[2];
                    String DescComb = desc1 + "\n" + desc2 + "\n" + desc3;
                    in.putExtra("description", DescComb);
                }
                in.putExtra("lat", lat);
                in.putExtra("lng", lng);
                in.putExtra("title", title);
                in.putExtra("rssLink", rss_link);
                in.putExtra("pubDate", pub1);
                startActivity(in);
            }
        });

        //methods for the search bar on the list items
        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ((SimpleAdapter)RSSFeedActivity.this.adapter).getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    class LoadRSSFeedItems extends AsyncTask<String, String, String>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressBar(RSSFeedActivity.this, null, android.R.attr.progressBarStyleLarge);
            RelativeLayout relativeLayout = findViewById(id.relativeLayout);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );

            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            pDialog.setLayoutParams(layoutParams);
            pDialog.setVisibility(View.VISIBLE);
            relativeLayout.addView(pDialog);
        }

        @Override
        protected String doInBackground(String... args) {

            // rss link url
            String rss_url = args[0];
            // list of rss items
            rssItems = rssParser.getRSSFeedItems(rss_url);
            // looping through each item
            for (final RssItems item : rssItems) {
                // creating new HashMap
                if (item.title.equals(""))
                    break;
                HashMap<String, String> map = new HashMap<String, String>();
                HashMap<String, Double> geoRss = new HashMap<>();

                // adding each child node to HashMap key => value
                String givenDateString = item.pubdate.trim();
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");


                try {
                    Date mDate = sdf.parse(givenDateString);
                    SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE, dd MMMM yyyy - hh:mm a", Locale.UK);
                    item.pubdate = sdf2.format(mDate);





                } catch (ParseException e) {
                    e.printStackTrace();

                }
                map.put(TAG_TITLE, item.title);
                map.put(TAG_DESCRIPTION, item.description);
                map.put(TAG_PUB_DATE, item.pubdate);
                map.put(TAG_GEORSS, item.georss);
                item.description.split("<br /");




                rssItemList.add(map);


            }

            runOnUiThread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                public void run() {



                    adapter = new SimpleAdapter(
                            RSSFeedActivity.this,
                            rssItemList, R.layout.rss_item_list_row,
                            new String[]{TAG_DESCRIPTION, TAG_TITLE, TAG_GEORSS, TAG_PUB_DATE},
                            new int[]{id.description, id.title, id.georss, id.pubDate});








                    // updating listview
                    setListAdapter(adapter);
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(String args) {
            pDialog.setVisibility(View.GONE);
            //rssItems = rssParser.getRSSFeedItems(args);



        }
    }
}
