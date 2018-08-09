package model;

public class Empadres {
    private String empadre_id, nombre, descripcion;

    public String getEmpadre_id() {
        return empadre_id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setEmpadre_id(String empadre_id) {
        this.empadre_id = empadre_id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Empadres(String id, String nombre){
        this.empadre_id = id;
        this.nombre = nombre;
    }
}
