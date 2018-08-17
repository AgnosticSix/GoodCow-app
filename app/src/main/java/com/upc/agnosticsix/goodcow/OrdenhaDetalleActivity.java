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
import model.Ordenhas;

import static model.DataHelper.HOST_URL;

public class OrdenhaDetalleActivity extends AppCompatActivity {

    private String TAG = OrdenhaDetalleActivity.class.getSimpleName();
    private String postId, vacas, empleados, fechas, observas, cantidads;
    private ProgressDialog progressDialog;
    private static String url = HOST_URL + "ordenhas/";
    private List<Ordenhas> ordeList;
    private TextView vaca, empleado, fecha, observa, cantidad;
    private DataHelper dataHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordenha_detalle);

        initObjects();
        initViews();
        new GetData().execute();
    }

    private void initViews(){
        vaca = (TextView) findViewById(R.id.vacaOrdenaDetalles);
        empleado = (TextView) findViewById(R.id.empleadoOrdenaDetalles);
        fecha = (TextView) findViewById(R.id.fechaOrdenaDetalles);
        observa = (TextView) findViewById(R.id.observaOrdeDetalles);
        cantidad = (TextView) findViewById(R.id.cantidadOrdeDetalles);
    }

    private void initObjects(){
        postId = getIntent().getStringExtra("idbovinoorde2");
        ordeList = new ArrayList<>();
        dataHelper = new DataHelper();

    }

    private class GetData extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(OrdenhaDetalleActivity.this);
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
                        empleados = dataHelper.getEmpleado(c.getString("empleado_id"));
                        fechas = c.getString("fecha");
                        observas = c.getString("observaciones");
                        cantidads = c.getString("cantidad");
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
            empleado.setText(empleados);
            fecha.setText(fechas);
            observa.setText(observas);
            cantidad.setText(cantidads);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
