package nl.seventho.androidassessment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class DetailFragment extends Fragment {
    // json object response url
    private String pokemonURL = "http://pokeapi.co/api/v2/pokemon/";

    private static String TAG;
    private Button sentPokemonDataButton;
    private TextView pokemonDetail;
    private NetworkImageView pokemonSprite;
    private BitmapLruCache bitmapCache;
    private ImageLoader imageLoader;

    // Progress dialog
    private ProgressDialog pDialog;

    //what to sent?
    private Settings settings;
    private Pokemon detailPokemon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pokemon_detail,
                container, false);

        sentPokemonDataButton = (Button) view.findViewById(R.id.sentPokemonData);

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Obtaining pokemon information...");
        pDialog.setCancelable(false);
        TAG = PokemonViewer.class.getSimpleName();

        pokemonDetail = (TextView) view.findViewById(R.id.webLoader);

        //preparing sprite
        bitmapCache = new BitmapLruCache();
        imageLoader = new ImageLoader(Volley.newRequestQueue(getContext()),bitmapCache);
        pokemonSprite = (NetworkImageView) view.findViewById(R.id.pokemonSprite);

        settings = new Settings(PreferenceManager.getDefaultSharedPreferences(getContext()));
        detailPokemon = new Pokemon();

        sentPokemonDataButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // making json object request
                PackageManager pm= getContext().getPackageManager();
                try {

                    Intent waIntent = new Intent(Intent.ACTION_SEND);
                    waIntent.setType("text/plain");

                    PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                    //Check if package exists or not. If not then code
                    //in catch block will be called
                    waIntent.setPackage("com.whatsapp");

                    waIntent.putExtra(Intent.EXTRA_TEXT, generateStringToSent());
                    startActivity(Intent.createChooser(waIntent, "Pokemon delen"));

                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(getContext(), "WhatsApp not Installed", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Method to make json object request where json response starts wtih {
     * */
    public void setText(String pokemonId) {
        pokemonDetail.setText(pokemonId);
        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                pokemonURL + pokemonId, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    // add pokemon id
                    String id = response.getString("id");
                    detailPokemon.setId(id);

                    // add pokemon name
                    String name = response.getString("name");
                    detailPokemon.setName(name);

                    // add pokemon height
                    String height = response.getString("height");
                    detailPokemon.setHeight(height);

                    // add pokemon experience
                    String experience = response.getString("base_experience");
                    detailPokemon.setBaseExp(experience);

                    // show data
                    pokemonDetail.setText(generateStringToShow());

                    //show sprite
                    JSONObject sprites = response.getJSONObject("sprites");
                    String spriteURL = sprites.getString("front_default");
                    pokemonSprite.setImageUrl(spriteURL, imageLoader);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(),
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

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public String generateStringToSent(){
        StringBuilder sb = new StringBuilder();
        sb.append("A " + settings.getSender() +  " wishes to expand your pokemon knowledge.\n");

        if(settings.isIdEnabled()){
            sb.append(generateBulletPointString("Id", detailPokemon.getId()));
        }

        //always add name
        sb.append(generateBulletPointString("Name", detailPokemon.getName()));

        if (settings.isHeightEnabled()) {
            sb.append(generateBulletPointString("Height", detailPokemon.getHeight()));
        }

        if (settings.isBase_expEnabled()){
            sb.append(generateBulletPointString("Base Exp.:", detailPokemon.getBaseExp()));
        }
        return sb.toString();
    }

    public String generateStringToShow(){
        StringBuilder sb = new StringBuilder();
        sb.append(generateBulletPointString("Id", detailPokemon.getId()));
        sb.append(generateBulletPointString("Name", detailPokemon.getName()));
        sb.append(generateBulletPointString("Height", detailPokemon.getHeight()));
        sb.append(generateBulletPointString("Base Experience", detailPokemon.getBaseExp()));
        return  sb.toString();
    }

    public String generateBulletPointString(String key, String value){
        return "- " + key + ": " + value + "\n";

    }
}
