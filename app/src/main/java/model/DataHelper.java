package model;

import android.util.Log;
import com.upc.agnosticsix.goodcow.HttpHandler;

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
    private static String urlResPal = "http://goodcow-api-goodcow.7e14.starter-us-west-2.openshiftapps.com/resultados_palpamientos";
    private static String TAG2 = "";
    private static String TAG = "LOOKUP-MATEO";
    private static String siniiga, clase, raza, empadre, estado, bovino, sexo;
    static List<Vacunas> vacunaList = new ArrayList<>();
    static List<Empleados> empleadoList = new ArrayList<>();

    static List<Clases> clasesList = new ArrayList<>();
    static List<Siniigas> siniigasList = new ArrayList<>();
    static List<Razas> razasList = new ArrayList<>();
    static List<Empadres> empadresList = new ArrayList<>();
    static List<Estados> estadosList = new ArrayList<>();
    static List<ResultadosPalpamientos> resultadosPalpamientosList = new ArrayList<>();

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

    public static List<Vacunas> getVacunas(){
        HttpHandler sh = new HttpHandler();
        String jsonStr = sh.makeServiceCall(urlVacuna);
        //Log.i(TAG, urlVacuna+"\n"+jsonStr+"");

        if(jsonStr != null){
            try{
                JSONArray data = new JSONArray(jsonStr);

                for(int i = 0; i < data.length(); i++){
                    JSONObject c = data.getJSONObject(i);
                    Vacunas vacunas = new Vacunas(c.getString("vacuna_id"),
                            c.getString("nombre"));

                    vacunaList.add(vacunas);
                }
            }catch (final JSONException e){
                Log.e(TAG,"Json parsing error: " + e.getMessage());
            }
        }
        return vacunaList;
    }

    public static List<Empleados> getEmpleados(){
        HttpHandler sh = new HttpHandler();

        String jsonStr = sh.makeServiceCall(urlEmpleado);

        if(jsonStr != null){
            try{
                JSONArray data = new JSONArray(jsonStr);

                for(int i = 0; i < data.length(); i++){
                    JSONObject c = data.getJSONObject(i);
                    Empleados empleados = new Empleados(c.getString("empleado_id"));

                    empleadoList.add(empleados);
                }
            }catch (final JSONException e){
                Log.e(TAG2,"Json parsing error: " + e.getMessage());
            }
        }
        return empleadoList;
    }

    public static List<Clases> getClases(){
        HttpHandler sh = new HttpHandler();
        String url = urlClase.substring(0, urlClase.length()-1);
        String jsonStr = sh.makeServiceCall(url);
        //Log.i(TAG, urlVacuna+"\n"+jsonStr+"");

        if(jsonStr != null){
            try{
                JSONArray data = new JSONArray(jsonStr);

                for(int i = 0; i < data.length(); i++){
                    JSONObject c = data.getJSONObject(i);

                    Clases clases = new Clases(c.getString("clase_bovino_id"),
                            c.getString("nombre"));

                    clasesList.add(clases);
                }
            }catch (final JSONException e){
                Log.e(TAG,"Json parsing error: " + e.getMessage());
            }
        }
        return clasesList;
    }

    public static List<Siniigas> getSiniigas(){
        HttpHandler sh = new HttpHandler();
        String url = urlSiniiga.substring(0, urlSiniiga.length()-1);
        String jsonStr = sh.makeServiceCall(url);
        //Log.i(TAG, urlVacuna+"\n"+jsonStr+"");

        if(jsonStr != null){
            try{
                JSONArray data = new JSONArray(jsonStr);

                for(int i = 0; i < data.length(); i++){
                    JSONObject c = data.getJSONObject(i);

                    Siniigas siniigas = new Siniigas(c.getString("siniiga_id"),
                            c.getString("codigo"));

                    siniigasList.add(siniigas);
                }
            }catch (final JSONException e){
                Log.e(TAG,"Json parsing error: " + e.getMessage());
            }
        }
        return siniigasList;
    }

    public static List<Razas> getRazas(){
        HttpHandler sh = new HttpHandler();
        String url = urlRaza.substring(0, urlRaza.length()-1);
        String jsonStr = sh.makeServiceCall(url);
        //Log.i(TAG, urlVacuna+"\n"+jsonStr+"");

        if(jsonStr != null){
            try{
                JSONArray data = new JSONArray(jsonStr);

                for(int i = 0; i < data.length(); i++){
                    JSONObject c = data.getJSONObject(i);

                    Razas razas = new Razas(c.getString("raza_bovino_id"),
                            c.getString("nombre"));

                    razasList.add(razas);
                }
            }catch (final JSONException e){
                Log.e(TAG,"Json parsing error: " + e.getMessage());
            }
        }
        return razasList;
    }

    public static List<Empadres> getEmpadres(){
        HttpHandler sh = new HttpHandler();
        String url = urlEmpadre.substring(0, urlEmpadre.length()-1);
        String jsonStr = sh.makeServiceCall(url);
        //Log.i(TAG, urlVacuna+"\n"+jsonStr+"");

        if(jsonStr != null){
            try{
                JSONArray data = new JSONArray(jsonStr);

                for(int i = 0; i < data.length(); i++){
                    JSONObject c = data.getJSONObject(i);

                    Empadres empadres = new Empadres(c.getString("empadre_id"),
                            c.getString("nombre"));

                    empadresList.add(empadres);
                }
            }catch (final JSONException e){
                Log.e(TAG,"Json parsing error: " + e.getMessage());
            }
        }
        return empadresList;
    }

    public static List<Estados> getEstados(){
        HttpHandler sh = new HttpHandler();
        String url = urlEstado.substring(0, urlEstado.length()-1);
        String jsonStr = sh.makeServiceCall(url);
        //Log.i(TAG, urlVacuna+"\n"+jsonStr+"");

        if(jsonStr != null){
            try{
                JSONArray data = new JSONArray(jsonStr);

                for(int i = 0; i < data.length(); i++){
                    JSONObject c = data.getJSONObject(i);

                    Estados estados = new Estados(c.getString("estado_id"),
                            c.getString("nombre"));

                    estadosList.add(estados);
                }
            }catch (final JSONException e){
                Log.e(TAG,"Json parsing error: " + e.getMessage());
            }
        }
        return estadosList;
    }

    public static List<ResultadosPalpamientos> getResPalpamientos(){
        HttpHandler sh = new HttpHandler();
        String jsonStr = sh.makeServiceCall(urlResPal);
        //Log.i(TAG, urlVacuna+"\n"+jsonStr+"");

        if(jsonStr != null){
            try{
                JSONArray data = new JSONArray(jsonStr);

                for(int i = 0; i < data.length(); i++){
                    JSONObject c = data.getJSONObject(i);

                    ResultadosPalpamientos resultadosPalpamientos = new ResultadosPalpamientos(c.getString("resultado_palpamiento_id"),
                            c.getString("nombre"));

                    resultadosPalpamientosList.add(resultadosPalpamientos);
                }
            }catch (final JSONException e){
                Log.e(TAG,"Json parsing error: " + e.getMessage());
            }
        }
        return resultadosPalpamientosList;
    }
}
