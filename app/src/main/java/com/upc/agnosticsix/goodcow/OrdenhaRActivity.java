package com.upc.agnosticsix.goodcow;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapters.OrdeñaAdapter;
import model.Ordenhas;

import static model.DataHelper.HOST_URL;

public class OrdenhaRActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private String TAG = OrdenhaRActivity.class.getSimpleName();
    private AppCompatActivity activity = OrdenhaRActivity.this;
    private RecyclerView recyclerViewOrde;
    private List<Ordenhas> ordeList;
    private OrdeñaAdapter ordeñaAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    private android.support.v7.widget.SearchView searchView;
    private ProgressDialog progressDialog;
    private static String url = HOST_URL + "ordenhas";
    private String idbovino, postid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordenha_r);

        initViews();
        initObjects();

        new GetData().execute();
    }

    private void initViews(){
        recyclerViewOrde = (RecyclerView) findViewById(R.id.recyclerViewOrde);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_containerOrde);
        swipeRefreshLayout.setOnRefreshListener(OrdenhaRActivity.this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
    }

    private void initObjects(){
        idbovino = getIntent().getStringExtra("idbovinoorde");
        ordeList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewOrde.setLayoutManager(layoutManager);
        recyclerViewOrde.setItemAnimator(new DefaultItemAnimator());
        recyclerViewOrde.setHasFixedSize(true);
        ordeñaAdapter = new OrdeñaAdapter(activity, ordeList, new CustomItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                postid = ordeList.get(i).getId();
                Intent intent = new Intent(activity, OrdenhaDetalleActivity.class);
                intent.putExtra("idbovinoorde2", postid);
                startActivity(intent);
            }
        });
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
                ordeñaAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ordeñaAdapter.getFilter().filter(newText);
                return false;
            }
        });

        if (!searchView.isIconified()) {
            searchView.setIconified(true);
        }
        return true;
    }

    private class GetData extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(OrdenhaRActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String url2 = url.concat("?where=bovino_id:"+idbovino);
            String jsonStr = sh.makeServiceCall(url2);

            if(jsonStr != null){
                try{
                    JSONArray data = new JSONArray(jsonStr);

                    for(int i = 0; i < data.length(); i++){
                        JSONObject c = data.getJSONObject(i);

                        Ordenhas ordenhas = new Ordenhas(c.getString("ordenha_id"),
                                c.getString("bovino_id"));

                        ordeList.add(ordenhas);
                    }


                } catch (final JSONException e){
                    Log.e(TAG,"Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            idbovino = getIntent().getStringExtra("idbovino");
                            Toast.makeText(getApplicationContext(),
                                    "No hay ordeñas",
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

            recyclerViewOrde.setAdapter(ordeñaAdapter);
        }
    }

    @Override
    public void onRefresh() {
        ordeñaAdapter.update(ordeList);
        new GetData().execute();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
