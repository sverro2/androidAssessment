package nl.seventho.androidassessment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Rik van den Heuvel on 8-3-2016.
 */
public class PokemonListAdapter extends ArrayAdapter<PokemonListItem> implements Filterable{
    List<PokemonListItem> pokemonList;

    public PokemonListAdapter(Context context, int resource, List<PokemonListItem> items) {
        super(context, resource, items);
        this.pokemonList = new ArrayList<>();
        pokemonList.addAll(items);
    }

    Filter myFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            ArrayList<PokemonListItem> tempList=new ArrayList<>();
            //constraint is the result from text you want to filter against.
            //objects is your data set you will filter from
            //Log.v("Logger", "The constraint is now: " + constraint.toString());
            if(constraint != null && pokemonList!=null) {
                int length=pokemonList.size();
                int i=0;
                while(i<length){
                    PokemonListItem item=pokemonList.get(i);
                    //do whatever you wanna do here
                    //adding result set output array
                    boolean containsString = item.getName().toLowerCase().contains(constraint.toString().toLowerCase());
                    if(containsString){
                        tempList.add(item);
                        //Log.v("Logger", "Constraint: " + constraint.toString().toLowerCase() + " value: " + item.getName() + " bool: " + containsString);
                    }

                    i++;
                }
                //following two lines is very important
                //as publish result can only take FilterResults objects
                filterResults.values = tempList;
                filterResults.count = tempList.size();
            }
            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence contraint, FilterResults results) {
            List<PokemonListItem> pokemonList = (List<PokemonListItem>) results.values;
            if (results.count > 0) {
                clear();
                addAll(pokemonList);
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    };

    @Override
    public Filter getFilter() {
        return myFilter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.url_list_row, null);
        }

        PokemonListItem p = getItem(position);

        if (p != null) {
            TextView name = (TextView) v.findViewById(R.id.name);


            if (name != null) {
                name.setText(p.getName());
            }
        }

        return v;
    }

}
