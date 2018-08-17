package com.upc.agnosticsix.goodcow;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import model.DataHelper;
import model.Zoometricas;

import static model.DataHelper.HOST_URL;

public class ZoometricaDetalleActivity extends AppCompatActivity {

    private String TAG = ZoometricaDetalleActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    private List<Zoometricas> zoometricasList;
    private DataHelper dataHelper;
    private TextView bovino, altura, fecha, peso, testiculos;
    private String bovinos, alturas, fechas, pesos, testiculoss, idintent, response, currentTime, idbovino2;
    private int responseCode;
    private Button agregarBtn;
    private static String urla = HOST_URL + "zoometricas_bovinos";
    private static String urle = HOST_URL + "bovinos";
    private int idbovino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoometrica_detalle);
        initViews();
        initObjects();
        new GetData().execute();
    }

    private void initViews(){
        bovino = (TextView) findViewById(R.id.bovinoZooText);
        altura = (TextView) findViewById(R.id.alturaZooText);
        fecha = (TextView) findViewById(R.id.fechaZooText);
        peso = (TextView) findViewById(R.id.pesoZooText);
        testiculos = (TextView) findViewById(R.id.testiZooText);
        agregarBtn = (Button) findViewById(R.id.updateZooBtn);
    }

    private void initObjects(){
        dataHelper = new DataHelper();
        idintent = getIntent().getStringExtra("idzoometrica");
        idbovino2 = getIntent().getStringExtra("idbovinozoo3");
        Log.i(TAG, ""+idbovino2);
        if(idintent == "0" || idintent == null){
            agregarBtn.setText("Agregar");
        }else{
            agregarBtn.setText("Actualizar");
        }

        agregarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(agregarBtn.getText().equals("Agregar")){
                    new uploadData().execute();
                    finish();
                }else if(agregarBtn.getText().equals("Actualizar")){
                    new updateData().execute();
                    finish();
                }
            }
        });

        Date date = new Date();
        DateFormat HDFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        currentTime = HDFormat.format(date);

    }

    private class GetData extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ZoometricaDetalleActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(idintent == "" || idintent == null || idintent.isEmpty()){
                HttpHandler sh = new HttpHandler();
                String url2 = urle.concat("/"+idbovino2);

                String jsonStr = sh.makeServiceCall(url2);

                if(jsonStr != null){
                    try{
                        JSONArray data = new JSONArray(jsonStr);

                        for(int i = 0; i < data.length(); i++) {

                            JSONObject c = data.getJSONObject(i);

                            idbovino = Integer.parseInt(c.getString("bovino_id"));
                            bovinos = c.getString("nombre");

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
                    Log.e(TAG, "" + jsonStr);
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

            }else{
                HttpHandler sh = new HttpHandler();
                String url2 = urla.concat("/"+idintent);

                String jsonStr = sh.makeServiceCall(url2);

                if(jsonStr != null){
                    try{
                        JSONArray data = new JSONArray(jsonStr);

                        for(int i = 0; i < data.length(); i++) {

                            JSONObject c = data.getJSONObject(i);

                            //idbovino = Integer.parseInt(c.getString("bovino_id"));
                            bovinos = dataHelper.getCow(c.getString("bovino_id"));
                            alturas = c.getString("altura");
                            fechas = c.getString("fecha");
                            pesos = c.getString("peso");
                            testiculoss = c.getString("testiculos");
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
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(progressDialog.isShowing())
                progressDialog.dismiss();

            if(idintent == "" || idintent == null){
                bovino.setText(bovinos);
                fecha.setText(currentTime);
            }else{
                bovino.setText(bovinos);
                altura.setText(alturas);
                peso.setText(pesos);
                fecha.setText(fechas);
                testiculos.setText(testiculoss);
            }

        }
    }

    private class uploadData extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                //String url2 = urla.substring(0, urla.length()-1);
                URL url = new URL(urla);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");

                JSONObject c = new JSONObject();

                c.put("bovino_id", idbovino = Integer.parseInt(idbovino2));
                c.put("altura", altura.getText().toString());
                c.put("fecha", fecha.getText().toString());
                c.put("peso", peso.getText().toString());
                c.put("testiculos", testiculos.getText().toString());

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
                    String line = "";

                    while((line = br.readLine()) != null) {
                        response += line;
                        break;
                    }
                    br.close();
                }else if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR){

                }
                conn.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
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
            }else if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR){
                Toast.makeText(getApplicationContext(), "Error: "+ responseCode,Toast.LENGTH_LONG).show();
                response = "";
            }
        }
    }

    private class updateData extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                String url2 = urla.concat("/"+idintent);
                URL url = new URL(url2);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("PUT");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");

                JSONObject c = new JSONObject();

                c.put("bovino_id", idbovino = Integer.parseInt(idbovino2));
                c.put("altura", alturas);
                c.put("peso", pesos);
                c.put("fecha", fechas);
                c.put("testiculos", testiculoss);

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

                if(responseCode == HttpURLConnection.HTTP_OK){
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line = "";

                    while((line = br.readLine()) != null) {
                        response += line;
                        break;
                    }
                    br.close();
                }else if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR){

                }
                conn.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(responseCode == HttpURLConnection.HTTP_OK){
                Toast.makeText(getApplicationContext(), "Datos insertados: "+ responseCode,Toast.LENGTH_LONG).show();
                response = "";
            }else if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR){
                Toast.makeText(getApplicationContext(), "Error: "+ responseCode,Toast.LENGTH_LONG).show();
                response = "";
            }
        }
    }
}
