package model;

public class Razas {
    private String raza_id, nombre, descripcion;

    public String getRaza_id() {
        return raza_id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setRaza_id(String raza_id) {
        this.raza_id = raza_id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Razas(String id, String nombre){
        this.raza_id = id;
        this.nombre = nombre;
    }
}
