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

import model.Cruzamientos;
import model.DataHelper;

import static model.DataHelper.HOST_URL;

public class CruzamientoDetalleActivity extends AppCompatActivity {

    private String TAG = DetallesActivity.class.getSimpleName();
    private String postId, vacas, sementals, empleados, fechas, estados, descripcions;
    private ProgressDialog progressDialog;
    private static String url = HOST_URL + "cruzamientos/";
    private List<Cruzamientos> cruzaList;
    private TextView vaca, semental, empleado, fecha, estado, descripcion;
    private DataHelper dataHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cruzamiento_detalle);

        initObjects();
        initViews();
        new GetData().execute();
    }

    private void initViews(){
        vaca = (TextView) findViewById(R.id.vacaCruzaDetalles);
        semental = (TextView) findViewById(R.id.sementalCruzaDetalles);
        empleado = (TextView) findViewById(R.id.empleadoCruzaDetalles);
        fecha = (TextView) findViewById(R.id.fechaCruzaDetalles);
        estado = (TextView) findViewById(R.id.estadoCruzaDetalles);
        descripcion = (TextView) findViewById(R.id.descripcionCruzaDetalles);
    }

    private void initObjects(){
        postId = getIntent().getStringExtra("idbovinocruza2");
        cruzaList = new ArrayList<>();
        dataHelper = new DataHelper();

    }

    private class GetData extends AsyncTask<Void,Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CruzamientoDetalleActivity.this);
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

                        vacas = dataHelper.getCow(c.getString("vaca_id"));
                        sementals = dataHelper.getCow(c.getString("semental_id"));
                        empleados = dataHelper.getEmpleado(c.getString("empleado_id"));
                        fechas = c.getString("fecha");
                        estados = c.getString("estado").equals("1") ? "Realizado" : "Pendiente";
                        descripcions = c.getString("descripcion");
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
            semental.setText(sementals);
            empleado.setText(empleados);
            fecha.setText(fechas);
            estado.setText(estados);
            descripcion.setText(descripcions);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
