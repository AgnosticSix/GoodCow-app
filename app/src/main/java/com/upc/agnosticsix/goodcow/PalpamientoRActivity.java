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

import adapters.PalpamientoAdapter;
import model.Palpamientos;

import static model.DataHelper.HOST_URL;

public class PalpamientoRActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private String TAG = PalpamientoRActivity.class.getSimpleName();
    private AppCompatActivity activity = PalpamientoRActivity.this;
    private RecyclerView recyclerViewpal;
    private List<Palpamientos> palList;
    private PalpamientoAdapter palpamientoAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    private android.support.v7.widget.SearchView searchView;
    private ProgressDialog progressDialog;
    private static String url = HOST_URL + "palpamientos";
    private String idbovino, postid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palpamiento_r);

        initViews();
        initObjects();

        new GetData().execute();
    }

    private void initViews(){
        recyclerViewpal = (RecyclerView) findViewById(R.id.recyclerViewPal);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_containerPal);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
    }

    private void initObjects(){
        idbovino = getIntent().getStringExtra("idbovinopal");
        palList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewpal.setLayoutManager(layoutManager);
        recyclerViewpal.setItemAnimator(new DefaultItemAnimator());
        recyclerViewpal.setHasFixedSize(true);
        palpamientoAdapter = new PalpamientoAdapter(activity, palList, new CustomItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                postid = palList.get(i).getId();
                Intent intent = new Intent(activity, PalpamientoDetalleActivity.class);
                intent.putExtra("idbovinopal2", postid);
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
                palpamientoAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                palpamientoAdapter.getFilter().filter(newText);
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
            progressDialog = new ProgressDialog(PalpamientoRActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url.concat("?where=bovino_id:"+idbovino));

            if(jsonStr != null){
                try{
                    JSONArray data = new JSONArray(jsonStr);

                    for(int i = 0; i < data.length(); i++){
                        JSONObject c = data.getJSONObject(i);

                        Palpamientos palpamientos = new Palpamientos(c.getString("palpamiento_id"),
                                c.getString("bovino_id"));

                        palList.add(palpamientos);
                    }


                } catch (final JSONException e){
                    Log.e(TAG,"Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            idbovino = getIntent().getStringExtra("idbovino");
                            Toast.makeText(getApplicationContext(),
                                    "No hay palpamientos",
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

            recyclerViewpal.setAdapter(palpamientoAdapter);
        }
    }

    @Override
    public void onRefresh() {
        palpamientoAdapter.update(palList);
        new GetData().execute();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
