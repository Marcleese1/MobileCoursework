package com.example.mobilecoursework;

public class RssItems {

    public String title;
    public String description;
    public String georss;
    public String pubDate;



    public RssItems() {
        this.title = "UNDEFINED";
    }

    public RssItems(String title, String description, String link, String georss, String pubDate){
        super();
        this.title = title;
        this.description = description;
        this.georss = georss;
        this.pubDate = pubDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getGeorss() {
        return georss;
    }

    public void setGeorss(String georss) {
        this.georss = georss;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    @Override
    public String toString() {
        return "RSSItems{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", georss='" + georss + '\'' +
                ", pubDate='" + pubDate + '\'' +
                '}';
    }
}


