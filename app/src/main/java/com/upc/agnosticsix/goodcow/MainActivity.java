package com.upc.agnosticsix.goodcow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapters.CowRecyclerAdapter;
import model.Cow;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    private String TAG = MainActivity.class.getSimpleName();
    private AppCompatActivity activity = MainActivity.this;
    private RecyclerView recyclerViewCow;
    private List<Cow> cowList;
    private CowRecyclerAdapter cowRecyclerAdapter;
    private Cow cow;
    SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog progressDialog;
    private static String url = "http://goodcow-api-goodcow.7e14.starter-us-west-2.openshiftapps.com/bovinos";
    private String postId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initObjects();

        new GetData().execute();
    }

    private void initViews(){
        recyclerViewCow = (RecyclerView) findViewById(R.id.recyclerViewCows);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
    }

    private void initObjects(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, BovinoActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView emailtext = (TextView) findViewById(R.id.emailview);

        cowList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewCow.setLayoutManager(layoutManager);
        recyclerViewCow.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCow.setHasFixedSize(true);
        cowRecyclerAdapter = new CowRecyclerAdapter(getApplicationContext(), cowList, new CustomItemClickListener(){
            @Override
            public void onItemClick(View view, int i) {
                postId = cowList.get(i).getId();
                Intent intent = new Intent(activity, DetallesActivity.class);
                intent.putExtra("idbovino", postId);
                startActivity(intent);
                //finish();
            }
        });
    }

    @Override
    public void onRefresh(){
        new GetData().execute();
        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {
            Intent intent = new Intent(activity, OrdenhaActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(activity, PalpamientoActivity.class);
            startActivity(intent);

        }else if (id == R.id.nav_slideshow2) {
            Intent intent = new Intent(activity, CruzamientoActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(activity, AcercaDeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_camera){
            Intent intent = new Intent(activity, DecesosActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class GetData extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(url);

            if(jsonStr != null){
                try{
                    JSONArray data = new JSONArray(jsonStr);

                    for(int i = 0; i < data.length(); i++){
                        JSONObject c = data.getJSONObject(i);

                        Cow cowData = new Cow(c.getString("bovino_id"),
                                c.getString("fierro"),
                                c.getString("nombre"));
                        //String id = c.getString("bovino_id");
                        //String matricula = c.getString("fierro");
                        //String nombre = c.getString("nombre");

                        cowList.add(cowData);
                    }


                } catch (final JSONException e){
                    Log.e(TAG,"Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error" + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } else {
                Log.e(TAG, jsonStr);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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
        protected void onPostExecute(Void result){
            super.onPostExecute(result);

            if(progressDialog.isShowing())
                progressDialog.dismiss();

            recyclerViewCow.setAdapter(cowRecyclerAdapter);
        }
    }
}
