package com.upc.agnosticsix.goodcow;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import adapters.BovinoAdapter;
import model.Cow;
import model.DataHelper;

public class PalpamientoActivity extends AppCompatActivity {

    private String TAG = PalpamientoActivity.class.getSimpleName();
    private Spinner bovinospin;
    private Switch palSw;
    private int resultado;
    private ProgressDialog progressDialog;
    private static String url = "http://goodcow-api-goodcow.7e14.starter-us-west-2.openshiftapps.com/bovinos?where=sexo:2";
    private String fechas;
    private TextView observa, fecha;
    private DataHelper dataHelper;
    List<Cow> bovinoHList;
    BovinoAdapter bovinoAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palpamiento);
        initObjects();
        initViews();

        new GetData().execute();
    }

    private void initViews(){
        bovinospin = (Spinner) findViewById(R.id.bovinoPal);
        palSw = (Switch) findViewById(R.id.swPal);
        palSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    resultado = 1;
                }else{
                    resultado = 2;
                }
            }
        });
        fecha = (TextView) findViewById(R.id.fechaPal);
        observa = (TextView) findViewById(R.id.observaPal);

    }

    private void initObjects(){
        dataHelper = new DataHelper();
        bovinoHList = new ArrayList<>();
    }

    private class GetData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog = new ProgressDialog(PalpamientoActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(url);
            String currentTime = Calendar.getInstance().getTime().toString();

            if(jsonStr != null){
                try{
                    JSONArray data = new JSONArray(jsonStr);

                    for(int i = 0; i < data.length(); i++) {

                        JSONObject c = data.getJSONObject(i);
                        Cow cow = new Cow(c.getString("bovino_id"),
                                c.getString("fierro"),
                                c.getString("nombre"));

                        bovinoHList.add(cow);
                    }

                    fechas = currentTime;


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

            bovinoAdapter = new BovinoAdapter(PalpamientoActivity.this, R.layout.custom_spinner_items, bovinoHList);
            bovinospin.setAdapter(bovinoAdapter);
            fecha.setText(fechas);


        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    //TODO: uploadMethod
}
