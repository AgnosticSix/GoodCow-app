package model;

public class Vacunas {
    private String id, nombre, descripcion, dosis, frecuencia;

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Vacunas(String id, String nombre){
        this.id = id;
        this.nombre = nombre;
    }
}
