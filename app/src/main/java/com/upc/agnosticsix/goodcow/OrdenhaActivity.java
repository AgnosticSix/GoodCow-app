package com.upc.agnosticsix.goodcow;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import adapters.BovinoAdapter;
import adapters.EmpleadoAdapter;
import model.Cow;
import model.DataHelper;
import model.Empleados;

import static model.DataHelper.HOST_URL;

public class OrdenhaActivity extends AppCompatActivity {

    private String TAG = OrdenhaActivity.class.getSimpleName();
    private Spinner bovispin, empspin;
    private TextView fecha;
    private EditText cantidad, observa;
    private Button agregarbtn;
    private ProgressDialog progressDialog;
    private DataHelper dataHelper;
    private static String url = HOST_URL + "bovinos?where=clase_bovino_id:1";
    private static String urla = HOST_URL + "ordenhas";
    private String currentTime, response;
    private int empleado, responseCode, idbovino;
    private float cantidad2;
    List<Cow> cowList;
    List<Empleados> empleadosList;
    BovinoAdapter bovinoAdapter;
    EmpleadoAdapter empleadoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordenha);
        initViews();
        initObjects();
        new GetData().execute();
    }

    private void initViews(){
        fecha = (TextView) findViewById(R.id.fechaOrdeTxt);
        cantidad = (EditText) findViewById(R.id.cantiOrdeTxt);
        observa = (EditText) findViewById(R.id.observaOrdeTxt);
        agregarbtn = (Button) findViewById(R.id.addOrdeBtn);
    }

    private void initObjects(){
        dataHelper = new DataHelper();
        cowList = new ArrayList<>();
        empleadosList = new ArrayList<>();
        agregarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new uploadData().execute();
            }
        });
        Date date = new Date();
        DateFormat HDFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        currentTime = HDFormat.format(date);
    }

    private class GetData extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(OrdenhaActivity.this);
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

                    for(int i = 0; i < data.length(); i++) {

                        JSONObject c = data.getJSONObject(i);

                        Cow cow = new Cow(c.getString("bovino_id"),
                                c.getString("fierro"),
                                c.getString("nombre"));

                        cowList.add(cow);
                    }
                    empleadosList = DataHelper.getEmpleados();

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
            }else {
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
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(progressDialog.isShowing())
                progressDialog.dismiss();

            fecha.setText(currentTime);
            bovispin = (Spinner) findViewById(R.id.bovinoOrdeSpin);
            empspin = (Spinner) findViewById(R.id.empOrdeSpin);
            bovinoAdapter = new BovinoAdapter(OrdenhaActivity.this, R.layout.custom_spinner_items, cowList);
            empleadoAdapter = new EmpleadoAdapter(OrdenhaActivity.this, R.layout.custom_spinner_items, empleadosList);
            bovispin.setAdapter(bovinoAdapter);
            empspin.setAdapter(empleadoAdapter);

            bovispin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    idbovino = Integer.parseInt(cowList.get(position).getId());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            empspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    empleado = Integer.parseInt(empleadosList.get(position).getId());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private class uploadData extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            try{
                URL url2 = new URL(urla);
                HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");

                JSONObject c = new JSONObject();

                c.put("bovino_id", idbovino);
                c.put("empleado_id", empleado);
                c.put("fecha", currentTime);
                c.put("observaciones", observa.getText());
                c.put("cantidad", Float.parseFloat(cantidad.getText().toString()));

                String str = c.toString();
                byte[] output = str.getBytes("UTF-8");
                Log.i(TAG, str+"");
                String out = conn.getOutputStream().toString();

                OutputStream os = conn.getOutputStream();
                os.write(output);
                os.flush();
                os.close();

                responseCode = conn.getResponseCode();
                Log.i(TAG, responseCode+"");

                if(responseCode == HttpURLConnection.HTTP_CREATED){
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    //StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while((line = br.readLine()) != null) {
                        response += line;
                        break;
                    }

                    br.close();
                }else if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR){

                }
                conn.disconnect();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(responseCode == HttpURLConnection.HTTP_CREATED){
                Toast.makeText(getApplicationContext(), "Datos insertados: "+ responseCode,Toast.LENGTH_LONG).show();
                response = "";
                cantidad.setText("");
                observa.setText("");
            }else if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR){
                Toast.makeText(getApplicationContext(), "Error: "+ responseCode,Toast.LENGTH_LONG).show();
                response = "";
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        cowList.clear();
        empleadosList.clear();
    }
}
