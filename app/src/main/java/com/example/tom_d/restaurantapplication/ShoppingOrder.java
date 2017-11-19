package com.example.tom_d.restaurantapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class ShoppingOrder extends AppCompatActivity {

    private JSONArray jsonArray;
    private JSONArray jsonResponse;
    private ArrayList<String> totalList;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_order);
        final ListView mListView = (ListView) findViewById(R.id.mListView);
        SharedPreferences settings = this.getSharedPreferences("order", MODE_PRIVATE);
        String item = settings.getString("order", "");
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://resto.mprog.nl/menu";

        totalList = new ArrayList<>();
        final ArrayList<String> list = new ArrayList<String>();
        try {
            JSONArray order = new JSONArray(item);
            jsonArray = order;

            for (int i=0; i<order.length(); i++) {
                try {
                    list.add(order.getString(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(

                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    private JSONObject object = null;

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            object = new JSONObject(response.toString());
                            jsonResponse = object.getJSONArray("items");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for (int x = 0; x < jsonArray.length(); x++) {
                            for (int i = 0; i < jsonResponse.length(); i++) {
                                try {
                                    if (jsonResponse.getJSONObject(i).getString("name").equals(jsonArray.getString(x))) {
                                        JSONObject itemOrder = jsonResponse.getJSONObject(i);
                                        String itemPrice = jsonResponse.getJSONObject(i).optString("price");
                                        String finalOrder = itemOrder.optString("name") +" "+"$"+ itemPrice;
                                        totalList.add(finalOrder);

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        ArrayAdapter<String> adapter =
                                new ArrayAdapter<String>(
                                        getApplicationContext(),
                                        R.layout.row_layout,
                                        totalList
                                );
                        mListView.setAdapter(adapter);
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);

        mListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(final AdapterView<?> adapterView, View view, final int i, long l) {

                        final AlertDialog.Builder builder = new AlertDialog.Builder(adapterView.getContext(), android.R.style.Theme_Material_Dialog_Alert);

                        builder.setTitle("Delete");
                        builder.setMessage("Are you sure you want to delete?");
                        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {

                                SharedPreferences prefs2 = getSharedPreferences("order", MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs2.edit();

                                jsonArray.remove(i);

                                editor.putString("order", String.valueOf(jsonArray));
                                editor.commit();
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            }
                        });
                        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                            }
                        });
                        builder.setIcon(android.R.drawable.ic_dialog_alert);
                        builder.show();

                    }
                }
        );

    }

    public void responseTime(View view) {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://resto.mprog.nl/order";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        String toast = response.toString();

                        Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });
        queue.add(jsObjRequest);

    }
}