package com.example.mobilecoursework;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class RSSFeedActivity extends ListActivity {

    private ProgressBar pDialog;
    ArrayList<HashMap<String, String>> rssItemList = new ArrayList<>();
    RSSparser rssParser = new RSSparser();
    List<RssItems> rssItems = new ArrayList<>();

    private static String TAG_TITLE = "title";
    private static String TAG_DESCRIPTION = "description";
    private static String TAG_PUB_DATE = "pubDate";
    private static String TAG_LINK = "link";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsfeed);

        String rss_link = getIntent().getStringExtra("rssLink");
        new LoadRSSFeedItems().execute(rss_link);
        ListView lv = getListView();

/*        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent in = new Intent(getApplicationContext(), RSSFeedActivity.class);
                String page_url = ((TextView) view.findViewById(R.id.rssTitle)).getText().toString();
                in.putExtra("url", page_url);
                startActivity(in);
            }
        });*/
    }

    private class LoadRSSFeedItems extends AsyncTask<String, String, String>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressBar(RSSFeedActivity.this, null, android.R.attr.progressBarStyleLarge);
            RelativeLayout relativeLayout = findViewById(R.id.relativeLayout);
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
                //map.put(TAG_LINK, item.description);


                rssItemList.add(map);

            }

            runOnUiThread(new Runnable() {
                public void run() {
                    ListAdapter adapter = new SimpleAdapter(
                            RSSFeedActivity.this,
                            rssItemList, R.layout.rss_item_list_row,
                            new String[]{TAG_DESCRIPTION, TAG_TITLE, TAG_PUB_DATE},
                            new int[]{R.id.description, R.id.title});

                    // updating listview
                    setListAdapter(adapter);
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(String args) {
            pDialog.setVisibility(View.GONE);

        }
    }
}
