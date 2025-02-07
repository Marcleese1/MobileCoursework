package com.example.mobilecoursework;
//Marc Leese
//S1827987

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class RSSparser {

    private static String TAG_TITLE = "title";
    private static String TAG_DESRIPTION = "description";
    private static String TAG_ITEM = "item";
    private static String TAG_PUB_DATE = "pubDate";
    private static String TAG_GEORSS = "georss:point";

    public RSSparser() {

    }

    public List<RssItems> getRSSFeedItems(String rss_url) {
        List<RssItems> itemsList = new ArrayList<RssItems>();
        String rss_feed_xml;

        rss_feed_xml = this.getXmlFromUrl(rss_url);
        if (rss_feed_xml != null) {
            XmlPullParserFactory factory;
            try {
                factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(new StringReader(rss_feed_xml));
                RssItems Rssitems = null;
                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String eleName = null;
                    switch (eventType){
                        case XmlPullParser.START_TAG:
                            eleName = parser.getName();
                            if(TAG_ITEM.equals(eleName)){
                                Rssitems = new RssItems();
                                itemsList.add(Rssitems);
                            }else if (Rssitems != null){
                                String next = parser.nextText();
                                if(next != null) {
                                    if (TAG_TITLE.equals(eleName)) {
                                            Rssitems.setTitle(next);
                                    } else if (TAG_DESRIPTION.equals(eleName)) {
                                        Rssitems.setDescription(next);
                                    } else if (TAG_PUB_DATE.equals(eleName)) {
                                        Rssitems.setPubDate(next);
                                    } else if (TAG_GEORSS.equals(eleName)) {
                                        Rssitems.setGeorss(next);
                                    }
                            }
                            }
                            break;
                        }

                    eventType = parser.next();
                }

            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
            }
        }
        return itemsList;
    }



    private String getXmlFromUrl(String url) {
        String xml = null;
        try {
           DefaultHttpClient httpClient = new DefaultHttpClient();
           HttpGet httpGet = new HttpGet(url);
           HttpResponse httpResponse = httpClient.execute(httpGet);
           HttpEntity httpEntity = httpResponse.getEntity();
           xml = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException | ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return xml;
    }


}
