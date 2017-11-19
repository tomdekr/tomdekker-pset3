package com.example.tom_d.restaurantapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class Appetizer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appetizer);
        Intent intent = getIntent();
        final String theCategory = intent.getStringExtra("category");
        final TextView mTextView = findViewById(R.id.mTextView);
        final ArrayList<String> listdata = new ArrayList<String>();
        Button b = (Button) findViewById(R.id.button);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://resto.mprog.nl/menu";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    private JSONObject object = null;
                    private JSONArray categoriesArray = null;
                    private JSONObject object2 = null;
                    private JSONArray categoriesArray2 = null;

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            object = (JSONObject) new JSONObject(response.toString());
                            categoriesArray = object.getJSONArray("items");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ArrayList<String> listdata = new ArrayList<String>();
                        for (int i = 0; i < categoriesArray.length(); i++) {
                            try {
                                if (categoriesArray.getJSONObject(i).optString("category").equals(theCategory)){
                                    listdata.add(categoriesArray.getJSONObject(i).getString("name"));
                                }
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }

                        try {
                            object2 = (JSONObject) new JSONObject(response.toString());
                            categoriesArray2 = object2.getJSONArray("items");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ArrayList<String> listdata2 = new ArrayList<String>();
                        for (int i = 0; i < categoriesArray2.length(); i++) {
                            try {
                                if (categoriesArray2.getJSONObject(i).optString("category").equals(theCategory)){
                                    listdata2.add(categoriesArray2.getJSONObject(i).getString("price"));
                                }
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

                        ArrayAdapter<String> adapter2 =
                                new ArrayAdapter<String>(
                                        getApplicationContext(),
                                        R.layout.row_layout,
                                        listdata2
                                );
                        ListView mListView2 = findViewById(R.id.mListView2);

                        mListView2.setAdapter(adapter2);


                        try {
                            mListView.setOnItemClickListener(

                                    new AdapterView.OnItemClickListener() {

                                        SharedPreferences settings = Appetizer.this.getSharedPreferences("order", MODE_PRIVATE);
                                        String item = settings.getString("order", "[]");

                                        JSONArray order = new JSONArray(item);


                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                            String categoryPicked = "You added " +
                                                    String.valueOf(adapterView.getItemAtPosition(position)) + "to your order";
                                            Toast.makeText(Appetizer.this, categoryPicked, Toast.LENGTH_SHORT).show();

                                            SharedPreferences settings = Appetizer.this.getSharedPreferences("order", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = settings.edit();

                                            order.put(String.valueOf(adapterView.getItemAtPosition(position)));
                                            editor.putString("order", String.valueOf(order));
                                            editor.commit();

                                        }

                                    });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }




                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("That does not work"+error);
                error.printStackTrace();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);

    }
    public void button1(View view) {
        Intent intent = new Intent(this, ShoppingOrder.class);
        startActivity(intent);

    }



}