package model;

public class Estados {
    private String estado_id, pais_id, nombre, codigo;

    public String getEstado_id() {
        return estado_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setEstado_id(String estado_id) {
        this.estado_id = estado_id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Estados(String id, String nombre){
        this.estado_id = id;
        this.nombre = nombre;
    }
}
