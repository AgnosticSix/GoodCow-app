package com.upc.agnosticsix.goodcow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import model.Cow;
import model.DataHelper;

public class DetallesActivity extends AppCompatActivity {

    private String TAG = DetallesActivity.class.getSimpleName();
    private String idbovino, fierros, nombres, sexos, siniigaa, clases, razas, empadres, estados, fechas;
    private ProgressDialog progressDialog;
    private static String url = "http://goodcow-api-goodcow.7e14.starter-us-west-2.openshiftapps.com/bovinos/";
    String url2;
    private List<Cow> cowList;
    private TextView fierro, nombre, sexo, clase, siniiga, raza, empadre, fecha, estado;
    private DataHelper dataHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);

        initObjects();
        initViews();
        new GetData().execute();
    }

    private void initViews(){

        fierro = (TextView) findViewById(R.id.fierroDetalles);
        nombre = (TextView) findViewById(R.id.nombreDetalles);
        sexo = (TextView) findViewById(R.id.sexoDetalles);
        clase = (TextView) findViewById(R.id.claseDetalles);
        siniiga = (TextView) findViewById(R.id.siniigaDetalles);
        raza = (TextView) findViewById(R.id.razaDetalles);
        empadre = (TextView) findViewById(R.id.empadreDetalles);
        fecha = (TextView) findViewById(R.id.fechaDetalles);
        estado = (TextView) findViewById(R.id.estadoDetalles);
    }

    private void initObjects(){
        final String idintent = getIntent().getStringExtra("idbovino");
        url2 = url.concat(idintent);
        dataHelper = new DataHelper();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetallesActivity.this, VacunasActivity.class);
                intent.putExtra("idbovino", idintent);
                startActivity(intent);

            }
        });
    }

    private class GetData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog = new ProgressDialog(DetallesActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(url2);

            if(jsonStr != null){
                try{
                    JSONArray data = new JSONArray(jsonStr);

                    for(int i = 0; i < data.length(); i++) {

                        JSONObject c = data.getJSONObject(i);

                        fierros = c.getString("fierro");
                        nombres = c.getString("nombre");
                        sexos = c.getString("sexo").equals("1") ? "Macho" : "Hembra";
                        clases = dataHelper.getClase(c.getString("clase_bovino_id"));
                        siniigaa = dataHelper.getSiniiga(c.getString("siniiga_id"));
                        razas = dataHelper.getRaza(c.getString("raza_bovino_id"));
                        empadres = dataHelper.getEmpadre(c.getString("empadre_id"));
                        fechas = c.getString("fecha_nacimiento");
                        estados = dataHelper.getEstado(c.getString("estado_bovino_id"));
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

            fierro.setText(fierros);
            nombre.setText(nombres);
            sexo.setText(sexos);
            clase.setText(clases);
            siniiga.setText(siniigaa);
            raza.setText(razas);
            empadre.setText(empadres);
            fecha.setText(fechas);
            estado.setText(estados);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
