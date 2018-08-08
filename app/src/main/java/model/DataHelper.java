package model;

import android.util.Log;
import com.upc.agnosticsix.goodcow.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
    static ArrayList<String> claseList = new ArrayList<>();
    static ArrayList<String> razaList = new ArrayList<>();
    static ArrayList<String> empadreList = new ArrayList<>();
    static ArrayList<String> estadoList = new ArrayList<>();
    static ArrayList<String> siniigaList = new ArrayList<>();

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
        //Log.i(TAG, urlVacuna+"\n"+jsonStr+"");

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
        //Log.i(TAG, urlVacuna+"\n"+jsonStr+"");
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

    public static ArrayList<String> getClases(){
        HttpHandler sh = new HttpHandler();
        String url = urlClase.substring(0, urlClase.length()-1);
        String jsonStr = sh.makeServiceCall(url);
        //Log.i(TAG, urlVacuna+"\n"+jsonStr+"");

        if(jsonStr != null){
            try{
                JSONArray data = new JSONArray(jsonStr);

                for(int i = 0; i < data.length(); i++){
                    JSONObject c = data.getJSONObject(i);
                    clase = c.getString("clase_bovino_id");
                    clase = c.getString("nombre");

                    claseList.add(clase);
                }
            }catch (final JSONException e){
                Log.e(TAG,"Json parsing error: " + e.getMessage());
            }
        }
        return claseList;
    }

    public static ArrayList<String> getSiniigas(){
        HttpHandler sh = new HttpHandler();
        String url = urlSiniiga.substring(0, urlSiniiga.length()-1);
        String jsonStr = sh.makeServiceCall(url);
        //Log.i(TAG, urlVacuna+"\n"+jsonStr+"");

        if(jsonStr != null){
            try{
                JSONArray data = new JSONArray(jsonStr);

                for(int i = 0; i < data.length(); i++){
                    JSONObject c = data.getJSONObject(i);
                    siniiga = c.getString("siniiga_id");
                    siniiga = c.getString("codigo");

                    siniigaList.add(siniiga);
                }
            }catch (final JSONException e){
                Log.e(TAG,"Json parsing error: " + e.getMessage());
            }
        }
        return siniigaList;
    }

    public static ArrayList<String> getRazas(){
        HttpHandler sh = new HttpHandler();
        String url = urlRaza.substring(0, urlRaza.length()-1);
        String jsonStr = sh.makeServiceCall(url);
        //Log.i(TAG, urlVacuna+"\n"+jsonStr+"");

        if(jsonStr != null){
            try{
                JSONArray data = new JSONArray(jsonStr);

                for(int i = 0; i < data.length(); i++){
                    JSONObject c = data.getJSONObject(i);
                    raza = c.getString("raza_bovino_id");
                    raza = c.getString("nombre");

                    razaList.add(raza);
                }
            }catch (final JSONException e){
                Log.e(TAG,"Json parsing error: " + e.getMessage());
            }
        }
        return razaList;
    }

    public static ArrayList<String> getEmpadres(){
        HttpHandler sh = new HttpHandler();
        String url = urlEmpadre.substring(0, urlEmpadre.length()-1);
        String jsonStr = sh.makeServiceCall(url);
        //Log.i(TAG, urlVacuna+"\n"+jsonStr+"");

        if(jsonStr != null){
            try{
                JSONArray data = new JSONArray(jsonStr);

                for(int i = 0; i < data.length(); i++){
                    JSONObject c = data.getJSONObject(i);
                    empadre = c.getString("empadre_id");
                    empadre = c.getString("nombre");

                    empadreList.add(empadre);
                }
            }catch (final JSONException e){
                Log.e(TAG,"Json parsing error: " + e.getMessage());
            }
        }
        return empadreList;
    }

    public static ArrayList<String> getEstados(){
        HttpHandler sh = new HttpHandler();
        String url = urlEstado.substring(0, urlEstado.length()-1);
        String jsonStr = sh.makeServiceCall(url);
        //Log.i(TAG, urlVacuna+"\n"+jsonStr+"");

        if(jsonStr != null){
            try{
                JSONArray data = new JSONArray(jsonStr);

                for(int i = 0; i < data.length(); i++){
                    JSONObject c = data.getJSONObject(i);
                    estado = c.getString("estado_id");
                    estado = c.getString("nombre");

                    estadoList.add(estado);
                }
            }catch (final JSONException e){
                Log.e(TAG,"Json parsing error: " + e.getMessage());
            }
        }
        return estadoList;
    }
}
