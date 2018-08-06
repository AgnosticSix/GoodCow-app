package com.upc.agnosticsix.goodcow;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import model.DataHelper;

public class VacunasActivity extends AppCompatActivity {

    private String TAG = VacunasActivity.class.getSimpleName();
    private Spinner vacspin, empspin;
    private ProgressDialog progressDialog;
    private TextView bovino, fecha;
    private String bovinos, fechas, vacuna, empleado;
    private DataHelper dataHelper;
    private static String url = "http://goodcow-api-goodcow.7e14.starter-us-west-2.openshiftapps.com/bovinos/";
    private static String urlVacuna = "http://goodcow-api-goodcow.7e14.starter-us-west-2.openshiftapps.com/vacunas";
    private static String urlEmpleado = "http://goodcow-api-goodcow.7e14.starter-us-west-2.openshiftapps.com/empleados";
    String idvacuna;

    String url2;
    ArrayList<String> vacunaList;
    ArrayList<String> empleadoList;
    //ArrayList<String> vacunaList2;
    //ArrayList<String> empleadoList2;
    ArrayAdapter<String> vacunaAdapter, empleadoAdapter;

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
        vacspin = (Spinner) findViewById(R.id.vacspin);
        empspin = (Spinner) findViewById(R.id.empspin);


    }

    private void initObjects(){
        final String idintent = getIntent().getStringExtra("idbovino");

        dataHelper = new DataHelper();
        url2 = url.concat(idintent);
        vacunaList = new ArrayList<>();
        empleadoList = new ArrayList<>();
        //vacunaList2 = new ArrayList<>();
        //empleadoList2 = new ArrayList<>();
        vacunaList = DataHelper.getVacunas();
        empleadoList = DataHelper.getEmpleados();
        vacunaAdapter = new ArrayAdapter<>(VacunasActivity.this, R.layout.activity_vacunas, vacunaList);
        empleadoAdapter = new ArrayAdapter<>(VacunasActivity.this, R.layout.activity_vacunas, empleadoList);
        vacunaAdapter.setDropDownViewResource(R.layout.activity_vacunas);
        empleadoAdapter.setDropDownViewResource(R.layout.activity_vacunas);
        vacspin.setAdapter(vacunaAdapter);
        empspin.setAdapter(empleadoAdapter);
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
                        //cowList.add(cowData);
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

            bovino.setText(bovinos);
            fecha.setText(fechas);



        }
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}
