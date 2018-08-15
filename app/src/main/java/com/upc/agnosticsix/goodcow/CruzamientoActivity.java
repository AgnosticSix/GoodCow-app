package com.upc.agnosticsix.goodcow;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import adapters.EstadoAdapter;
import model.Cow;
import model.DataHelper;
import model.Empleados;
import model.Estados;

public class CruzamientoActivity extends AppCompatActivity {

    private String TAG = CruzamientoActivity.class.getSimpleName();
    private static String url = "http://goodcow-api-goodcow.7e14.starter-us-west-2.openshiftapps.com/bovinos?where=clase_bovino_id:6";
    private static String url2 = "http://goodcow-api-goodcow.7e14.starter-us-west-2.openshiftapps.com/bovinos?where=sexo:2";
    private static String urla = "http://goodcow-api-goodcow.7e14.starter-us-west-2.openshiftapps.com/cruzamientos";
    private Spinner sementalSpin, empleadoSpin, vacaSpin;
    private Switch estadosw;
    private TextView fecha;
    private EditText descripcion;
    private Button agregarBtn;
    private ProgressDialog progressDialog;
    List<Cow> sementalList, vacaList;
    List<Empleados> empleadoList;
    List<Estados> estadoList;
    EmpleadoAdapter empleadoAdapter;
    BovinoAdapter sementalAdapter, vacaAdapter;
    private String currentTime, response;
    private int responseCode, idsemental, idvaca, estado = 0, empleado;
    private DataHelper dataHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cruzamiento);
        initViews();
        initObjects();
        new GetData().execute();
    }

    private void initViews(){
        fecha = (TextView) findViewById(R.id.fechaCruza);
        descripcion = (EditText) findViewById(R.id.descripCruza);
        agregarBtn = (Button) findViewById(R.id.addCruzaBtn);
        estadosw = findViewById(R.id.estadoCruzaSw);
    }

    private void initObjects(){
        dataHelper = new DataHelper();
        sementalList = new ArrayList<>();
        empleadoList = new ArrayList<>();
        estadoList = new ArrayList<>();
        vacaList = new ArrayList<>();
        agregarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new uploadData().execute();
            }
        });

        estadosw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    estado = 1;
                }else{
                    estado = 0;
                }
            }
        });
        Date date = new Date();
        DateFormat HDFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        currentTime = HDFormat.format(date);

    }

    private class GetData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog = new ProgressDialog(CruzamientoActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(url);
            String jsonStr2 = sh.makeServiceCall(url2);

            if(jsonStr != null){
                try{
                    JSONArray data = new JSONArray(jsonStr);
                    JSONArray data2 = new JSONArray(jsonStr2);

                    for(int i = 0; i < data.length(); i++) {

                        JSONObject c = data.getJSONObject(i);
                        Cow cow = new Cow(c.getString("bovino_id"),
                                c.getString("fierro"),
                                c.getString("nombre"));

                        sementalList.add(cow);
                    }

                    for(int i = 0; i < data2.length(); i++) {

                        JSONObject c = data2.getJSONObject(i);
                        Cow cow = new Cow(c.getString("bovino_id"),
                                c.getString("fierro"),
                                c.getString("nombre"));

                        vacaList.add(cow);
                    }

                    empleadoList = dataHelper.getEmpleados();
                    estadoList = dataHelper.getEstados();

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
            sementalSpin = (Spinner) findViewById(R.id.sementalSpin);
            empleadoSpin = (Spinner) findViewById(R.id.empSpinCruza);
            vacaSpin = (Spinner) findViewById(R.id.vacaSpin);
            sementalAdapter = new BovinoAdapter(CruzamientoActivity.this, R.layout.custom_spinner_items, sementalList);
            empleadoAdapter = new EmpleadoAdapter(CruzamientoActivity.this, R.layout.custom_spinner_items, empleadoList);
            vacaAdapter = new BovinoAdapter(CruzamientoActivity.this, R.layout.custom_spinner_items, vacaList);
            sementalSpin.setAdapter(sementalAdapter);
            empleadoSpin.setAdapter(empleadoAdapter);
            vacaSpin.setAdapter(vacaAdapter);
            sementalSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    idsemental = Integer.parseInt(sementalList.get(position).getId());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            vacaSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    idvaca = Integer.parseInt(vacaList.get(position).getId());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            empleadoSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    empleado = Integer.parseInt(empleadoList.get(position).getId());
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

                c.put("vaca_id", idvaca);
                c.put("semental_id", idsemental);
                c.put("empleado_id", empleado);
                c.put("fecha", currentTime);
                c.put("estado", estado);
                c.put("descripcion", descripcion.getText());

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
            }else if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR){
                Toast.makeText(getApplicationContext(), "Error: "+ responseCode,Toast.LENGTH_LONG).show();
                response = "";
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        vacaList.clear();
        estadoList.clear();
        empleadoList.clear();
    }
}
