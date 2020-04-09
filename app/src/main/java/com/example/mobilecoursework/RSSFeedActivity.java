package com.example.mobilecoursework;
//Marc Leese
//S1827987

import android.app.DatePickerDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.example.mobilecoursework.R.id;
import static com.example.mobilecoursework.R.layout;


public class RSSFeedActivity extends ListActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemClickListener{

    private ProgressBar pDialog;
    List<HashMap<String, String>> rssItemList = new ArrayList<>();
    RSSparser rssParser = new RSSparser();
    List<RssItems> rssItems = new ArrayList<>();
    Date selectedDate;
    private DatePickerAdapter adapter;
    private DatePickerAdapter dateadapter;



    private static String TAG_TITLE = "title";
    private static String TAG_DESCRIPTION = "description";
    private static String TAG_PUB_DATE = "pubDate";
    private static String TAG_GEORSS = "georss:point";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_rsfeed);
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
                String[] latLng = georss.split(" ");
                double lat = Double.parseDouble(latLng[0]);
                double lng = Double.parseDouble(latLng[1]);
                assert rss_link != null;

                in.putExtra("description", Desc);
                in.putExtra("lat", lat);
                in.putExtra("lng", lng);
                in.putExtra("title", title);
                in.putExtra("rssLink", rss_link);
                in.putExtra("pubDate", pub1);
                startActivity(in);
            }
        });

        final EditText filter = (EditText) findViewById(id.filter);
        //methods for the search bar on the list items
        filter.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                adapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    class LoadRSSFeedItems extends AsyncTask<String, String, String> {


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
              /*  if (item.title.equals(""))
                    break;*/
                HashMap<String, String> map = new HashMap<String, String>();

                // adding each child node to HashMap key => value
                String givenDateString = item.pubDate.trim();
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);


                try {
                    Date mDate = sdf.parse(givenDateString);
                    SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE, dd MMMM yyyy - hh:mm a", Locale.ENGLISH);

                    item.pubDate = sdf2.format(mDate);


                } catch (ParseException e) {
                    e.printStackTrace();

                }
                map.put(TAG_TITLE, item.title);
                map.put(TAG_DESCRIPTION, item.description);
                map.put(TAG_PUB_DATE, item.pubDate);
                map.put(TAG_GEORSS, item.georss);


                rssItemList.add(map);


            }

            runOnUiThread(new Runnable() {
                public void run() {


                    adapter = new DatePickerAdapter(rssItems, getApplicationContext());
                    setListAdapter(adapter);

                    //Search bar
                    EditText ItemSearch = (EditText)findViewById(R.id.filter);
                    ItemSearch.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            System.out.println(s);
                            adapter.getFilter().filter(s);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                }
            });
            return null;
        }


        @Override
        protected void onPostExecute(String args) {
            pDialog.setVisibility(View.GONE);

        }

    }
    public void showDatePicker(View v){
        DatePickerDialog dataPickerDialog =  createDataPickerDialog( v);
        dataPickerDialog.show();
    }

    public DatePickerDialog createDataPickerDialog(View v) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(v.getContext() , this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        this.adapter.setSelectedDate(calendar.getTime());
        TextView textView =  (TextView) findViewById(R.id.txtSelectedDate);
        textView.setText(calendar.getTime().toString());
        this.adapter.getFilter().filter("");

    }

}
