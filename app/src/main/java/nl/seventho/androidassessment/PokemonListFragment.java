package nl.seventho.androidassessment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PokemonListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PokemonListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PokemonListFragment extends Fragment {

    private OnItemSelectedListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rsslist_overview,
                container, false);
        //Button button = (Button) view.findViewById(R.id.button1);

        ArrayList<URLItem> list = new ArrayList();

        URLItem item1 = new URLItem();
        item1.setURL("http://www.kennislink.nl");
        item1.setUser("Sven");
        item1.setDescription("Science Site");

        URLItem item2 = new URLItem();
        item2.setURL("http://www.google.nl");
        item2.setUser("Sven");
        item2.setDescription("Zoekmachine");

        list.add(item1);
        list.add(item2);

        ListView yourListView = (ListView) view.findViewById(R.id.urlListView);

        // get data from the table by the ListAdapter
        URLAdapter customAdapter = new URLAdapter(getContext(), R.layout.url_list_row, list);

        yourListView.setAdapter(customAdapter);

        yourListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                URLItem item = (URLItem)parent.getItemAtPosition(position);
                updateDetail(item.getURL());
            }
        });

        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDetail("http://kennislink.nl");
            }
        });*/
        return view;
    }

    public interface OnItemSelectedListener {
        public void onRssItemSelected(String link);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement PokemonListFragment.OnItemSelectedListener");
        }
    }

    // triggers update of the details fragment
    public void updateDetail(String uri) {
        // create fake data
        String newTime = uri;
        // send data to activity
        listener.onRssItemSelected(newTime);
    }
}
