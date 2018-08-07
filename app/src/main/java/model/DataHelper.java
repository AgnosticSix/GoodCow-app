package model;

import android.util.Log;
import com.upc.agnosticsix.goodcow.HttpHandler;
import com.upc.agnosticsix.goodcow.ICallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DataHelper {
    private static String urlSiniiga = "http://goodcow-api-goodcow.7e14.starter-us-west-2.openshiftapps.com/siniigas/";
    private static String urlClase = "http://goodcow-api-goodcow.7e14.starter-us-west-2.openshiftapps.com/clases_bovinos/";
    private static String urlRaza = "http://goodcow-api-goodcow.7e14.starter-us-west-2.openshiftapps.com/razas_bovinos/";
    private static String urlEmpadre = "http://goodcow-api-goodcow.7e14.starter-us-west-2.openshiftapps.com/empadres/";
    private static String urlEstado = "http://goodcow-api-goodcow.7e14.starter-us-west-2.openshiftapps.com/estados/";

    private static String urlVacuna = "http://goodcow-api-goodcow.7e14.starter-us-west-2.openshiftapps.com/vacunas";
    private static String urlEmpleado = "http://goodcow-api-goodcow.7e14.starter-us-west-2.openshiftapps.com/empleados";
    private static String TAG2 = "";
    private static String TAG = "LOOKUP-MATEO";
    private static String siniiga, clase, raza, empadre, estado, bovino, vacuna, empleado, sexo;
    static ArrayList<String> vacunaList = new ArrayList<>();
    static ArrayList<String> empleadoList = new ArrayList<>();

    public String getClase(String id){
        HttpHandler sh = new HttpHandler();
        String url = urlClase.concat(id);

        String jsonStr = sh.makeServiceCall(url);

        if(jsonStr != null){
            try{
                JSONArray data = new JSONArray(jsonStr);

                for(int i = 0; i < data.length(); i++){
                    JSONObject c = data.getJSONObject(i);
                    clase = c.getString("nombre");

                }
            }catch (final JSONException e){
                Log.e(TAG,"Json parsing error: " + e.getMessage());
            }
        }
        return clase;
    }

    public String getSiniiga(String id){
        HttpHandler sh = new HttpHandler();
        String url = urlSiniiga.concat(id);

        String jsonStr = sh.makeServiceCall(url);

        if(jsonStr != null){
            try{
                JSONArray data = new JSONArray(jsonStr);

                for(int i = 0; i < data.length(); i++){
                    JSONObject c = data.getJSONObject(i);
                    siniiga = c.getString("codigo");

                }
            }catch (final JSONException e){
                Log.e(TAG,"Json parsing error: " + e.getMessage());
            }
        }
        return siniiga;
    }

    public String getRaza(String id){
        HttpHandler sh = new HttpHandler();
        String url = urlRaza.concat(id);

        String jsonStr = sh.makeServiceCall(url);

        if(jsonStr != null){
            try{
                JSONArray data = new JSONArray(jsonStr);

                for(int i = 0; i < data.length(); i++){
                    JSONObject c = data.getJSONObject(i);
                    raza = c.getString("nombre");

                }
            }catch (final JSONException e){
                Log.e(TAG,"Json parsing error: " + e.getMessage());
            }
        }
        return raza;
    }

    public String getEmpadre(String id){
        HttpHandler sh = new HttpHandler();
        String url = urlEmpadre.concat(id);

        String jsonStr = sh.makeServiceCall(url);

        if(jsonStr != null){
            try{
                JSONArray data = new JSONArray(jsonStr);

                for(int i = 0; i < data.length(); i++){
                    JSONObject c = data.getJSONObject(i);
                    empadre = c.getString("nombre");

                }
            }catch (final JSONException e){
                Log.e(TAG,"Json parsing error: " + e.getMessage());
            }
        }
        return empadre;
    }

    public String getEstado(String id){
        HttpHandler sh = new HttpHandler();
        String url = urlEstado.concat(id);

        //final String finalJsonStr = jsonStr;
        String jsonStr = sh.makeServiceCall(url);

        if(jsonStr != null){
            try{
                JSONArray data = new JSONArray(jsonStr);

                for(int i = 0; i < data.length(); i++){
                    JSONObject c = data.getJSONObject(i);
                    estado = c.getString("nombre");

                }
            }catch (final JSONException e){
                Log.e(TAG,"Json parsing error: " + e.getMessage());
            }
        }
        return estado;
    }

    public static ArrayList<String> getVacunas(){
        HttpHandler sh = new HttpHandler();
        String jsonStr = sh.makeServiceCall(urlVacuna);
        Log.i(TAG, urlVacuna+"\n"+jsonStr+"");

        if(jsonStr != null){
            try{
                JSONArray data = new JSONArray(jsonStr);

                for(int i = 0; i < data.length(); i++){
                    JSONObject c = data.getJSONObject(i);
                    vacuna = c.getString("nombre");

                    vacunaList.add(vacuna);
                }
            }catch (final JSONException e){
                Log.e(TAG,"Json parsing error: " + e.getMessage());
            }
        }
        return vacunaList;
    }

    public static ArrayList<String> getEmpleados(){
        HttpHandler sh = new HttpHandler();

        String jsonStr = sh.makeServiceCall(urlEmpleado);
        Log.i(TAG, urlVacuna+"\n"+jsonStr+"");
        if(jsonStr != null){
            try{
                JSONArray data = new JSONArray(jsonStr);

                for(int i = 0; i < data.length(); i++){
                    JSONObject c = data.getJSONObject(i);
                    empleado = c.getString("empleado_id");

                    empleadoList.add(empleado);
                }
            }catch (final JSONException e){
                Log.e(TAG2,"Json parsing error: " + e.getMessage());
            }
        }
        return empleadoList;
    }
}
