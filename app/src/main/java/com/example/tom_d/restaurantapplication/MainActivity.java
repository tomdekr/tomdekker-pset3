package com.example.tom_d.restaurantapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView mTextView = findViewById(R.id.mTextView);
        final ArrayList<String> listdata = new ArrayList<String>();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://resto.mprog.nl/categories";

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    private JSONObject object = null;
                    private JSONArray categoriesArray = null;

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            object = (JSONObject) new JSONObject(response.toString());
                            categoriesArray = object.getJSONArray("categories");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ArrayList<String> listdata = new ArrayList<String>();
                        for (int i = 0; i < categoriesArray.length(); i++) {
                            try {
                                listdata.add(categoriesArray.getString(i));
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }


                        ArrayAdapter<String> adapter =
                                new ArrayAdapter<String>(
                                        getApplicationContext(),
                                        R.layout.row_layout,
                                        listdata
                                );
                        ListView mListView = findViewById(R.id.mListView);

                        mListView.setAdapter(adapter);

                        mListView.setOnItemClickListener(
                                new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                        String categoryPicked = "You selected " +
                                                String.valueOf(adapterView.getItemAtPosition(position));
                                        Toast.makeText(MainActivity.this, categoryPicked, Toast.LENGTH_SHORT).show();

                                        Intent intent = new  Intent(view.getContext(), Appetizer.class);
                                        intent.putExtra("category", String.valueOf(adapterView.getItemAtPosition(position)));
                                        startActivity(intent);
                                    }
                                });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work!");
            }
        });
// Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

}