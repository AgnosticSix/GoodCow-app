package model;

public class Clases {
    private String clase_id, nombre, descripcion;

    public String getClase_id() {
        return clase_id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setClase_id(String clase_id) {
        this.clase_id = clase_id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Clases(String id, String nombre){
        this.clase_id = id;
        this.nombre = nombre;
    }
}
