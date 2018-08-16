package com.upc.agnosticsix.goodcow;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import adapters.BovinoAdapter;
import adapters.EmpleadoAdapter;
import adapters.ResPalpaAdapter;
import model.Cow;
import model.DataHelper;
import model.Empleados;
import model.ResultadosPalpamientos;

import static model.DataHelper.HOST_URL;

public class PalpamientoActivity extends AppCompatActivity {

    private String TAG = PalpamientoActivity.class.getSimpleName();
    private Spinner bovinospin, empspin, resspin;
    private Button agregarbtn;
    private int resultado, idbovino, empleado, responseCode;
    private ProgressDialog progressDialog;
    private static String url = HOST_URL + "bovinos?where=sexo:2";
    private static String urla = HOST_URL + "palpamientos";
    private String currentTime, response;
    private TextView observa, fecha;
    private DataHelper dataHelper;
    List<Cow> bovinoHList;
    List<Empleados> empleadosList;
    List<ResultadosPalpamientos> resultadosPalpamientosList;
    BovinoAdapter bovinoAdapter;
    EmpleadoAdapter empleadoAdapter;
    ResPalpaAdapter resPalpaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palpamiento);
        initObjects();
        initViews();

        new GetData().execute();
    }

    private void initViews(){
        fecha = (TextView) findViewById(R.id.fechaPal);
        observa = (TextView) findViewById(R.id.observaPal);
        agregarbtn = (Button) findViewById(R.id.addPalBtn);
    }

    private void initObjects(){
        dataHelper = new DataHelper();
        bovinoHList = new ArrayList<>();
        empleadosList = new ArrayList<>();
        resultadosPalpamientosList = new ArrayList<>();
        Date date = new Date();
        DateFormat HDFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        currentTime = HDFormat.format(date);
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
                    empleadosList = DataHelper.getEmpleados();
                    resultadosPalpamientosList = DataHelper.getResPalpamientos();

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

            fecha.setText(currentTime);
            bovinospin = (Spinner) findViewById(R.id.bovinoPal);
            empspin = (Spinner) findViewById(R.id.empleadoPal);
            resspin = (Spinner) findViewById(R.id.resultadoPalSpin);
            bovinoAdapter = new BovinoAdapter(PalpamientoActivity.this, R.layout.custom_spinner_items, bovinoHList);
            empleadoAdapter = new EmpleadoAdapter(PalpamientoActivity.this, R.layout.custom_spinner_items, empleadosList);
            resPalpaAdapter = new ResPalpaAdapter(PalpamientoActivity.this, R.layout.custom_spinner_items, resultadosPalpamientosList);
            bovinospin.setAdapter(bovinoAdapter);
            empspin.setAdapter(empleadoAdapter);
            resspin.setAdapter(resPalpaAdapter);
            bovinospin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    idbovino = Integer.parseInt(bovinoHList.get(position).getId());
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
            resspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    resultado = Integer.parseInt(resultadosPalpamientosList.get(position).getId());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            agregarbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new uploadData().execute();
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
                c.put("resultado_palpamiento_id", resultado);
                c.put("empleado_id", empleado);
                c.put("fecha", currentTime);
                c.put("observaciones", observa.getText());

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
        bovinoHList.clear();
        resultadosPalpamientosList.clear();
        empleadosList.clear();
    }
}
