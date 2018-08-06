package com.upc.agnosticsix.goodcow;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import model.DataHelper;

public class PalpamientoActivity extends AppCompatActivity {

    private String TAG = PalpamientoActivity.class.getSimpleName();
    private Spinner bovinospin;
    private Switch resultado;
    private ProgressDialog progressDialog;
    private static String url = "http://goodcow-api-goodcow.7e14.starter-us-west-2.openshiftapps.com/bovinos?where=sexo:2";
    private String bovinos, fechas;
    private TextView observa, fecha;
    private DataHelper dataHelper;
    ArrayList<String> bovinoHList;
    ArrayAdapter<String> bovinoAdapter;


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
        resultado = (Switch) findViewById(R.id.swPal);
        fecha = (TextView) findViewById(R.id.fechaPal);
        observa = (TextView) findViewById(R.id.observaPal);

    }

    private void initObjects(){
        dataHelper = new DataHelper();
        bovinoHList = new ArrayList<>();
        bovinoAdapter = new ArrayAdapter<String>(PalpamientoActivity.this, R.layout.activity_palpamiento, bovinoHList);
        bovinoAdapter.setDropDownViewResource(R.layout.activity_palpamiento);
        bovinospin.setAdapter(bovinoAdapter);

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
                        bovinos = c.getString("nombre");

                        bovinoHList.add(bovinos);
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


            fecha.setText(fechas);


        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
