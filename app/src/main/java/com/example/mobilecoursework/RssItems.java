package com.example.mobilecoursework;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class RssItems {

    public String title;
    public String link;
    public String description;
    public String pubdate;
    public String guid;

    public RssItems(String title, String link, String description, String pubdate, String guid) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubdate = pubdate;
        this.guid = guid;

    }



}
