package model;

public class Empleados {
    private String id, nombre;

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Empleados(String id, String nombre){
        this.id = id;
        this.nombre = nombre;
    }
}
