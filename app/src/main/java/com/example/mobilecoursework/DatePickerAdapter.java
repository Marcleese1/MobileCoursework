package com.example.mobilecoursework;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DatePickerAdapter extends BaseAdapter implements Filterable {

    private List<RssItems> data;
    private List<RssItems>dataFilter;
    Context context;
    Date filterDate;
    private LayoutInflater mLayout;

    public void setSelectedDate(Date time){
        this.filterDate = time;
    }

    private static class Holder {
        TextView title;
        TextView description;
        TextView pubDate;
        TextView startDate;
        TextView endDate;
        TextView geoRss;

    }

    public DatePickerAdapter(List<RssItems> data, Context c){
        this.context = c;
        this.data = data;
        this.dataFilter = data;
        mLayout = LayoutInflater.from(context);
}

    @Override
    public int getCount() {
        return this.dataFilter.size();
    }

    @Override
    public RssItems getItem(int position) {
        return this.dataFilter.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        RssItems dataModel = dataFilter.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        Holder viewHolder; // view lookup cache stored in tag

        final View result;


        viewHolder = new Holder();
        convertView= mLayout.inflate(R.layout.rss_item_list_row, null);
        viewHolder.title = (TextView) convertView.findViewById(R.id.title);
        viewHolder.description = (TextView) convertView.findViewById(R.id.description);
        viewHolder.geoRss = (TextView) convertView.findViewById(R.id.georss);
        viewHolder.pubDate = (TextView) convertView.findViewById(R.id.pubDate);
        viewHolder.startDate = (TextView) convertView.findViewById(R.id.startDate);
        viewHolder.endDate = (TextView) convertView.findViewById(R.id.endDate);

        viewHolder.title.setText(dataModel.getTitle());
        viewHolder.description.setText(dataModel.getDescription());
        viewHolder.geoRss.setText(dataModel.getGeorss());
        viewHolder.pubDate.setText(dataModel.getPubDate());
        viewHolder.startDate.setText(dataModel.getStartDate().toString());
        viewHolder.endDate.setText(dataModel.getEndDate().toString());
        System.out.println(dataModel.getStartDate());

        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public Filter getFilter() {

        return new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                dataFilter = (List<RssItems>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                ArrayList<RssItems> filteredArrayNames = new ArrayList<>();

                // perform your search here using the searchConstraint String.

                String constraintStr = constraint.toString().toLowerCase();

                if(constraintStr.isEmpty() && filterDate ==null) {
                    dataFilter = data;
                }
                else {

                    for (int i = 0; i < data.size(); i++) {
                        String title = data.get(i).getTitle();
                        String desc = data.get(i).getDescription();

                        if (title.toLowerCase().contains(constraintStr.toString())
                                || desc.toLowerCase().contains(constraintStr.toString().toLowerCase())) {

                            if (filterDate != null) {
                                if ((filterDate.after(data.get(i).getStartDate()) || filterDate.equals(data.get(i).getStartDate())) && (filterDate.before(data.get(i).getEndDate()) || filterDate.equals(data.get(i).getEndDate()))) {
                                    filteredArrayNames.add(data.get(i));

                                }
                            } else {
                                filteredArrayNames.add(data.get(i));

                            }

                        }
                    }
                }
                results.count = filteredArrayNames.size();
                results.values = filteredArrayNames;

                return results;
            }
        };
    }




}
