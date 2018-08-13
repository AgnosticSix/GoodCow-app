package com.upc.agnosticsix.goodcow;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import adapters.EmpleadoAdapter;
import adapters.VacunaAdapter;
import model.DataHelper;
import model.Empleados;
import model.Vacunas;

public class VacunasActivity extends AppCompatActivity {

    private String TAG = VacunasActivity.class.getSimpleName();
    private Spinner vacspin, empspin;
    private ProgressDialog progressDialog;
    private TextView bovino, fecha;
    private String bovinos, fechas, vacuna, empleado;
    private DataHelper dataHelper;
    private static String url = "http://goodcow-api-goodcow.7e14.starter-us-west-2.openshiftapps.com/bovinos/";
    String url2;
    List<Vacunas> vacunaList;
    List<Empleados> empleadoList;
    VacunaAdapter vacunaAdapter;
    EmpleadoAdapter empleadoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacunas);
        initViews();
        initObjects();
        new GetData().execute();
    }

    private void initViews(){

        bovino = (TextView) findViewById(R.id.bovinovacuna);
        fecha = (TextView) findViewById(R.id.fechavacuna);

    }

    private void initObjects(){
        final String idintent = getIntent().getStringExtra("idbovino");
        dataHelper = new DataHelper();
        url2 = url.concat(idintent);
        vacunaList = new ArrayList<>();
        empleadoList = new ArrayList<>();
    }

    private class GetData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog = new ProgressDialog(VacunasActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(url2);
            String currentTime = Calendar.getInstance().getTime().toString();

            if(jsonStr != null){

                try{
                    JSONArray data = new JSONArray(jsonStr);

                    for(int i = 0; i < data.length(); i++) {

                        JSONObject c = data.getJSONObject(i);

                        bovinos = c.getString("nombre");
                        fechas = currentTime;
                    }
                    vacunaList = DataHelper.getVacunas();
                    empleadoList = DataHelper.getEmpleados();

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

            bovino.setText(bovinos);
            fecha.setText(fechas);
            vacspin = (Spinner) findViewById(R.id.vacspin);
            empspin = (Spinner) findViewById(R.id.empspin);
            vacunaAdapter = new VacunaAdapter(VacunasActivity.this, R.layout.custom_spinner_items, vacunaList);
            empleadoAdapter = new EmpleadoAdapter(VacunasActivity.this, R.layout.custom_spinner_items, empleadoList);

            vacspin.setAdapter(vacunaAdapter);
            empspin.setAdapter(empleadoAdapter);

        }
    }
    @Override
    public void onBackPressed() {
        finish();
    }

    //TODO: uploadMethod
}
