package nl.seventho.androidassessment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    // json object response url
    private String urlJsonPokemonList = "http://pokeapi.co/api/v2/pokemon?limit=811";

    // Progress dialog
    private ProgressDialog pDialog;
    private String TAG;
    private Context context;

    // Listview Adapter
    ListView yourListView;
    List<PokemonListItem> pokemonList;
    PokemonListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pokemon_list_overview,
                container, false);

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Refreshing pokemon list...");
        pDialog.setCancelable(false);
        TAG = PokemonViewer.class.getSimpleName();
        context = getContext();

        yourListView = (ListView) view.findViewById(R.id.urlListView);
        refreshPokemonListItems();

        EditText inputSearch = (EditText) view.findViewById(R.id.searchInput);

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                if(adapter != null){
                    adapter.getFilter().filter(cs);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {}

            @Override
            public void afterTextChanged(Editable arg0) {}
        });

        return view;
    }

    public interface OnItemSelectedListener {
        void onPokemonSelected(String link);
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

    //load data from external server
    public void refreshPokemonListItems(){
        pokemonList = new ArrayList<>();
        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlJsonPokemonList, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    JSONArray results = response.getJSONArray("results");
                    for(int x = 0; x < results.length(); x++){
                        JSONObject pokemon = (JSONObject) results.get(x);

                        String id = pokemon.getString("name");
                        String name = pokemon.getString("name");

                        PokemonListItem pli = new PokemonListItem();
                        pli.setId(id);
                        pli.setName(name);

                        pokemonList.add(pli);
                    }

                    // get data from the table by the ListAdapter
                    adapter = new PokemonListAdapter(context, R.id.searchInput, pokemonList);
                    yourListView.setAdapter(adapter);

                    yourListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            PokemonListItem item = (PokemonListItem)parent.getItemAtPosition(position);
                            updateDetail(item.getId());
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                hidepDialog();
            }
        });

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    // triggers update of the details fragment
    public void updateDetail(String id) {
        // create fake data
        String pokemonId = id;
        // send data to activity
        listener.onPokemonSelected(pokemonId);
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
