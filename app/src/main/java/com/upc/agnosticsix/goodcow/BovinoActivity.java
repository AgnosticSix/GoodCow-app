package com.upc.agnosticsix.goodcow;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import adapters.ClaseAdapter;
import adapters.EmpadreAdapter;
import adapters.EstadoAdapter;
import adapters.RazaAdapter;
import adapters.SiniigaAdapter;
import model.Clases;
import model.DataHelper;
import model.Empadres;
import model.Estados;
import model.Razas;
import model.Siniigas;

import static model.DataHelper.HOST_URL;

public class BovinoActivity extends AppCompatActivity {

    private String TAG = BovinoActivity.class.getSimpleName();
    private Spinner claseSpin, siniigaSpin, razaSpin, empadreSpin, estadoSpin;
    private EditText fierro, nombre;
    private TextView fecha;
    private Switch sexo;
    private Button agregarBtn;
    private ProgressDialog progressDialog;
    private String currentTime, response;
    List<Clases> claseList;
    List<Siniigas> siniigaList;
    List<Razas> razaList;
    List<Empadres> empadreList;
    List<Estados> estadoList;
    ClaseAdapter claseAdapter;
    SiniigaAdapter siniigaAdapter;
    RazaAdapter razaAdapter;
    EmpadreAdapter empadreAdapter;
    EstadoAdapter estadoAdapter;
    private DataHelper dataHelper;
    private int clase, siniiga, raza, empadre, estado, sexos = 1, responseCode;
    private String fierros, nombres, postId= "0", fechas;
    private static String urla = HOST_URL + "bovinos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bovino);
        initViews();
        initObjects();
        new GetData().execute();
    }

    private void initViews(){
        fierro = (EditText) findViewById(R.id.fierroAddBovino);
        nombre = (EditText) findViewById(R.id.nombreAddBovino);
        sexo = (Switch) findViewById(R.id.swSexoBovino);
        fecha = (TextView) findViewById(R.id.fechaAddBovino);
        agregarBtn = (Button) findViewById(R.id.addBovinoBtn);

        sexo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sexos = 2;
                }else{
                    sexos = 1;
                }
            }
        });
    }

    private void initObjects(){
        dataHelper = new DataHelper();
        claseList = new ArrayList<>();
        siniigaList = new ArrayList<>();
        razaList = new ArrayList<>();
        empadreList = new ArrayList<>();
        estadoList = new ArrayList<>();
        postId = getIntent().getStringExtra("idbovino");

        if(postId == "0" || postId == null){

            agregarBtn.setText("Agregar");
        }else{
            agregarBtn.setText("Actualizar");
        }

        agregarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(agregarBtn.getText().equals("Agregar")){
                    new uploadData().execute();
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

    private class GetData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog = new ProgressDialog(BovinoActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            if(postId == "0" || postId == null){
                claseList = dataHelper.getClases();
                siniigaList = dataHelper.getSiniigas();
                razaList = dataHelper.getRazas();
                empadreList = dataHelper.getEmpadres();
                estadoList = dataHelper.getEstados();
            }else{
                HttpHandler sh = new HttpHandler();
                String url2 = urla.concat("/"+postId);

                String jsonStr = sh.makeServiceCall(url2);

                if(jsonStr != null){
                    try{
                        JSONArray data = new JSONArray(jsonStr);

                        for(int i = 0; i < data.length(); i++) {

                            JSONObject c = data.getJSONObject(i);

                            fierros = c.getString("fierro");
                            nombres = c.getString("nombre");
                            sexos = Integer.parseInt(c.getString("sexo"));
                            fechas = c.getString("fecha_nacimiento");
                        }

                        claseList = dataHelper.getClases();
                        siniigaList = dataHelper.getSiniigas();
                        razaList = dataHelper.getRazas();
                        empadreList = dataHelper.getEmpadres();
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
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);

            if(progressDialog.isShowing())
                progressDialog.dismiss();

            fierro.setText(fierros);
            nombre.setText(nombres);


            claseSpin = (Spinner) findViewById(R.id.claseSpinBovino);
            siniigaSpin = (Spinner) findViewById(R.id.siniigaSpinBovino);
            razaSpin = (Spinner) findViewById(R.id.razaSpinBovino);
            empadreSpin = (Spinner) findViewById(R.id.empadreSpinBovino);
            estadoSpin = (Spinner) findViewById(R.id.estadoSpinBovino);

            claseAdapter = new ClaseAdapter(BovinoActivity.this, R.layout.custom_spinner_items, claseList);
            siniigaAdapter = new SiniigaAdapter(BovinoActivity.this, R.layout.custom_spinner_items, siniigaList);
            razaAdapter = new RazaAdapter(BovinoActivity.this, R.layout.custom_spinner_items, razaList);
            empadreAdapter = new EmpadreAdapter(BovinoActivity.this, R.layout.custom_spinner_items, empadreList);
            estadoAdapter = new EstadoAdapter(BovinoActivity.this, R.layout.custom_spinner_items, estadoList);

            claseSpin.setAdapter(claseAdapter);
            siniigaSpin.setAdapter(siniigaAdapter);
            razaSpin.setAdapter(razaAdapter);
            empadreSpin.setAdapter(empadreAdapter);
            estadoSpin.setAdapter(estadoAdapter);

            claseSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    clase = Integer.parseInt(claseList.get(position).getClase_id());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            razaSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    raza = Integer.parseInt(razaList.get(position).getRaza_id());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            siniigaSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    siniiga = Integer.parseInt(siniigaList.get(position).getSiniiga_id());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            empadreSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    empadre = Integer.parseInt(empadreList.get(position).getEmpadre_id());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            estadoSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    estado = Integer.parseInt(estadoList.get(position).getEstado_id());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            if(postId == "0" || postId == null){
                fecha.setText(currentTime);
            }else{
                fecha.setText(fechas);
            }
        }
    }

    private class uploadData extends AsyncTask<Void, Void, Void>{

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

                c.put("fierro", fierro.getText().toString());
                c.put("nombre", nombre.getText().toString());
                c.put("clase_bovino_id", clase);
                c.put("siniiga_id", siniiga);
                c.put("raza_bovino_id", raza);
                c.put("empadre_id", empadre);
                c.put("estado_bovino_id", estado);
                c.put("fecha_nacimiento", fecha.getText().toString());
                c.put("fecha_aretado", fecha.getText().toString());
                c.put("sexo", sexos);

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

    private class updateData extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                String url2 = urla.concat("/"+postId);
                URL url = new URL(url2);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("PUT");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");

                JSONObject c = new JSONObject();

                c.put("fierro", fierro.getText().toString());
                c.put("nombre", nombre.getText().toString());
                c.put("clase_bovino_id", clase);
                c.put("siniiga_id", siniiga);
                c.put("raza_bovino_id", raza);
                c.put("empadre_id", empadre);
                c.put("estado_bovino_id", estado);
                //c.put("fecha_nacimiento", fecha.getText().toString());
                //c.put("fecha_aretado", fecha.getText().toString());
                c.put("sexo", sexos);

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

    @Override
    public void onBackPressed() {
            finish();
            claseList.clear();
            razaList.clear();
            siniigaList.clear();
            empadreList.clear();
            estadoList.clear();
    }
}
