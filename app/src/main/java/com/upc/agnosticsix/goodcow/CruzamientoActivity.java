package com.upc.agnosticsix.goodcow;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private Spinner sementalSpin, empleadoSpin, estadoSpin;
    private TextView fecha;
    private EditText descripcion;
    private ProgressDialog progressDialog;
    List<Cow> sementalList;
    List<Empleados> empleadoList;
    List<Estados> estadoList;
    EmpleadoAdapter empleadoAdapter;
    BovinoAdapter sementalAdapter;
    EstadoAdapter estadoAdapter;
    private String semental, currentTime;
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
    }

    private void initObjects(){
        dataHelper = new DataHelper();
        sementalList = new ArrayList<>();
        empleadoList = new ArrayList<>();
        estadoList = new ArrayList<>();

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

            if(jsonStr != null){
                try{
                    JSONArray data = new JSONArray(jsonStr);

                    for(int i = 0; i < data.length(); i++) {

                        JSONObject c = data.getJSONObject(i);
                        Cow cow = new Cow(c.getString("bovino_id"),
                                c.getString("fierro"),
                                c.getString("nombre"));

                        sementalList.add(cow);
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
            estadoSpin = (Spinner) findViewById(R.id.estadoSpinCruza);
            sementalAdapter = new BovinoAdapter(CruzamientoActivity.this, R.layout.custom_spinner_items, sementalList);
            empleadoAdapter = new EmpleadoAdapter(CruzamientoActivity.this, R.layout.custom_spinner_items, empleadoList);
            estadoAdapter = new EstadoAdapter(CruzamientoActivity.this, R.layout.custom_spinner_items, estadoList);
            sementalSpin.setAdapter(sementalAdapter);
            empleadoSpin.setAdapter(empleadoAdapter);
            estadoSpin.setAdapter(estadoAdapter);

        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    //TODO: uploadMethod
}
