package nl.seventho.androidassessment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Sven Brettschneider on 8-3-2016.
 */
public class URLAdapter extends ArrayAdapter<URLItem> {

    public URLAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public URLAdapter(Context context, int resource, List<URLItem> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.url_list_row, null);
        }

        URLItem p = getItem(position);

        if (p != null) {
            TextView tt2 = (TextView) v.findViewById(R.id.user);
            TextView tt3 = (TextView) v.findViewById(R.id.description);


            if (tt2 != null) {
                tt2.setText(p.getUser());
            }

            if (tt3 != null) {
                tt3.setText(p.getDescription());
            }
        }

        return v;
    }

}
