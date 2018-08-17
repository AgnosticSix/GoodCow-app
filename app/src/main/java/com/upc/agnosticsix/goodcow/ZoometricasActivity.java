package com.upc.agnosticsix.goodcow;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import adapters.ZoometricaAdapter;
import model.DataHelper;
import model.Zoometricas;

import static model.DataHelper.HOST_URL;

public class ZoometricasActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private String TAG = ZoometricasActivity.class.getSimpleName();
    private AppCompatActivity activity = ZoometricasActivity.this;
    private RecyclerView recyclerViewZoo;
    private List<Zoometricas> zooList;
    private ZoometricaAdapter zoometricaAdapter;
    private Zoometricas zoo;
    SwipeRefreshLayout swipeRefreshLayout;
    private android.support.v7.widget.SearchView searchView;
    private ProgressDialog progressDialog;
    private static String url = HOST_URL + "zoometricas_bovinos";
    private String postId;
    private String idbovino;
    private String sinid = "-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoometricas);

        initViews();
        initObjects();

        new GetData().execute();
    }

    private void initViews(){
        recyclerViewZoo = (RecyclerView) findViewById(R.id.recyclerViewZoo);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container2);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

    }

    private void initObjects(){
        idbovino = getIntent().getStringExtra("idbovinozoo");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab4);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ZoometricaDetalleActivity.class);
                //intent.putExtra("idzoometrica", sinid);
                intent.putExtra("idbovinozoo2", idbovino);
                startActivity(intent);
            }
        });
        zooList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewZoo.setLayoutManager(layoutManager);
        recyclerViewZoo.setItemAnimator(new DefaultItemAnimator());
        recyclerViewZoo.setHasFixedSize(true);
        zoometricaAdapter = new ZoometricaAdapter(getApplicationContext(), zooList, new CustomItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                postId = zooList.get(i).getId();
                Intent intent = new Intent(activity, ZoometricaDetalleActivity.class);
                intent.putExtra("idzoometrica", postId);
                intent.putExtra("idbovinozoo3", idbovino);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onRefresh(){
        zoometricaAdapter.update(zooList);
        new GetData().execute();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (android.support.v7.widget.SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                zoometricaAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                zoometricaAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    private class GetData extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ZoometricasActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String urla = url.concat("?where=bovino_id:" + idbovino);
            String jsonStr = sh.makeServiceCall(urla);

            if(jsonStr != null){
                try{
                    JSONArray data = new JSONArray(jsonStr);

                    for(int i = 0; i < data.length(); i++){
                        JSONObject c = data.getJSONObject(i);

                        Zoometricas zoometricas = new Zoometricas(c.getString("zoometricas_bovino_id"),
                                c.getString("bovino_id"));

                        zooList.add(zoometricas);
                    }


                } catch (final JSONException e){
                    Log.e(TAG,"Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            idbovino = getIntent().getStringExtra("idbovino");
                            Toast.makeText(getApplicationContext(),
                                    "No hay Zoometricas",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                    idbovino = getIntent().getStringExtra("idbovino");
                }
            } else {
                Log.e(TAG, ""+jsonStr);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        idbovino = getIntent().getStringExtra("idbovino");
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(progressDialog.isShowing())
                progressDialog.dismiss();

            recyclerViewZoo.setAdapter(zoometricaAdapter);
        }
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }

        finish();
        super.onBackPressed();
    }
}
