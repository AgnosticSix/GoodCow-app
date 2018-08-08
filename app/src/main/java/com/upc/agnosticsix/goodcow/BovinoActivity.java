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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import model.DataHelper;

public class BovinoActivity extends AppCompatActivity {

    private String TAG = BovinoActivity.class.getSimpleName();
    private Spinner claseSpin, siniigaSpin, razaSpin, empadreSpin, estadoSpin;
    private EditText fierro, nombre;
    private TextView fecha;
    private Switch sexo;
    private Button agregarBtn;
    private ProgressDialog progressDialog;
    private String fechas, sexos, currentTime, response;
    ArrayList<String> claseList, siniigaList, razaList, empadreList, estadoList;
    ArrayAdapter<String> claseAdapter, siniigaAdapter, razaAdapter, empadreAdapter, estadoAdapter;
    private DataHelper dataHelper;
    private long clase, siniiga, raza, empadre, estado, fierroStr, nombreStr;
    private static String urla = "http://goodcow-api-goodcow.7e14.starter-us-west-2.openshiftapps.com/bovinos";

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
                    sexos = "2";
                }else{
                    sexos = "1";
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
        currentTime = Calendar.getInstance().getTime().toString();
    }

    private void onClick(View v){

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

            claseList = dataHelper.getClases();
            siniigaList = dataHelper.getSiniigas();
            razaList = dataHelper.getRazas();
            empadreList = dataHelper.getEmpadres();
            estadoList = dataHelper.getEstados();
            agregarBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadData();
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);

            if(progressDialog.isShowing())
                progressDialog.dismiss();

            fecha.setText(currentTime);

            claseSpin = (Spinner) findViewById(R.id.claseSpinBovino);
            siniigaSpin = (Spinner) findViewById(R.id.siniigaSpinBovino);
            razaSpin = (Spinner) findViewById(R.id.razaSpinBovino);
            empadreSpin = (Spinner) findViewById(R.id.empadreSpinBovino);
            estadoSpin = (Spinner) findViewById(R.id.estadoSpinBovino);

            claseAdapter = new ArrayAdapter<>(BovinoActivity.this, R.layout.support_simple_spinner_dropdown_item, claseList);
            siniigaAdapter = new ArrayAdapter<>(BovinoActivity.this, R.layout.support_simple_spinner_dropdown_item, siniigaList);
            razaAdapter = new ArrayAdapter<>(BovinoActivity.this, R.layout.support_simple_spinner_dropdown_item, razaList);
            empadreAdapter = new ArrayAdapter<>(BovinoActivity.this, R.layout.support_simple_spinner_dropdown_item, empadreList);
            estadoAdapter = new ArrayAdapter<>(BovinoActivity.this, R.layout.support_simple_spinner_dropdown_item, estadoList);

            claseSpin.setAdapter(claseAdapter);
            siniigaSpin.setAdapter(siniigaAdapter);
            razaSpin.setAdapter(razaAdapter);
            empadreSpin.setAdapter(empadreAdapter);
            estadoSpin.setAdapter(estadoAdapter);

            clase = claseSpin.getSelectedItemId();
            siniiga = siniigaSpin.getSelectedItemId();
            raza = razaSpin.getSelectedItemId();
            empadre = empadreSpin.getSelectedItemId();
            estado = estadoSpin.getSelectedItemId();


        }
    }

    public void uploadData(){

        try {
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

            OutputStream os = conn.getOutputStream();
            os.write(output);

            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            Log.i(TAG, responseCode+"");

            if(responseCode == HttpsURLConnection.HTTP_CREATED){
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                //StringBuffer sb = new StringBuffer("");
                String line = "";

                while((line = br.readLine()) != null) {
                    response += line;
                    break;
                }

                br.close();
            }else{
                Toast.makeText(getApplicationContext(), ""+ responseCode,Toast.LENGTH_LONG).show();
                response = "";
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
    }


    @Override
    public void onBackPressed() {
            finish();
    }
}
