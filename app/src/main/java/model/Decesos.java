package model;

public class Decesos {
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

    public Decesos(String id, String nombre){
        this.id = id;
        this.nombre = nombre;
    }
}
