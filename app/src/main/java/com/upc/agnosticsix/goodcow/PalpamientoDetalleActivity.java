package com.upc.agnosticsix.goodcow;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import model.DataHelper;
import model.Palpamientos;

import static model.DataHelper.HOST_URL;

public class PalpamientoDetalleActivity extends AppCompatActivity {

    private String TAG = PalpamientoDetalleActivity.class.getSimpleName();
    private String postId, vacas, resultados, empleados, fechas, observas;
    private ProgressDialog progressDialog;
    private static String url = HOST_URL + "palpamientos/";
    private List<Palpamientos> palList;
    private TextView vaca, resultado, empleado, fecha, observa;
    private DataHelper dataHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palpamiento_detalle);

        initObjects();
        initViews();
        new GetData().execute();
    }

    private void initViews(){
        vaca = (TextView) findViewById(R.id.vacaPalDetalles);
        resultado = (TextView) findViewById(R.id.resultadoPalDetalles);
        empleado = (TextView) findViewById(R.id.empleadoPalDetalles);
        fecha = (TextView) findViewById(R.id.fechaPalDetalles);
        observa = (TextView) findViewById(R.id.observacionesPalDetalles);
    }

    private void initObjects(){
        postId = getIntent().getStringExtra("idbovinopal2");
        dataHelper = new DataHelper();
        palList = new ArrayList<>();
    }

    private class GetData extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PalpamientoDetalleActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String url2 = url.concat(postId);

            String jsonStr = sh.makeServiceCall(url2);

            if(jsonStr != null){
                try{
                    JSONArray data = new JSONArray(jsonStr);

                    for(int i = 0; i < data.length(); i++) {

                        JSONObject c = data.getJSONObject(i);

                        vacas = dataHelper.getCow(c.getString("bovino_id"));
                        resultados = dataHelper.getResPalpamiento(c.getString("resultado_palpamiento_id"));
                        empleados = dataHelper.getEmpleado(c.getString("empleado_id"));
                        fechas = c.getString("fecha");
                        observas = c.getString("observaciones");
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
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(progressDialog.isShowing())
                progressDialog.dismiss();

            vaca.setText(vacas);
            resultado.setText(resultados);
            empleado.setText(empleados);
            fecha.setText(fechas);
            observa.setText(observas);

        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
